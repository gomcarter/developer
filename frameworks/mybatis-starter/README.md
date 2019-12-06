在你的项目中引入依赖: https://mvnrepository.com/artifact/com.gomcarter.frameworks/mybatis-starter

### 使用指南（目前仅支持 nacos 配置中心，<a href="https://github.com/gomcarter/developer/blob/master/README.md">配置中心配置参考</a>）

```
@SpringBootApplication
// 打上标签， 一般来说来说，就这么配置就 ok 了
@EnableNacosMybatis(dataId = "DATA_ID", group = "GROUP_NAME")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 读写分离与支持事务

nacos配置如下：
```
dataId: DATA_ID
group: GROUP_NAME

#主库配置
write.jdbc.url=jdbc:mysql://MYSQL_ADDRESS:3306/developer?useUnicode=true&characterEncoding=utf8&failOverReadOnly=false&useSSL=false
write.jdbc.user=USER
write.jdbc.password=PASSWORD
write.jdbc.initialSize=1
write.jdbc.maxActive=80
write.jdbc.minIdle=10
write.jdbc.maxWait=60000
write.jdbc.testOnBorrow=false
write.jdbc.testOnReturn=false
write.jdbc.testWhileIdle=true
write.jdbc.timeBetweenEvictionRunsMillis=60000
write.jdbc.minEvictableIdleTimeMillis=25200000
write.jdbc.removeAbandoned=true
write.jdbc.removeAbandonedTimeout=1800
write.jdbc.logAbandoned=true
write.jdbc.filters=mergeStat

#读库配置
read.jdbc.initialSize=1
read.jdbc.maxActive=80
read.jdbc.minIdle=10
read.jdbc.maxWait=60000
read.jdbc.testOnBorrow=false
read.jdbc.testOnReturn=false
read.jdbc.testWhileIdle=true
read.jdbc.timeBetweenEvictionRunsMillis=60000
read.jdbc.minEvictableIdleTimeMillis=25200000
read.jdbc.removeAbandoned=true
read.jdbc.removeAbandonedTimeout=1800
read.jdbc.logAbandoned=true
read.jdbc.filters=mergeStat

#读库链接配置，多个从库用 | 分隔
read.jdbc.url=jdbc:mysql://MYSQL_READ_ADDRESS1:3306/developer?useUnicode=true&characterEncoding=utf8&useSSL=false|jdbc:mysql://MYSQL_READ_ADDRESS2:3306/developer?useUnicode=true&characterEncoding=utf8&useSSL=false
read.jdbc.user=USER1|USER2
read.jdbc.password=PASSWORD1|PASSWORD2
```
 
```
@EnableNacosMybatis(dataId = "DATA_ID", group = "GROUP_NAME" ...另外的一些配置项如下)
/**
 * 如： classpath:mybatis/**/*Mapper.xml
 * 扫描mybatis/**/*Mapper.xml下xml 文件
 */
String daoXmlPath() default "classpath:mybatis/**/*Mapper.xml";

/**
 * 如： com.company.*.dao
 * 扫描com.**.dao下 xxMapper.java 文件
 */
String[] daoBasePackage() default {"com.**.dao"};

/**
 * 所有service包下的类的所有方法
 * com..*.service..*.*(..)： 在 com..**.service包下面的所有方法将开启事务切面。事务的规则见：transactionRequiredNameMap
 */
String transactionPointcut() default "execution(* com..*.service..*.*(..))";

/**
 * 在开启事务的方法上，如果是以下面方法名开始的就开启写事务，否则设置 readOnly
 * 另外第一个进入切面也决定了走主库，还是走从库。
 */
String[] transactionRequiredNameMap() default {
        "add*", "edit*", "remove*", "insert*", "save*", "update*", "modify*", "delete*", "do*", "process*", "on*", "create*"
};

/**
 * 数据库类型: mysql, mariadb, oracle, db2, h2, hsql, sqlite, postgresql, sqlserver2005, sqlserver, dm
 */
String dbType() default "mysql";
```

读写分离和事务实例：
```
假设配置：
@EnableNacosMybatis(dataId = "DATA_ID", group = "GROUP_NAME",
        // com.domain.project.service包下面的所以方法开启事务
        transactionPointcut="com.domain.project.service..*.*(..))",
        // update和delete开头的方法开启写事务，其他都是读事务
        transactionRequiredNameMap={"update*", "delete*"})
 

package com.domain.project.controller;

public class XXController {
  @Autowired
  private BarService barService;
  
  @PostMapping("xxx")
  public void updateFoo() {
      // 调用此方法，开启新的可写事务，并且走主库，并且在此方法里面所有操作都是走主库。
      barService.updateBar();
      // 写事务提交
      
      // 调用此方法时，开启新的读事务，并且走从库，并且在此方法里面所有操作都是走从库。
      barService.getBar();
      // 事务完毕
  }
  
  @PostMapping("yyy")
  public void updateFoo() {
      // 调用此方法时，开启新的读事务，并且走从库，并且在此方法里面所有操作都是走从库。
      barService.getBar();
      // 事务完毕
  }
}



package com.domain.project.service;

public class FooService {
  @Autowired
  private BarService barService;
  
  public void updateFoo() {
      // 事务传播到updateBar中
      barService.updateBar();
      
      // 事务传播到updateBar中，但此方法里面只能执行读操作
      barService.getBar();
  }
}

package com.domain.project.service;

public class BarService {
  
  public void updateBar() {
     // 这个方法里面可以对数据库有写操作，但前提是： 前置方法调用的时候已经开启了可写事务，并且走主库。
  }
  
  public void getBar() {
    // 无论什么情况下，这个方法里面如果对数据库有写操作，则会报错。
  }
}
```

### BaseMapper 的分页查询：

继承于 https://mybatis.plus/， 也支持其原生的一些写法，但这里有比较强大的封装，<a href="https://github.com/gomcarter/developer/blob/master/frameworks/mybatis-starter/src/main/java/com/gomcarter/frameworks/mybatis/mapper/BaseMapper.java">见源码</a>

```
实例：
import com.yiayoframework.mybatis.pager.DefaultPager;

@RestController
@RequestMapping("publics/test")
public class PublicsCategoryController {

    @Autowired
    private FooService fooService;

    @GetMapping(value = "query", name = "分页查询")
    List<Foo> query(FooParam params, DefaultPager pager) {
        return fooService.query(params, pager);
    }
}

//////////////////////////////////////////////////////////////
import com.yiayoframework.mybatis.pager.Pageable;

@Service
public class FooService {
    @Autowired
    private FooMapper fooMapper;

    public <P> List<Foo> query(P params, Pageable pager) {
        return fooMapper.query(params, pager);
    }
}

//////////////////////////////////////////////////////////////
import com.yiayoframework.mybatis.mapper.BaseMapper;

public interface FooMapper extends BaseMapper<Foo> {
  // empty
}

//////////////////////////////////////////////////////////////
import com.yiayoframework.mybatis.annotation.Condition;
import com.yiayoframework.mybatis.annotation.MatchType;

public class FooParam {

    // 什么都不写，主要这个值不为空，则默认表示： where id = id
    private Long id;

    // 表示： where name like %name%
    @Condition(type = MatchType.LIKE)
    private String name;

    //field 表示数据库字段， 此项表示：where sort > sortGT
    @Condition(field = "sort", type = MatchType.GT)
    private Integer sortGT;

    // where sort < sortLT
    @Condition(field = "sort", type = MatchType.LT)
    private Integer sortLT;

    // where state = state
    private Boolean state;

    // where url LIKE "%url"
    @Condition(type = MatchType.LEFTLIKE)
    private String url;

    // where url LIKE "url%"
    @Condition(type = MatchType.RIGHTLIKE)
    private String code;

    // where is_leaf <> isLeaf     自动驼峰转下划线
    @Condition(type = MatchType.NE)
    private Boolean isLeaf;

    // where brokerage is null
    @Condition(type = MatchType.NULL)
    private Boolean brokerage;
    
    // where brokerage is not null
    @Condition(field = "brokerage", type = MatchType.NOTNULL)
    private Boolean brokerageNotNull;
    
    // where fk_foo_id in (fkFooId)
    @Condition(type = MatchType.IN)
    private Long fkFooId;

    // 对于 Iterable和 array 什么都不写默认是 IN   where fk_foo_id in (fkFooId.get(0), fkFooId.get(1) ... ) 
    private List<Long> fkFooId;
    // where fk_foo_id in (fkFooId[0], fkFooId[1] ... ) 
    private Long[] fkFooId;
    
    // where fk_foo_id not in (fkFooId[0], fkFooId[1] ... ) 
    @Condition(field = "fk_foo_id", type = MatchType.NOTIN)
    private Long[] fkFooId;

    // where ( fk_foo_id in (fooIdListOr) ) or (other condition)
    @Condition(field = "fk_foo_id", type = MatchType.OR)
    private List<Long> fooIdListOr;

    // where (XX = XX AND YY = YY OR ZZ = ZZ) or (other condition) 
    // XX,YY,ZZ 是FooChildrenParams类里面的字段
    @Condition(type = MatchType.OR)
    private FooChildrenParams children;
    
    // where (XX = XX AND YY = YY OR ZZ = ZZ) and (other condition) 
    // XX,YY,ZZ 是FooChildrenParams类里面的字段
    @Condition(type = MatchType.AND)
    private FooChildrenParams children;
```

### 代码自动生成：
```
public class Generator {

    public static void main(String[] args) {
    
        // 项目绝对路径，如果不填写则为当前项目地址 classpath
        CodeGenerator.PROJECT_ABSOLUTE_DIR = null;
        // CodeGenerator.PROJECT_ABSOLUTE_DIR = "D://prjects/foo";
        
        // 生成代码输出的初始包路径(自身项目包路径)
        CodeGenerator.BASE_PACKAGE = "com.gomcarter.test";

        // 生成代码输出的初始包路径下的子目录，自定义
        // 如：abc, 那么文件就输出到  com.gomcarter.test.abc下
        CodeGenerator.SUB_PACKAGE = "";
        
        // 数据库连接地址
        CodeGenerator.DB_URL = "jdbc:mysql://127.0.0.1:3306/test?useSSL=true";

        // 数据库用户
        CodeGenerator.DB_USERNAME = "root";
        
        // 数据库密码
        CodeGenerator.DB_PASSWORD = "123456";

        // 表名， 为空会则生成整个数据库的所有表
        CodeGenerator.TOBE_GENERATE_TABLES = "test";
         
        // 作者信息
        CodeGenerator.AUTHOR = "gomcarter";

        // 执行生成代码， 不出意外就会生成  TestMapper.xml, TestMapper.java, TestService.java, Test.java, TestParam.java, TestDto.java, TestController.java
        CodeGenerator.main(null);
    }
}
```



    

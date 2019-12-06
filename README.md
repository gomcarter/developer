# frameworks #
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/base/README.md" >主框架功能</a>
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/dubbo-starter/README.md" >dubbo-starter</a>
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/http-api-starter/README.md" >http-api-starter</a>
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/interfaces-starter/README.md" >interfaces-starter</a>
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/mybatis-starter/README.md" >mybatis-starter</a>
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/redis-starter/README.md" >redis-starter</a>
### <a href="https://github.com/gomcarter/developer/blob/master/frameworks/xmlexcel/README.md" >xmlexcel</a>
# developer #
### 一、快速开始：

1，安装部署 nacos

参考：https://nacos.io/zh-cn/docs/what-is-nacos.html

安装完成之后，假设 nacos 地址为：  http://nacos.server:8848


有两种方式，让开发者中心连接到 nacos：
a，设置环境变量：
`// 设置 namespace，非必填项，如果不填则默认访问public命名空间`

`export NACOS_NAMESPACE=e9cc9114-647c-46c2-bc61-5dfaafe77a8f`

`// 设置 nacos 服务地址`

`export NACOS_SERVER_ADDR=http://nacos.server:8848`

b，启动开发者中心设置系统参数，如：

`java -jar -Dnacos.namespace=e9cc9114-647c-46c2-bc61-5dfaafe77a8f -Dnacos.server.addr=http://nacos.server:8848  developer-1.0.0.jar &`

如果不设置 namespace，则：

`java -jar -Dnacos.server.addr=http://nacos.server:8848  developer-1.0.0.jar &`


**2，导入mysql表结构**

sql：<a href="https://github.com/gomcarter/developer/blob/master/developer/developer.sql" target="_blank">developer.sql</a>


**3，配置数据库连接**
在 nacos 中配置如下：

```dataId： DEVELOPER```

```group：  MYSQL```

```content：```

```
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

#读库链接配置
read.jdbc.url=jdbc:mysql://MYSQL_ADDRESS:3306/developer?useUnicode=true&characterEncoding=utf8&useSSL=false
read.jdbc.user=USER
read.jdbc.password=PASSWORD
```

**4，启动服务**

```java -jar developer-1.0.0.jar &```

日志文件在：

/home/logs/developer/目录下；

**5，使用**
在你的其他项目里面引入依赖：

```
<dependency>
    <groupId>com.gomcarter.frameworks</groupId>
    <artifactId>interfaces-starter</artifactId>
    <version>${frameworks.version}</version>
</dependency>
```
见：<a href="https://mvnrepository.com/artifact/com.gomcarter.frameworks/interfaces-starter" target="_blank">mvn 中央仓库地址</a>

使用规范：
```
1，@GetMapping(value = "list",name = "接口名称")

   @RequestMapping(value = "list",name = "接口名称，如果名称为空，则此接口不会存入接口中心")
   
2， 接口、参数、返回值使用com.gomcarter.frameworks.interfaces.annotation.Notes，如下：

  @Notes("this interface for user login")
  @GetMapping(value = "list",name = "接口名称")
  public ReturnDto list(@Notes("id") Long id, Params params) {
  }
  
  class Params {
      @Notes("xxxx")
      private String name;
      
      @Notes("yyyy")
      private List<ParamsAA> list;
  }
  
  class ReturnDto {
      @Notes("xxxx")
      private String name;
      
      @Notes("yyyy")
      private List<ParamsAA> list;
  }
```


配置发者中心：

打开开发者中心：http://developer.server:port


a，配置前端系统，需要根据接口的第一个目录开区分给哪个端提供的接口，如

http://xserver.com/end1/xxx

http://xserver.com/end2/yyy

http://xserver.com/end3/zzz

end1,end2,end3用来区分哪个端提供的接口，所以对接口的设计有要求， 如图：

![image](https://upload-images.jianshu.io/upload_images/19189438-aeda91cbb1640585.png)

b，配置模块

模块的意思其实就是 java 项目，配置这个 java 项目的信息
![image](https://upload-images.jianshu.io/upload_images/19189438-58f2e07a1a67112f.png)

c，导入接口
![image](https://upload-images.jianshu.io/upload_images/19189438-467c8a1b093bec82.png)

选择上一步配置的 java 项目即可生成接口
![image](https://upload-images.jianshu.io/upload_images/19189438-852be8a8a2fc375a.png)

d，接口详情
![image](https://upload-images.jianshu.io/upload_images/19189438-d53d8b03f5eedba4.png)
![image](https://upload-images.jianshu.io/upload_images/19189438-8d3fcf8bf096b014.png)

e,接口测试
![image](https://upload-images.jianshu.io/upload_images/19189438-43d9d3c47fc2e755.png)

### 二、帮助

如需帮助请联系：gomcarter@vip.qq.com

或者 qq： 506598720

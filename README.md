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

sql：<a href="https://github.com/gomcarter/developer/blob/master/java/developer.sql" target="_blank">developer.sql</a>


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
启动服务：
```
java -jar -Xms1g -Xmx2g -Dserver.port=自己服务端口 -Dinterfaces.domain=接口中心地址 -Dinterfaces.javaId=下面配置的java模块id xx-project.jar

-Dinterfaces.domain：接口中心地址
-Dinterfaces.javaId：见下面配置java模块
```

配置发者中心：

打开开发者中心：http://developer.server:port


a，配置前端系统，需要根据接口的第一个目录开区分给哪个端提供的接口，如

http://xserver.com/endpoint1/xxx

http://xserver.com/endpoint2/yyy

http://xserver.com/endpoint3/zzz

endpoint1,endpoint2,endpoint3用来区分哪个端提供的接口，所以对接口的设计有要求， 如图：

![image](https://upload-images.jianshu.io/upload_images/19189438-aeda91cbb1640585.png)

b，配置java模块：意思其实就是 java 项目，配置这个 java 项目的信息

![image](https://upload-images.jianshu.io/upload_images/19189438-58f2e07a1a67112f.png)

c，接口详情
![image](https://upload-images.jianshu.io/upload_images/19189438-d53d8b03f5eedba4.png)
![image](https://upload-images.jianshu.io/upload_images/19189438-8d3fcf8bf096b014.png)

d，接口测试
![image](https://upload-images.jianshu.io/upload_images/19189438-43d9d3c47fc2e755.png)

### 二、接口自动化测试
a，配置用例
![image](https://upload-images.jianshu.io/upload_images/19189438-906197ada8af7a95.png)
![image](https://upload-images.jianshu.io/upload_images/19189438-37eb222b999cf3f8.png)

b，执行用例
![image](https://upload-images.jianshu.io/upload_images/19189438-6b662ff6191c4588.png)

##### 注：要使用接口自动化测试，接口需要支持跨域访问（可以在测试环境开启）：
```
    String o = request.getHeader("Origin");
    // 允许跨域访问
    response.setHeader("Access-Control-Allow-Origin", o);
    // 跨域带cookie
    response.setHeader("Access-Control-Allow-Credentials", "true");
    // 支持的http method
    response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE");
    // 支持http请求携带的header头
    response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, 其他支持携带的header");
```

### 三、自行编译打包
考虑到可能适用其他配置中心，如apollo，diamond，或者不使用，下面将自行打包
```
具体操作如下（以本地模式示例）：
1，将pom.xml中 
    <artifactId>config-center-nacos</artifactId> 修改为：
    <artifactId>config-center-local</artifactId>  或
    <artifactId>config-center-apollo</artifactId>  或
    <artifactId>config-center-diamond</artifactId>
    当然如果还有其他的配置中心，可自行实现，参照：https://github.com/gomcarter/frameworks/tree/master/config-center-nacos
    
2，建一个database.properties将上面（二中第3部：配置数据库连接）内容存放于此，假设此文件就放在resource下
3，修改Application.java文件，@EnableMybatis({"DEVELOPER", "MYSQL"}) 修改为 @EnableMybatis("database.properties")
4，编译打包即可
```

### 四、帮助

如需帮助请联系：gomcarter@vip.qq.com

或者 qq： 506598720

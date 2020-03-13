# developer #
### 一、快速开始：

**1，部署配置中心（非必须）**

nacos（默认）：

    参考：https://nacos.io/zh-cn/docs/what-is-nacos.html

    安装完成之后，假设 nacos 地址为：  http://nacos.server:8848   有两种方式，让开发者中心连接到 nacos：

    a，设置环境变量：
        // 设置 nacos 服务地址
        export NACOS_SERVER_ADDR=http://nacos.server:8848

    b，设置系统参数：
        java -jar -Dnacos.server.addr=http://nacos.server:8848  developer-1.0.0.jar &

其他配置中心（需自行下载源码打包）：

    考虑到可能适用其他配置中心，如apollo，diamond，或者使用本地properties配置，可通过下面方式下载源码自行打包
    只需下载java目录下的代码即可：https://github.com/gomcarter/developer/tree/master/java
    
    具体操作如下（以本地模式示例）：
    1，将pom.xml中 
        <artifactId>config-center-nacos</artifactId> 修改为：
        <artifactId>config-center-local</artifactId> —— 本地配置模式
        <artifactId>config-center-apollo</artifactId> —— apollo
        <artifactId>config-center-diamond</artifactId> —— diamond
        当然如果还有其他的配置中心，可自行实现，参照：https://github.com/gomcarter/frameworks/tree/master/config-center-nacos

    2，建一个database.properties将上面（下面第3步：配置数据库连接）内容存放于此，假设此文件就放在resource下
    3，修改Application.java文件，@EnableMybatis({"DEVELOPER", "MYSQL"}) 修改为 @EnableMybatis("database.properties")
    4，编译打包即可


**2，导入mysql表结构**

需要mysql5.7版本及以上，SQL文件：<a href="https://github.com/gomcarter/developer/blob/master/java/developer.sql" target="_blank">戳这里</a>


**3，配置数据库连接**

nacos 的 public 中配置：

dataId： DEVELOPER

group：  MYSQL

content内容：

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

    #读库链接配置，如果需读写分离，下面可设置为读库链接，不需要就设置和主库一样
    read.jdbc.url=jdbc:mysql://MYSQL_ADDRESS:3306/developer?useUnicode=true&characterEncoding=utf8&useSSL=false
    read.jdbc.user=USER
    read.jdbc.password=PASSWORD


**4，启动服务**

下载jar包：https://github.com/gomcarter/developer/releases

    java -jar developer-${version}.jar &

    日志文件在：${jar包所在目录}/logs/developer/developer.log


**5，使用**

其他项目接入使用参考：<a href="https://github.com/gomcarter/frameworks/blob/master/interfaces-starter/README.md" target="_blank">戳这里</a>


**6，配置发者中心**

打开开发者中心：http://developer.server:port

默认登录账号/密码：  admin/admin

a，配置前端系统，需要根据接口的第一个目录开区分给哪个端提供的接口，如

http://xserver.com/endpoint1/xxx ，  http://xserver.com/endpoint2/yyy ， http://xserver.com/endpoint3/zzz

endpoint1,endpoint2,endpoint3用来区分哪个端提供的接口，所以对接口的设计有要求， 如图：

![image](https://user-images.githubusercontent.com/16378826/75749398-aed83200-5d5c-11ea-9991-539029c7edff.png)

b，配置java模块：意思其实就是 java 项目，配置这个 java 项目的信息

![image](https://user-images.githubusercontent.com/16378826/75749379-a3850680-5d5c-11ea-810e-635d020af72c.png)
    
c，接口详情

![image](https://user-images.githubusercontent.com/16378826/75749340-8cdeaf80-5d5c-11ea-91af-3de2f321ced0.png)
![image](https://user-images.githubusercontent.com/16378826/75749320-7e909380-5d5c-11ea-9aca-6fe515578f10.png)

d，接口测试

![image](https://user-images.githubusercontent.com/16378826/75749279-6c165a00-5d5c-11ea-9ca2-f2d9f609f617.png)


### 二、接口自动化测试
**1，配置用例**

![image](https://user-images.githubusercontent.com/16378826/75749177-2ce80900-5d5c-11ea-8368-0f03ce8c7efc.png)
![image](https://user-images.githubusercontent.com/16378826/75749238-530da900-5d5c-11ea-8b4d-f505f23930d2.png)

**2，执行用例**

![image](https://user-images.githubusercontent.com/16378826/75000401-312d4000-5499-11ea-8438-0282a96bc689.png)

##### 注：要使用接口自动化测试，接口需要支持跨域访问（可以在测试环境开启）
    String o = request.getHeader("Origin");
    // 允许跨域访问
    response.setHeader("Access-Control-Allow-Origin", o);
    // 跨域带cookie
    response.setHeader("Access-Control-Allow-Credentials", "true");
    // 支持的http method
    response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE");
    // 支持http请求携带的header头
    response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, 其他支持携带的header");

    注：当然在nginx配置跨域访问也可以

### 三、用户管理
**1，自带用户管理**

![image](https://user-images.githubusercontent.com/16378826/75749078-e98d9a80-5d5b-11ea-9ad7-61a6e2416e3d.png)

**2，连接外部登录**

![image](https://user-images.githubusercontent.com/16378826/75749118-03c77880-5d5c-11ea-8a62-b65fb3cda07f.png)


### 四、mock

自动生成mock地址，调用mock地址将返回mock数据

![image](https://user-images.githubusercontent.com/16378826/75968793-eb489100-5f08-11ea-9f20-98521b22af50.png)


### 五、帮助

如需帮助请联系：gomcarter@vip.qq.com
或者 qq： 506598720

欢迎提issue和需求！

# developer #
### 写在前面

本工具是在spring（java 1.8）系列下，自动拉取接口并生成接口文档，在此基础之上构建了单接口测试、接口流程自动化测试、mock数据自动生成。
国内用户可访问：https://gitee.com/gomcarter/developer


### 一、快速开始：

下载安装压缩包： <a href="https://github.com/gomcarter/developer/releases/download/1.0.6/developer.zip" target="_blank">戳这里</a>

**1，导入mysql表结构**

需要mysql5.7版本及以上，SQL文件：<a href="https://github.com/gomcarter/developer/blob/master/java/developer.sql" target="_blank">戳这里</a>


**2，配置数据库连接**

修改database.properties文件:

    #主库配置
    write.jdbc.url=jdbc:mysql://127.0.0.1:3306/developer?useUnicode=true&characterEncoding=utf8&failOverReadOnly=false&useSSL=false
    write.jdbc.user=root
    write.jdbc.password=root123

**3，启动服务**
    
如需更换端口，请自行修改
    
    sh startup.sh


**4，使用**

其他项目接入使用参考：<a href="https://github.com/gomcarter/frameworks/blob/master/interfaces-starter/README.md" target="_blank">戳这里</a>


**5，配置发者中心**

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

加Q群 ： 783287193

欢迎提issue和需求！

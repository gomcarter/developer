## 写在前面

本工具是在spring（java 1.8）系列下，自动拉取接口并生成接口文档，在此基础之上构建了单接口测试、接口流程自动化测试、mock数据自动生成。
国内用户可访问：https://gitee.com/gomcarter/developer

## 简介
![image](https://user-images.githubusercontent.com/16378826/98345026-0f87df00-204f-11eb-9bd0-9b04540af6f7.png)

启动某个spring服务端项目，将该项目的接口发送到developer中进行管理，在developer系统中提供各种关于接口的功能。

## 工作原理
1，通过RequestMappingHandlerMapping获取到所有接口列表；

2，通过获取到的接口解析该接口的参数、返回值等数据结构；

3，最后将接口相关数据结构通过http发送到developer系统；

## 使用步骤

### 1，<a href="https://github.com/gomcarter/developer/wiki/1%EF%BC%8C%E5%AE%89%E8%A3%85%E9%83%A8%E7%BD%B2">安装部署</a>

### 2，<a href="https://github.com/gomcarter/developer/wiki/2%EF%BC%8C%E6%8E%A5%E5%8F%A3%E7%AE%A1%E7%90%86">接口管理</a>

### 3，<a href="https://github.com/gomcarter/developer/wiki/3%EF%BC%8C%E6%8E%A5%E5%8F%A3%E6%89%93%E5%8C%85%E7%AE%A1%E7%90%86">接口打包管理</a>

### 4，<a href="https://github.com/gomcarter/developer/wiki/4%EF%BC%8C%E6%8E%A5%E5%8F%A3mock">接口mock</a>

### 5，<a href="https://github.com/gomcarter/developer/wiki/5%EF%BC%8C%E5%8D%95%E6%8E%A5%E5%8F%A3%E6%B5%8B%E8%AF%95">单接口测试</a>

### 6，<a href="https://github.com/gomcarter/developer/wiki/6%EF%BC%8C%E6%8E%A5%E5%8F%A3%E8%87%AA%E5%8A%A8%E5%8C%96%E6%B5%8B%E8%AF%95">接口自动化测试</a>

### 7，<a href="https://github.com/gomcarter/developer/wiki/7%EF%BC%8C%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%8E%A5%E5%8F%A3%E8%87%AA%E5%8A%A8%E8%81%9A%E5%90%88">微服务接口自动聚合</a>

## 五、帮助

如需帮助请联系：gomcarter@vip.qq.com

加Q群 ： 783287193

欢迎提issue和需求！

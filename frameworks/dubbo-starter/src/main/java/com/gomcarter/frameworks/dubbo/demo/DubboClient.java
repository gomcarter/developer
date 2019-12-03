package com.gomcarter.frameworks.dubbo.demo;

import com.gomcarter.frameworks.base.mapper.JsonMapper;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author gomcarter
 */
public class DubboClient {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("dubbo.application.logger", "slf4j");

        ReferenceConfig<DemoApi> reference = new ReferenceConfig<>();
//        RegistryConfig rc = new RegistryConfig("zookeeper://172.18.19.251:2181?backup=172.18.20.6:2181,172.18.19.252:2181");
//        RegistryConfig rc = new RegistryConfig("redis://119.23.240.12:7480");
        RegistryConfig rc = new RegistryConfig("nacos://119.23.240.12:10009");

        rc.setParameters(new HashMap<>());
//        rc.setPassword("3zda3caeyx6pn7c5z");
//        rc.setUsername("anywhocares");

        reference.setApplication(new ApplicationConfig("dubbo-consumer"));
        reference.setRegistry(rc);
        reference.setCheck(false);
        reference.setInterface(DemoApi.class);
        DemoApi service = reference.get();

        DemoDto result = service.getById(1);

        System.out.println("调用结果：" + JsonMapper.buildNonNullMapper().toJson(result));

        new CountDownLatch(1).await();
    }
}

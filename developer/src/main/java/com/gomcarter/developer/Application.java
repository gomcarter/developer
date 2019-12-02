package com.gomcarter.developer;

import com.gomcarter.frameworks.base.spring.RequestMappingHandlerAdapterModify;
import com.gomcarter.frameworks.mybatis.annotation.EnableDiamondMybatis;
import com.gomcarter.frameworks.redis.annotation.EnableDiamondRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 程序主入口
 */

@SpringBootApplication
@EnableDiamondMybatis("DEVELOPER")
@EnableDiamondRedis
public class Application {

    @Bean
    public RequestMappingHandlerAdapterModify requestMappingHandlerAdapterModify() {
        return new RequestMappingHandlerAdapterModify();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//            String[] beanNames = ctx.getBeanDefinitionNames();
//
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName/* + ":" + ctx.getBean(beanName).getClass()*/);
//            }
//        };
//    }

}

package com.gomcarter.developer;

import com.gomcarter.frameworks.base.spring.RequestMappingHandlerAdapterModify;
import com.gomcarter.frameworks.mybatis.annotation.EnableMybatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 程序主入口
 *
 * @author gomcarter
 */
@SpringBootApplication
@EnableMybatis({"DEVELOPER", "MYSQL"})
public class Application {

    /**
     * 返回结果自动 wrap
     *
     * @return RequestMappingHandlerAdapterModify
     */
    @Bean
    public RequestMappingHandlerAdapterModify requestMappingHandlerAdapterModify() {
        return new RequestMappingHandlerAdapterModify();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

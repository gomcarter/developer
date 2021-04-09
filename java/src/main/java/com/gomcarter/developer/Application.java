package com.gomcarter.developer;

import com.gomcarter.frameworks.mybatis.annotation.EnableMybatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序主入口
 *
 * @author gomcarter
 */
@SpringBootApplication
@EnableMybatis("./database.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package com.gomcarter.developer.configuration;

import com.gomcarter.developer.interceptors.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author gomcarter on 2018年3月29日 16:29:58
 */
@Configuration
public class Interceptors implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加认证拦截器
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/developer/**");

        //添加更多的拦截器
        //...
    }
}

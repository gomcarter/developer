package com.gomcarter.developer.aop;

import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.frameworks.base.annotation.IgnoreLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商户平台拦截器配置
 *
 * @author 李银 on 2018年3月29日 16:29:58
 */
@Configuration
public class UserInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        /*复杂请求的探针，这里需要直接通过*/
        if (StringUtils.equalsIgnoreCase(RequestMethod.OPTIONS.name(), request.getMethod())) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        IgnoreLogin il = handlerMethod.getMethod().getAnnotation(IgnoreLogin.class);
        if (il != null) {
            return true;
        }

        // 解析账号信息，存到UserHolder中
        UserHolder.auth(request);

        return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        UserHolder.reset();
    }
}

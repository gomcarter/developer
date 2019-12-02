package com.gomcarter.frameworks.httpapi.aop;

import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.httpapi.annotation.NoCheckToken;
import com.gomcarter.frameworks.httpapi.utils.ApiTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckTokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        /*复杂请求的探针，这里需要直接通过*/
        if (StringUtils.equalsIgnoreCase(RequestMethod.OPTIONS.name(), request.getMethod())) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NoCheckToken il = handlerMethod.getMethod().getAnnotation(NoCheckToken.class);
        if (il == null && !ApiTokenUtils.validate(request)) {
            throw new NoPermissionException();
        }

        /*记录访问此接口的信息*/
        /*logger.info(request.getRequestURI() + "【" + 1 + "】：" + JsonMapper.buildNonNullMapper().toJson(request.getParameterMap()));*/
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
    }
}

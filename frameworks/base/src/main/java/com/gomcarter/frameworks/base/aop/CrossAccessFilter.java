package com.gomcarter.frameworks.base.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gomcarter
 */
public class CrossAccessFilter extends OncePerRequestFilter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void crossAllows(HttpServletRequest request, HttpServletResponse response) {

        String o = request.getHeader("Origin");
        /* 允许跨域 */
        response.setHeader("Access-Control-Allow-Origin", o);
        /* 允许带cookie */
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /* 允许请求的方式 */
        response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE");
        /* 支持带头,  header名不能用下划线(_), 会接受不到 */
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, backToken");
        /* 预检有效期 */
        response.addHeader("Access-Control-Max-Age", "172800");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        crossAllows(request, response);
        chain.doFilter(request, response);
    }
}

package com.gomcarter.frameworks.base.controller;

import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.json.ErrorCode;
import com.gomcarter.frameworks.base.json.JsonError;
import com.gomcarter.frameworks.base.json.JsonObject;
import com.gomcarter.frameworks.base.mapper.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.ReflectionException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gomcarter  on 2019-11-11 23:17:48
 */
@ControllerAdvice
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param request   HttpServletRequest
     * @param exception what exception happened in your code
     * @return result for our user as some friendly notice
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public JsonObject exceptionHandler(HttpServletRequest request, Exception exception) {
        //如果是ClientAbortException, 直接返回null;
        if (StringUtils.contains(exception.getClass().toString(), "ClientAbortException")) {
            return null;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headerMap.put(header, request.getHeader(header));
        }

        logger.error("{}操作失败, url:{}, method:{}, ip: {}, Referer: {}, UA: {}, params: {}, cookie: {},header: {},",
                this.getClass().getName(),
                request.getRequestURI(),
                request.getMethod(),
                getIp(request),
                request.getHeader("Referer"),
                request.getHeader("User-Agent"),
                JsonMapper.buildNotNullMapper().toJson(request.getParameterMap()),
                JsonMapper.buildNotNullMapper().toJson(request.getCookies()),
                JsonMapper.buildNotNullMapper().toJson(headerMap),
                exception);

        if (exception instanceof MissingServletRequestParameterException) {
            return new JsonError(ErrorCode.paramError);
        }

        if (exception instanceof NullPointerException) {
            return new JsonError(ErrorCode.nullPointer);
        }

        if (exception instanceof SQLException) {
            return new JsonError(ErrorCode.sqlError);
        }

        if (exception instanceof ReflectionException) {
            return new JsonError(ErrorCode.sqlError);
        }

        if (exception instanceof RuntimeException) {
            if (exception instanceof CustomException) {
                return new JsonError(exception.getMessage(), ((CustomException) exception).getCode());
            } else {
                return new JsonError(exception.getMessage());
            }
        }

        JsonObject jsonError = addMoreExceptionHandler(request, exception);
        if (jsonError != null) {
            return jsonError;
        }

        return new JsonError("请求失败！");
    }

    protected JsonObject addMoreExceptionHandler(HttpServletRequest request, Exception exception) {
        return null;
    }

    /**
     * get visitor's ip
     *
     * @param request HttpServletRequest
     * @return ip such as "119.22.11.2"
     */
    public String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (request.getHeader("X-Forwarded-For") != null) {
            ip = request.getHeader("X-Forwarded-For");
        } else if (request.getHeader("X-Real-IP") != null) {
            ip = request.getHeader("X-Real-IP");
        }
        return ip;
    }

    /**
     * spring InitBinder
     *
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // binder.registerCustomEditor(String.class, new StringEscapeEditor());
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    /**
     * read request body
     *
     * @param request HttpServletRequest
     * @return body content
     * @throws IOException for read failed
     */
    public static String readBody(HttpServletRequest request) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}

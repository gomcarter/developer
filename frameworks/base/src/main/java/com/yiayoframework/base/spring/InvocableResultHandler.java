package com.yiayoframework.base.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

public class InvocableResultHandler {

    private Object bean;
    private Method method;

    public final static InvocableResultHandler nullMethod = new InvocableResultHandler();

    private InvocableResultHandler() {

    }

    public InvocableResultHandler(Object bean, Method method) {
        ReflectionUtils.makeAccessible(method);
        this.bean = bean;
        this.method = method;
    }

    public Object invoke(Object... params)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        return method.invoke(bean, params);
    }
}

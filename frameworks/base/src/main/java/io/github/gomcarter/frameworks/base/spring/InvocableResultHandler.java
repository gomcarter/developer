package io.github.gomcarter.frameworks.base.spring;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

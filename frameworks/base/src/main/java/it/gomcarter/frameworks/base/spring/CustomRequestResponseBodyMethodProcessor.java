package it.gomcarter.frameworks.base.spring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.gomcarter.frameworks.base.json.JsonData;
import it.gomcarter.frameworks.base.json.JsonObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

    private BeanFactory beanFactory;

    private final static InvocableResultHandler nullHandler = InvocableResultHandler.nullMethod;

    private final static Map<Class<?>, InvocableResultHandler> resultHandleMethod = new ConcurrentHashMap<Class<?>, InvocableResultHandler>();

    public CustomRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    public CustomRequestResponseBodyMethodProcessor(
            List<HttpMessageConverter<?>> messageConverters,
            ContentNegotiationManager contentNegotiationManager, BeanFactory beanFactory) {
        super(messageConverters, contentNegotiationManager);
        if (beanFactory == null) {
            throw new IllegalArgumentException("must init with a beanFactory");
        }
        this.beanFactory = beanFactory;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException {

        mavContainer.setRequestHandled(true);

        if (!jsonIgnore(returnType)) {
            Object result = wrapReturnValue(returnType, returnValue);
            if (result != null) {
                writeWithMessageConverters(result, returnType, webRequest);
            }
        } else if (returnValue != null) {
            writeWithMessageConverters(returnValue, returnType, webRequest);
        }
    }

    private Object wrapReturnValue(MethodParameter returnType, Object returnValue) {

        InvocableResultHandler handler = getHandler(returnType);

        if (handler == nullHandler) {
            return doDefaultWrap(returnValue);
        }
        try {
            return handler.invoke(returnType, returnValue);
        } catch (Exception e) {
            return returnValue;
        }
    }

    private Object doDefaultWrap(Object returnValue) {
        if (returnValue == null) {
            return new JsonData();
        }
        if (isWrapped(returnValue)) {
            return returnValue;
        } else {
            return new JsonData(returnValue);
        }
    }

    private boolean isWrapped(Object returnValue) {
        return (returnValue instanceof JsonObject) || isJsonObject(returnValue);

    }

    private boolean isJsonObject(Object returnValue) {
        Class<?> clz = returnValue.getClass();
        while (clz != null) {
            if (JsonObject.class == clz) {
                return true;
            }
            clz = clz.getSuperclass();
        }
        return false;
    }

    private InvocableResultHandler getHandler(MethodParameter returnType) {

        Class<?> clz = returnType.getDeclaringClass();

        InvocableResultHandler handler = resultHandleMethod.get(clz);
        if (handler != null) {
            return handler;
        }
        Object bean = beanFactory.getBean(clz);
        if (bean == null) {
            return resolveDefault(clz);
        }
        for (Method method : clz.getMethods()) {
            if (AnnotationUtils.findAnnotation(method, CustomResultHandler.class) != null) {
                InvocableResultHandler _handler =
                        new InvocableResultHandler(bean, BridgeMethodResolver.findBridgedMethod(method));
                resultHandleMethod.put(clz, _handler);
                return _handler;
            }
        }
        return resolveDefault(clz);
    }

    private InvocableResultHandler resolveDefault(Class<?> clz) {
        resultHandleMethod.put(clz, nullHandler);
        return nullHandler;
    }

    private boolean jsonIgnore(MethodParameter returnType) {
        return returnType.getMethodAnnotation(JsonIgnore.class) != null;
    }
}

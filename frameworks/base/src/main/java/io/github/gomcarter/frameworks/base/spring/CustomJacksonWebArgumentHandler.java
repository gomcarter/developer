package io.github.gomcarter.frameworks.base.spring;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class CustomJacksonWebArgumentHandler implements WebArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

        if (methodParameter.hasParameterAnnotation(ModelAttribute.class)) {
            return WebArgumentResolver.UNRESOLVED;
        }
        Class<?> clz = methodParameter.getParameterType();
        if (BeanUtils.isSimpleProperty(clz)) {
            return WebArgumentResolver.UNRESOLVED;
        }
        return ModifyUtils.calcValue(methodParameter, webRequest.getParameterValues(methodParameter.getParameterName()));
    }
}

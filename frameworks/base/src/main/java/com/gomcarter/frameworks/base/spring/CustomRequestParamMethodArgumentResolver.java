package com.gomcarter.frameworks.base.spring;

import com.gomcarter.frameworks.base.exception.MissingParameterException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {

    public CustomRequestParamMethodArgumentResolver(boolean useDefaultResolution) {
        super(useDefaultResolution);
    }

    public CustomRequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
        super(beanFactory, useDefaultResolution);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    private void assertIsMultipartRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
            throw new MultipartException("The current request is not a multipart request");
        }
    }

    private Class<?> getCollectionParameterType(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)) {
            Class<?> valueType = GenericTypeResolver.resolveParameterType(parameter, paramType);
            if (valueType != null) {
                return valueType;
            }
        }
        return null;
    }

    private boolean isMultipartFileCollection(MethodParameter parameter) {
        Class<?> collectionType = getCollectionParameterType(parameter);
        return ((collectionType != null) && collectionType.equals(MultipartFile.class));
    }

    private boolean isPartCollection(MethodParameter parameter) {
        Class<?> collectionType = getCollectionParameterType(parameter);
        return ((collectionType != null) && "javax.servlet.http.Part".equals(collectionType.getName()));
    }

    private boolean isPartArray(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType().getComponentType();
        return ((paramType != null) && "javax.servlet.http.Part".equals(paramType.getName()));
    }

    private boolean isMultipartFileArray(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType().getComponentType();
        return ((paramType != null) && MultipartFile.class.equals(paramType));
    }

    private static class RequestPartResolver {
        public static Object resolvePart(HttpServletRequest servletRequest) throws Exception {
            return servletRequest.getParts().toArray(new Part[servletRequest.getParts().size()]);
        }
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {

        Object arg;

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(
                servletRequest, MultipartHttpServletRequest.class);

        if (MultipartFile.class.equals(parameter.getParameterType())) {
            assertIsMultipartRequest(servletRequest);
            Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
            arg = multipartRequest.getFile(name);
        } else if (isMultipartFileCollection(parameter)) {
            assertIsMultipartRequest(servletRequest);
            Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
            arg = multipartRequest.getFiles(name);
        } else if (isMultipartFileArray(parameter)) {
            assertIsMultipartRequest(servletRequest);
            Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
            arg = multipartRequest.getFiles(name).toArray(new MultipartFile[0]);
        } else if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
            assertIsMultipartRequest(servletRequest);
            arg = servletRequest.getPart(name);
        } else if (isPartCollection(parameter)) {
            assertIsMultipartRequest(servletRequest);
            arg = new ArrayList<Object>(servletRequest.getParts());
        } else if (isPartArray(parameter)) {
            assertIsMultipartRequest(servletRequest);
            arg = RequestPartResolver.resolvePart(servletRequest);
        } else {
            arg = null;
            if (multipartRequest != null) {
                List<MultipartFile> files = multipartRequest.getFiles(name);
                if (!files.isEmpty()) {
                    arg = (files.size() == 1 ? files.get(0) : files);
                }
            }
            if (arg == null) {
                String[] paramValues = webRequest.getParameterValues(name);
                if (paramValues != null) {
                    arg = convertObject(parameter, paramValues);
                }
            }
        }
        return arg;
    }

    private Object convertObject(MethodParameter methodParameter, String[] values) {
        Class<?> clz = methodParameter.getParameterType();
        if (BeanUtils.isSimpleProperty(clz)) {
            return values.length == 1 ? values[0] : values;
        }
        return ModifyUtils.calcValue(methodParameter, values);
    }

    @Override
    protected void handleResolvedValue(Object arg,
                                       String name,
                                       MethodParameter parameter,
                                       ModelAndViewContainer mavContainer,
                                       NativeWebRequest webRequest) {
        if (arg != null) {
            return;
        }
        RequestParam rp = parameter.getParameterAnnotation(RequestParam.class);
        if (rp != null && rp.required()) {
            throw new MissingParameterException(name, parameter.getParameterType().getSimpleName());
        }
    }
}

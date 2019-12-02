package com.yiayoframework.base.spring;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.google.common.collect.Lists;

public class RequestMappingHandlerAdapterModify implements BeanFactoryAware, InitializingBean {

    private BeanFactory beanFactory;

    private boolean wrapReturnValue = true;

    private boolean resolverArgument = true;

    public void setResolverArgument(boolean resolverArgument) {
        this.resolverArgument = resolverArgument;
    }

    public void setWrapReturnValue(boolean wrapReturnValue) {
        this.wrapReturnValue = wrapReturnValue;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        RequestMappingHandlerAdapter bean = beanFactory.getBean(RequestMappingHandlerAdapter.class);

        if (bean == null) {
            return;
        }
        if (resolverArgument) {
            this.modifyArgumentResolvers(bean);
        }
        if (wrapReturnValue) {
            this.modifyReturnValueHandlers(bean);
        }
    }

    private void modifyReturnValueHandlers(RequestMappingHandlerAdapter bean) {

        List<HandlerMethodReturnValueHandler> newList = Lists.newArrayList();

        for (HandlerMethodReturnValueHandler handler : bean.getReturnValueHandlers()) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                newList.add(new CustomRequestResponseBodyMethodProcessor(bean.getMessageConverters(),
                        beanFactory.getBean("mvcContentNegotiationManager", ContentNegotiationManager.class),
                        beanFactory));
            } else {
                newList.add(handler);
            }
        }
        bean.setReturnValueHandlers(newList);
    }

    private void modifyArgumentResolvers(RequestMappingHandlerAdapter bean) {

        if (!ConfigurableBeanFactory.class.isAssignableFrom(beanFactory.getClass())) {
            return;
        }
        List<HandlerMethodArgumentResolver> newList = Lists.newArrayList();
        Boolean replace = true;
        for (HandlerMethodArgumentResolver handler : bean.getArgumentResolvers()) {
            if (handler instanceof RequestParamMethodArgumentResolver && replace) {
                newList.add(new CustomRequestParamMethodArgumentResolver((ConfigurableBeanFactory) beanFactory, false));
                replace = false;
            } else {
                newList.add(handler);
            }
        }
        bean.setArgumentResolvers(newList);
    }
}

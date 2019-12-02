package it.gomcarter.frameworks.base.spring;

import it.gomcarter.frameworks.base.common.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author gomcarter on 2019-11-15 17:43:37
 */
public abstract class BeanFieldAndMethodProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public final Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Class clazz = bean.getClass();
        for (Field field : ReflectionUtils.findAllField(clazz)) {
            processField(bean, beanName, field);
        }
        for (Method method : ReflectionUtils.findAllMethod(clazz)) {
            processMethod(bean, beanName, method);
        }
        return bean;
    }

    @Override
    public final Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * subclass should implement this method to process field
     */
    protected abstract void processField(Object bean, String beanName, Field field);

    /**
     * subclass should implement this method to process method
     */
    protected abstract void processMethod(Object bean, String beanName, Method method);


    @Override
    public int getOrder() {
        //make it as late as possible
        return Ordered.LOWEST_PRECEDENCE;
    }
}

package com.gomcarter.frameworks.base.common;

import org.springframework.beans.BeansException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * @param source : 不能为空
     * @param target ： 不能为空
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                Object value = _invokeGetterMethod(source, targetPd.getName());
                if (null == value) {
                    continue;
                }
                // 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                try {
                    writeMethod.invoke(target, value);
                } catch (Exception e) {
                    Object sonFiled = _invokeGetterMethod(target, targetPd.getName());
                    if (null == sonFiled) {
                        Class<?>[] params = writeMethod.getParameterTypes();
//                        sonFiled = ReflectionUtils.newInstance(params[0]);
                        try {
                            copyProperties(value, sonFiled);
                            writeMethod.invoke(target, sonFiled);
                        } catch (Exception e1) {
                            throw new RuntimeException(targetPd.getName()
                                    + "类型不匹配：【" + value.getClass().toString()
                                    + "】=>【" + params[0].toString() + "】");
                        }
                    } else {
                        copyProperties(value, sonFiled);
                    }
                }
            }
        }
    }

    public static <T> T copyObject(Object source, T target) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                Object value = _invokeGetterMethod(source, targetPd.getName());
                if (value == null) {
                    continue;
                }
                // 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                try {
                    writeMethod.invoke(target, value);
                } catch (Exception e) {
                    Object sonFiled = _invokeGetterMethod(target, targetPd.getName());
                    if (sonFiled == null) {
                        try {
                            copyObject(value, sonFiled);
                            writeMethod.invoke(target, sonFiled);
                        } catch (Exception e1) {
                            throw new RuntimeException("copyObject 类型不匹配：" + targetPd.getName());
                        }
                    } else {
                        copyObject(value, sonFiled);
                    }
                }
            }
        }
        return target;
    }

    private static Object _invokeGetterMethod(Object source, String filedName) {
        PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                filedName);// targetPd.getName());
        if (sourcePd != null) {
            try {
                if (sourcePd.getReadMethod() != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass()
                            .getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    return readMethod.invoke(source);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

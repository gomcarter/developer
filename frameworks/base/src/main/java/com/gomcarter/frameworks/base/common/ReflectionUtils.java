/**
 * Copyright (c) 2005-2011 springside.org.cn
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p>
 * $Id: ReflectionUtils.java 1504 2011-03-08 14:49:20Z calvinxiu $
 */
package com.gomcarter.frameworks.base.common;

import com.gomcarter.frameworks.base.converter.Convertable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @author gomcarter
 */
public abstract class ReflectionUtils {

    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final Field field) {
        Object result = null;
        if (field != null) {
            try {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                result = field.get(obj);
                field.setAccessible(accessible);
            } catch (IllegalAccessException e) {
            }
        }
        return result;
    }

    /**
     * 调用Getter方法.
     *
     * @param obj          obj
     * @param propertyName propertyName
     * @return get result
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{},
                new Object[]{});
    }

    /**
     * @param clazz clazz
     * @return methods of clazz
     */
    public static List<Method> findAllMethod(Class clazz) {
        final List<Method> res = new LinkedList<>();
        org.springframework.util.ReflectionUtils.doWithMethods(clazz, res::add);
        return res;
    }

    /**
     * @param clazz clazz
     * @return fields of clazz
     */
    public static List<Field> findAllField(Class clazz) {
        final List<Field> res = new LinkedList<>();
        org.springframework.util.ReflectionUtils.doWithFields(clazz, res::add);
        return res;
    }

    /**
     * @param target        target
     * @param field         field
     * @param originalValue originalValue
     */
    public static void setFieldIfNotMatchConvertIt(Object target, Field field, Object originalValue) {
        // 获取转换器
        Convertable converter = Convertable.getConverter(field.getType());
        Object fieldValue = converter.convert(originalValue, field.getAnnotatedType().getType());

        setField(target, field, fieldValue);
    }

    /**
     * @param target     target
     * @param field      field
     * @param fieldValue fieldValue
     */
    public static void setField(Object target, Field field, Object fieldValue) {
        // 设置变量
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        org.springframework.util.ReflectionUtils.setField(field, target, fieldValue);
        field.setAccessible(accessible);
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     *
     * @param obj          obj
     * @param propertyName propertyName
     * @param value        value
     */
    public static void invokeSetterMethod(Object obj, String propertyName,
                                          Object value) {
        invokeSetterMethod(obj, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     *
     * @param obj          obj
     * @param propertyName propertyName
     * @param value        value
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetterMethod(Object obj, String propertyName,
                                          Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type},
                new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     *
     * @param obj       obj
     * @param fieldName fieldName
     * @return Object
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + obj + "]");
        }

        return getFieldValue(obj, field);
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     *
     * @param obj       obj
     * @param fieldName fieldName
     * @param value     value
     */
    public static void setFieldValue(final Object obj, final String fieldName,
                                     final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     *
     * @param obj       obj
     * @param fieldName fieldName
     * @return Field
     */
    public static Field getAccessibleField(final Object obj,
                                           final String fieldName) {
        AssertUtils.notNull(obj, "object不能为空");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 对于被cglib AOP过的对象, 取得真实的Class类型.
     *
     * @param clazz clazz
     * @return the real Class
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     *
     * @param obj            obj
     * @param methodName     methodName
     * @param parameterTypes parameterTypes
     * @param args           args
     * @return Object
     */
    public static Object invokeMethod(final Object obj,
                                      final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method ["
                    + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     *
     * @param obj            obj
     * @param methodName     methodName
     * @param parameterTypes parameterTypes
     * @return Method
     */
    public static Method getAccessibleMethod(final Object obj,
                                             final String methodName, final Class<?>... parameterTypes) {
        AssertUtils.notNull(obj, "object不能为空");

        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName,
                        parameterTypes);

                method.setAccessible(true);

                return method;

            } catch (NoSuchMethodException e) {// NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
     * extends HibernateDao&lt;User&gt;
     *
     * @param clazz The class to introspect
     * @param <T>   T
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 如public UserDao
     * extends HibernateDao&lt;User,Long&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz,
                                                final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     *
     * @param e e
     * @return RuntimeException
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(
            Exception e) {
        if (e instanceof IllegalAccessException
                || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException("Reflection Exception.",
                    ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * @param cls cls
     * @param <T> t
     * @return instance of cls
     */
    public static <T> T newInstance(Class<T> cls) {
        T r = null;
        try {
            r = cls.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("生成类实例失败！");
        }
        return r;
    }

    /**
     * @param collection        collection
     * @param keyPropertyName   keyPropertyName
     * @param valuePropertyName valuePropertyName
     * @param <K>               k
     * @param <V>               v
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> extractToMap(final Collection<?> collection, final String keyPropertyName, final String valuePropertyName) {
        Map<K, V> map = new HashMap<K, V>();
        if (CollectionUtils.isEmpty(collection)) {
            return map;
        }
        Iterator<?> iterator = collection.iterator();
        Object value = iterator.next();
        Field keyField = getExistAccessibleField(value, keyPropertyName);
        Field valueField = getExistAccessibleField(value, valuePropertyName);
        try {
            map.put((K) keyField.get(value), (V) valueField.get(value));
            while (iterator.hasNext()) {
                Object v = iterator.next();
                map.put((K) keyField.get(v), (V) valueField.get(v));
            }
        } catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
        return map;
    }

    /**
     * @param collection      collection
     * @param keyPropertyName keyPropertyName
     * @param <K>             k
     * @param <V>             v
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> tranToMap(final Collection<V> collection, final String keyPropertyName) {
        Map<K, V> map = new HashMap<K, V>();
        if (CollectionUtils.isEmpty(collection)) {
            return map;
        }
        Iterator<V> iterator = collection.iterator();
        V value = iterator.next();
        Field keyField = getExistAccessibleField(value, keyPropertyName);
        try {
            map.put((K) keyField.get(value), value);
            while (iterator.hasNext()) {
                V v = iterator.next();
                map.put((K) keyField.get(v), v);
            }
        } catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
        return map;
    }

    /**
     * @param collection   collection
     * @param propertyName propertyName
     * @param <K>          K
     * @return ArrayList
     */
    public static <K> ArrayList<K> extractToList(final Collection<?> collection, final String propertyName) {
        ArrayList<K> list = new ArrayList<K>();
        fillCollection(list, collection, propertyName);
        return list;
    }

    /**
     * @param collection   collection
     * @param propertyName propertyName
     * @param <K>          K
     * @return HashSet
     */
    public static <K> HashSet<K> extractToSet(final Collection<?> collection, final String propertyName) {
        HashSet<K> set = new HashSet<K>();
        fillCollection(set, collection, propertyName);
        return set;
    }

    /**
     * @param result       result
     * @param collection   collection
     * @param propertyName propertyName
     * @param <K>          k
     */
    @SuppressWarnings("unchecked")
    private static <K> void fillCollection(final Collection<K> result, final Collection<?> collection, final String propertyName) {
        AssertUtils.notNull(result, "接收集合错误");
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        Iterator<?> iterator = collection.iterator();
        Object value = iterator.next();
        Field propertyField = getExistAccessibleField(value, propertyName);
        try {
            result.add((K) propertyField.get(value));
            while (iterator.hasNext()) {
                result.add((K) propertyField.get(iterator.next()));
            }
        } catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * @param value        value
     * @param propertyName propertyName
     * @return Field
     */
    public static Field getExistAccessibleField(Object value, String propertyName) {
        Field propertyField = getAccessibleField(value, propertyName);
        if (propertyField == null) {
            throw new RuntimeException(value.getClass().getSimpleName() + " 没有【" + propertyName + "】属性");
        }
        return propertyField;
    }

    /**
     * 对比两个对象 不会显示list异常
     *
     * @param obj1 obj1
     * @param obj2 obj2
     * @param <T>  T
     * @return String
     */
    public static <T> String compare(T obj1, T obj2) {
        return compare(obj1, obj2, "");
    }

    private static <T> String compare(T t1, T t2, String baseName) {
        StringBuffer str = new StringBuffer();
        if (t1 == null) {
            return StringUtils.EMPTY;
        }
        Field[] fields = t1.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String name = baseName + field.getName();
                field.setAccessible(true);
                Object obj1 = field.get(t1);
                Object obj2;
                if (t2 == null) {
                    obj2 = null;
                } else {
                    obj2 = field.get(t2);
                }

                if (isBaseType(obj1, obj2)) {
                    if (obj1 != null && !obj1.equals(obj2)) {
                        if (obj1 instanceof Date) {
                            str.append(name).append(":{").append(DateFormatUtils.format((Date) obj1, DATE_FORMAT_FULL)).append("->").append(obj2 == null ? "null" : DateFormatUtils.format((Date) obj2, DATE_FORMAT_FULL)).append("},");
                        } else {
                            str.append(name).append(":{").append(obj1.toString()).append("->").append(obj2 == null ? "null" : obj2.toString()).append("},");
                        }
                    } else if (obj1 == null && obj2 != null) {
                        if (obj2 instanceof Date) {
                            str.append(name).append(":{null->").append(DateFormatUtils.format((Date) obj2, DATE_FORMAT_FULL)).append("},");
                        } else {
                            str.append(field.getName()).append(":{null->").append(obj2.toString()).append("},");
                        }
                    }
                } else {
                    if ((obj1 != null && obj1.getClass().isArray()) || (obj2 != null && obj2.getClass().isArray())) {
                        continue;
                    }
                    name += ".";
                    str.append(compare(obj1, obj2, name));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    private static boolean isBaseType(Object obj1, Object obj2) {
        Object obj = obj1;
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 == null && obj2 != null) {
            obj = obj2;
        }
        if (obj instanceof String) {
            return true;
        }
        if (obj instanceof Number) {
            return true;
        }
        if (obj instanceof Boolean) {
            return true;
        }
        if (obj instanceof Character) {
            return true;
        }
        if (obj instanceof Date) {
            return true;
        }
        if (obj.getClass().isPrimitive()) {
            return true;
        }
        return false;
    }
}

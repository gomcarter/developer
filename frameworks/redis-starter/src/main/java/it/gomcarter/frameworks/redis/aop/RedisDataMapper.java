/**
 * Copyright (c) 2005-2011 springside.org.cn
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p>
 * $Id: JsonMapper.java 1574 2011-05-09 13:39:10Z calvinxiu $
 */
package it.gomcarter.frameworks.redis.aop;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单封装Jackson实现JSON<->Java Object的Mapper.
 * <p>
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 *
 * @author calvin
 */
public class RedisDataMapper {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper mapper;

    @SuppressWarnings("deprecation")
    public RedisDataMapper(Inclusion inclusion) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.getSerializationConfig().setSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    }

    /**
     * 创建输出全部属性到Json字符串的Mapper.
     */
    public static RedisDataMapper buildNormalMapper() {
        return new RedisDataMapper(Inclusion.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     */
    public static RedisDataMapper buildNonNullMapper() {
        return new RedisDataMapper(Inclusion.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     */
    public static RedisDataMapper buildNonDefaultMapper() {
        return new RedisDataMapper(Inclusion.NON_DEFAULT);
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时使用如下语句,使用後面的函數.
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 構造泛型的Type如List<MyBean>, Map<String,MyBean>
     */
    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum,
     * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
     * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    @SuppressWarnings("deprecation")
    public void setEnumUseToString(boolean value) {
        mapper.getSerializationConfig().set(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
        mapper.getDeserializationConfig().set(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需读取集合如List/Map, 且不是List<String>時,
     * 先用constructParametricType(List.class,MyBean.class)構造出JavaTeype,再調用本函數.
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.error("Json转换出错", e);
            return null;
        }
    }

    public <T> T fromJson(String jsonString, TypeReference<T> javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.error("Json转换出错", e);
            return null;
        }
    }

    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public <T> T update(T object, String jsonString) {
        try {
            return (T) mapper.updatingReader(object).readValue(jsonString);
        } catch (Exception e) {
            logger.error("Json转换出错", e);
        }
        return null;
    }

    public <T> List<T> fromJsonToList(String jsonString, Class<T> clazz) {
        List<T> objectList = new ArrayList<T>();
        RedisDataMapper jsonMapper = RedisDataMapper.buildNormalMapper();
        if (jsonString.startsWith("[")) { //List
            JavaType javaType = jsonMapper.constructParametricType(List.class, clazz);
            objectList = jsonMapper.fromJson(jsonString, javaType);
        } else { //Single
            T t = jsonMapper.fromJson(jsonString, clazz);
            if (null != t) {
                objectList.add(t);
            } else {
                objectList = null;
            }
        }
        return (List<T>) objectList;
    }
}

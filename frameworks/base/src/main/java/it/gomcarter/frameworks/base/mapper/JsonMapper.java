/**
 * Copyright (c) 2005-2011 springside.org.cn
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p/>
 * $Id: JsonMapper.java 1574 2011-05-09 13:39:10Z calvinxiu $
 */
package it.gomcarter.frameworks.base.mapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 简单封装Jackson实现JSON<->Java Object的Mapper.
 * <p/>
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 *
 * @author calvin
 */
public class JsonMapper {

    private static final JsonMapper defaultMapper = new JsonMapper(Include.NON_NULL);

    private static JsonMapper timeFormatMapper;

    protected final Logger logger = LoggerFactory.getLogger("error_file_log");

    private ObjectMapper mapper;

    public JsonMapper(Include inclusion) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(inclusion);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    public JsonMapper(Include inclusion, String timeFormat) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(inclusion);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.setDateFormat(new SimpleDateFormat(timeFormat));
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNotNullMapper() {
        return defaultMapper;
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     * <p/>
     * 此方法可废弃，请使用新方法  buildNotNullMapper
     */
    public static JsonMapper buildNonNullMapper() {
        return defaultMapper;
    }

    public static JsonMapper buildNonNullTimeFormatMapper() {
        if (timeFormatMapper == null) {
            timeFormatMapper = new JsonMapper(Include.NON_NULL, "yyyy-MM-dd HH:mm:ss");
        }
        return timeFormatMapper;
    }

//    public ObjectMapper getMapper() { //去掉该代码,防止mapper被修改
//        return this.mapper;
//    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p/>
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时使用如下语句,使用後面的函數.
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.error("Json转换出错", e);
            return null;
        }
    }


    /**
     * 構造泛型的Type如List<MyBean>, Map<String,MyBean>
     */
    @SuppressWarnings("deprecation")
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
            logger.error("Json转换出错", e);
            return null;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p/>
     * 如需读取集合如List/Map, 且不是List<String>時,
     * 先用constructParametricType(List.class,MyBean.class)構造出JavaTeype,再調用本函數.
     */
    public <T> T fromJson(String jsonString, JavaType javaType) {
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


    public <T> Iterable<T> fromJsonToCollection(String jsonString, Class<? extends Iterable> collectionClass, Class<T> clazz) {
        if (jsonString.startsWith("[")) {
            return fromJson(jsonString, constructParametricType(collectionClass, clazz));
        } else {
            //Single
            return fromJsonToCollection("[" +  jsonString + "]", collectionClass, clazz);
        }
    }

    public <T> List<T> fromJsonToList(String jsonString, Class<T> clazz) {
        return (List<T>) this.fromJsonToCollection(jsonString, List.class, clazz);
    }
}

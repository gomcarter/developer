package com.gomcarter.frameworks.redis.aop;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 简单封装Jackson实现JSON&lt;-&gt;Java Object的Mapper.
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
        mapper.setSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    }

    /**
     * 创建输出全部属性到Json字符串的Mapper.
     *
     * @return RedisDataMapper
     */
    public static RedisDataMapper buildNormalMapper() {
        return new RedisDataMapper(Inclusion.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     *
     * @return RedisDataMapper
     */
    public static RedisDataMapper buildNonNullMapper() {
        return new RedisDataMapper(Inclusion.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     *
     * @return RedisDataMapper
     */
    public static RedisDataMapper buildNonDefaultMapper() {
        return new RedisDataMapper(Inclusion.NON_DEFAULT);
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需读取集合如List/Map, 且不是List&lt;String&gt;这种简单类型时使用如下语句,使用後面的函數.
     *
     * @param jsonString jsonString
     * @param clazz      clazz
     * @param <T>        clazz
     * @return instance of clazz
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
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     *
     * @param object object
     * @return json string
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需读取集合如List/Map, 且不是List&lt;String&gt;時,
     * 先用constructParametricType(List.class,MyBean.class)構造出JavaTeype,再調用本函數.
     *
     * @param jsonString jsonString
     * @param javaType   javaType
     * @param <T>        javaType
     * @return instance of javaType
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
}

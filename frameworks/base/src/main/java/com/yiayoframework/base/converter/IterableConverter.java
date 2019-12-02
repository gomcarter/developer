package com.yiayoframework.base.converter;

import com.yiayoframework.base.mapper.JsonMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 对象类型转换器
 *
 * @author 李银 on 2019-11-16 13:58:11
 */
public class IterableConverter implements Convertable {

    @Override
    public <T> T convert(Object sourceValue, Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class kls = (Class) parameterizedType.getActualTypeArguments()[0];
        Class collectionClass = (Class) parameterizedType.getRawType();

        return (T) JsonMapper.buildNonNullMapper().fromJsonToCollection(sourceValue + "", collectionClass, kls);
    }
}

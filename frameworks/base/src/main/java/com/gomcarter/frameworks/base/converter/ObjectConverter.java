package com.gomcarter.frameworks.base.converter;

import com.gomcarter.frameworks.base.mapper.JsonMapper;

import java.lang.reflect.Type;

/**
 * 对象类型转换器
 *
 * @author gomcarter on 2019-11-16 13:58:11
 */
public class ObjectConverter implements Convertable {

    @Override
    public <T> T convert(Object sourceValue, Type type) {
        return JsonMapper.buildNonNullMapper().fromJson(sourceValue + "", (Class<T>) type);
    }
}

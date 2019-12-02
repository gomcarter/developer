package io.github.gomcarter.frameworks.base.converter;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.convert.support.GenericConversionService;

import java.lang.reflect.Type;

/**
 * 基础类型转换器
 *
 * @author gomcarter on 2019-11-16 13:58:11
 */
public class PrimitiveConverter implements Convertable {

    @Override
    public <T> T convert(Object sourceValue, Type type) {
        // SimpleTypeConverter线程不安全，只能每次转换每次new
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        typeConverter.setConversionService(new GenericConversionService());
        return typeConverter.convertIfNecessary(sourceValue, (Class<T>) type);
    }
}

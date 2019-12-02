package com.gomcarter.frameworks.base.converter;

import com.gomcarter.frameworks.base.common.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * 类型转换
 *
 * @author gomcarter on 2019-11-16 13:58:11
 */
public interface Convertable {
    Logger logger = LoggerFactory.getLogger(Convertable.class);

    <T> T convert(Object sourceValue, Type type);



    Convertable PRIMITIVE_CONVERTER = new PrimitiveConverter();
    Convertable ITERABLE_CONVERTER = new IterableConverter();
    Convertable PROPERTIES_CONVERTER = new PropertiesConverter();
    Convertable OBJECT_CONVERTER = new ObjectConverter();

    static Convertable getConverter(Class kls) {
        if (BeanUtils.isSimpleProperty(kls) || kls == Object.class) {
            return PRIMITIVE_CONVERTER;
        } else if (kls.isArray() || Iterable.class.isAssignableFrom(kls)) {
            return ITERABLE_CONVERTER;
        } else if (Properties.class.isAssignableFrom(kls) || File.class.isAssignableFrom(kls)) {
            return PROPERTIES_CONVERTER;
        }
        return OBJECT_CONVERTER;
    }
}

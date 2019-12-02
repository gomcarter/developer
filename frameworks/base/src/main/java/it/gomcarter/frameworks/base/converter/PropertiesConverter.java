package it.gomcarter.frameworks.base.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * Properties类型转换器
 *
 * @author gomcarter on 2019-11-16 13:58:11
 */
public class PropertiesConverter implements Convertable {

    @Override
    public Properties convert(Object sourceValue, Type ignore) {
        if (sourceValue == null) {
            return null;
        }

        try {
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(sourceValue.toString().getBytes()));
            return properties;
        } catch (IOException e) {
            logger.error("转换失败：{} ", sourceValue, e);
        }

        return null;
    }
}

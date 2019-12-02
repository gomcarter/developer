package it.gomcarter.frameworks.base.spring;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * 接受
 */
public abstract class ModifyUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

    static {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> T fromJson(String jsonString, Type type) throws IOException {
        return mapper.readValue(jsonString, typeFactory.constructType(type));
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }

    public static Object calcValue(MethodParameter methodParameter, String[] values) {
        if (values == null) {
            return null;
        }
        String value;
        Class<?> clz = methodParameter.getParameterType();
        if (Collection.class.isAssignableFrom(clz) || clz.isArray()) {
            if (values.length == 1) {
                value = values[0];
                if (!value.startsWith("[")) {
                    value = ModifyUtils.mark2List(value);
                }
            } else {
                value = ModifyUtils.mark2List(StringUtils.join(values, ','));
            }
        } else {
            value = values.length > 0 ? values[0] : null;
        }
        try {
            return fromJson(value, methodParameter.getGenericParameterType());
        } catch (IOException e1) {
            return values.length == 1 ? values[0] : values;
        }
    }

    public static String mark2List(String value) {
        StringBuilder sb = new StringBuilder(value.length() + 2);
        sb.append('[');
        sb.append(value);
        sb.append(']');
        value = sb.toString();
        return value;
    }
}

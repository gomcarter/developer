package io.github.gomcarter.frameworks.interfaces.utils;


import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamSource;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.util.Map;

/**
 * @author gomcarter on 2019-12-02 09:23:09
 */
public enum DataType {
    /**
     * such as：Long，long，Integer，Date，BigDecimal etc.
     */
    simple,
    /**
     * such as：List，Iterable，array etc
     */
    collection,
    /**
     * file
     */
    file,
    /**
     * map
     */
    map,
    /**
     * http request
     */
    request,
    /**
     * http response
     */
    response,
    /**
     * other
     */
    object;

    public static DataType get(Class kls) {
        if (BeanUtils.isSimpleProperty(kls) || kls == Object.class) {
            return simple;
        } else if (kls.isArray() || Iterable.class.isAssignableFrom(kls)) {
            return collection;
        } else if (InputStreamSource.class.isAssignableFrom(kls) || File.class.isAssignableFrom(kls)) {
            return file;
        } else if (ServletRequest.class.isAssignableFrom(kls)) {
            return request;
        } else if (ServletResponse.class.isAssignableFrom(kls)) {
            return response;
        } else if (Map.class.isAssignableFrom(kls)) {
            return map;
        }
        return object;
    }
}

package com.gomcarter.frameworks.interfaces.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gomcarter on 2019-12-02 09:23:09
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,
        ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.TYPE,
        ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
        ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Notes {

    /**
     * content
     *
     * @return content
     */
    String value() default "";

    /**
     * null or not
     *
     * @return true then not null
     */
    boolean notNull() default false;
}

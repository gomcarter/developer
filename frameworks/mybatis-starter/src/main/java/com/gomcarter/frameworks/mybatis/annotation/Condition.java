package com.gomcarter.frameworks.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author gomcarter on 2019-11-09 22:53:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Condition {

    /**
     * @return 对应数据库的字段，不写默认为该字段名
     */
    String field() default "";

    /**
     * @return 匹配方式，默认的 equal
     */
    MatchType type() default MatchType.EQ;
}

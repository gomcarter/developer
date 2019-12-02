package com.yiayoframework.redis.annotation;

import com.yiayoframework.redis.DiamondRedisRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author liyin on 2019-09-05 16:00:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DiamondRedisRegistrar.class)
public @interface EnableDiamondRedis {

    /**
     * diamond data_id
     */
    @AliasFor("value")
    String dataId() default "CONNECTION";

    /**
     * data_id
     */
    @AliasFor("dataId")
    String value() default "CONNECTION";

    /**
     * diamond group
     */
    String group() default "REDIS";
}

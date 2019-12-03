package com.gomcarter.frameworks.redis.annotation;

import com.gomcarter.frameworks.redis.NacosRedisRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author gomcarter on 2019-09-05 16:00:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NacosRedisRegistrar.class)
public @interface EnableNacosRedis {

    /**
     * nacos data_id
     *
     * @return dataId
     */
    @AliasFor("value")
    String dataId() default "CONNECTION";

    /**
     * data_id
     *
     * @return dataId
     */
    @AliasFor("dataId")
    String value() default "CONNECTION";

    /**
     * nacos group
     *
     * @return group
     */
    String group() default "REDIS";
}

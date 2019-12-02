package io.github.gomcarter.frameworks.redis.annotation;

import io.github.gomcarter.frameworks.base.exception.NonConcurrencyException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁，基于redis的setnx实现，在需要锁定的入口函数上@Lock即可
 *
 * @author liyin on 2019-09-05 16:00:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {
    /**
     * 需要锁定的key
     */
    String value() default "";

    /**
     * 将方法的参数并入到key中（参数将作toString处理，为null则是null字符串）
     * argsIndex为方法本身参数的索引，如argsIndex={0,2}及取方法的第1个和第3个参数拼接到key中。
     * 如果不需要参数拼接到key中，则忽略此参数即可
     */
    int[] argsIndex() default {};

    /**
     * 默认120秒之后自动释放
     */
    long timeout() default 120L;

    /**
     * <p>
     * 如果前面已经存在一个线程在调用此方法，当前线程是否等待，单位：毫秒；
     * 为了线程阻塞严重，使用short类型，所以最大等待时间为32秒左右
     * <p>
     * 默认不等待，并发时直接讲后进入的熔断
     * </p>
     * <p>
     * <p>
     * <p>
     * 小于0等于0：不等待（直接返回{@link NonConcurrencyException}）；
     * <p>
     * 大于0：表示最大等待时间，超时返回服务器繁忙
     * </p>
     */
    short await() default 0;
}

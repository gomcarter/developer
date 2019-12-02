package com.gomcarter.frameworks.redis;

import com.gomcarter.frameworks.redis.aop.DataRedisInterceptor;
import com.gomcarter.frameworks.redis.tool.RedisProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class RedisConfiguration {

    private DataRedisInterceptor dataRedisInterceptor;

    @Bean
    public DataRedisInterceptor dataRedisInterceptor(RedisProxy redisProxy) {
        dataRedisInterceptor = new DataRedisInterceptor()
                .setRedisProxy(redisProxy);
        return dataRedisInterceptor;
    }

    @Pointcut("@annotation(com.gomcarter.frameworks.redis.annotation.Cache)")
    public void cacheData() {
    }

    @Around("cacheData()")
    public Object cacheData(ProceedingJoinPoint joinPoint) throws Throwable {
        return dataRedisInterceptor.cacheData(joinPoint);
    }

    @Pointcut("@annotation(com.gomcarter.frameworks.redis.annotation.DelCache)")
    public void delCache() {
    }

    @AfterReturning("delCache()")
    public void delCache(JoinPoint joinPoint) throws Throwable {
        dataRedisInterceptor.dropCache(joinPoint);
    }

    @Pointcut("@annotation(com.gomcarter.frameworks.redis.annotation.Lock)")
    public void lockCache() {
    }

    @Around("lockCache()")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        return dataRedisInterceptor.lock(joinPoint);
    }

}

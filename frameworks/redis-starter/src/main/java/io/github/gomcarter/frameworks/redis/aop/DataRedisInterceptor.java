package io.github.gomcarter.frameworks.redis.aop;

import com.alibaba.nacos.client.config.utils.MD5;
import io.github.gomcarter.frameworks.base.exception.NonConcurrencyException;
import io.github.gomcarter.frameworks.redis.annotation.Cache;
import io.github.gomcarter.frameworks.redis.annotation.DelCache;
import io.github.gomcarter.frameworks.redis.annotation.Lock;
import io.github.gomcarter.frameworks.redis.tool.RedisProxy;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author by 李银 2018年1月9日 16:45:42
 */
public class DataRedisInterceptor {

    private RedisProxy redisProxy;

    private RedisDataMapper dataMapper = RedisDataMapper.buildNormalMapper();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 缓存annotation标签
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object cacheData(ProceedingJoinPoint joinPoint) throws Throwable {
        //如果未开启缓存 则直接关闭
        if (!redisProxy.needCache()) {
            return joinPoint.proceed();
        }

        //获取到方法
        Method m = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Cache caches = m.getAnnotation(Cache.class);
        //如果key为空，则取方法名为key
        String key = StringUtils.defaultIfBlank(caches.value(), joinPoint.getTarget().getClass().getName() + "_" + m.getName());

        //如果设定了argsIndex，把argsIndex对应的args也添加到key里面来
        key = this.getKey(key, caches.argsIndex(), joinPoint.getArgs());
        try {
            // 如果有缓存就直接获取缓存数据
            String value = redisProxy.get(key);
            if (value != null) {
                // logger.info(Thread.currentThread().getName() + "有数据，出去咯");
                return dataMapper.fromJson(value, TypeFactory.defaultInstance().constructType(m.getGenericReturnType()));
            }
        } catch (Exception e) {
            logger.error(joinPoint.getClass().getName() + "存在数据，但是调用缓存失败", e);
        }


        // 如果没有缓存数据就抢占资源
        // 避免并发，多个线程同时进入方法，导致数据库压力增大，这里只允许一个线程进入
        String lockerKey = key + "____concurrent_locker____";
        // 获取最大等待时间
        short await = caches.await();
        // 每次睡100毫秒
        int sleepPerTimeMS = 100;
        // 总共睡多少次
        long sleepTimes = await / sleepPerTimeMS + 1;
        int count = 0;

        // 默认锁300秒，避免服务挂了，一直锁在哪
        Long maxLockTime = 300L;
        while (!redisProxy.lock(lockerKey, maxLockTime)) {
            if (await <= 0 || count >= sleepTimes) {
                throw new RuntimeException("服务器繁忙，请稍后重试");
            }

            // TODO: 自旋锁优化，sleep 会导致核心态用户态切换，性能下降
            Thread.sleep(sleepPerTimeMS);

            // 超过sleepTimes次没有获得锁，证明前序线程太费时间，更不能进去添乱。就直接熔断降级处理了。
            count++;
        }

        try {
            // 获得锁，查看是否有缓存，有就直接获取缓存数据
            String value = redisProxy.get(key);
            if (value != null) {
                // logger.info(Thread.currentThread().getName() + "等到数据拉，出去咯");
                return dataMapper.fromJson(value, TypeFactory.defaultInstance().constructType(m.getGenericReturnType()));
            } else {
                Object object = joinPoint.proceed();
                // 将返回结果放入缓存
                // JDATA Mapper谁都不许改！！！！ 因为Jmapper缓存会把jsonIgnore的给去掉
                String jsonData = dataMapper.toJson(object);
                if (caches.nullable() || !isEmptyJson(jsonData)) {
                    if (!redisProxy.set(key, jsonData, Long.valueOf(caches.time()) * 60L)) {
                        logger.error("{}取到数据，但是存入缓存失败", joinPoint.getClass().getName());
                    }
                }
                return object;
            }
        } finally {
            // 释放锁
            redisProxy.del(lockerKey);
        }
    }

    /**
     * 去除缓存方法
     *
     * @param joinPoint
     * @throws Throwable
     */
    public void dropCache(JoinPoint joinPoint) {
        // 如果未开启缓存 则直接返回
        if (redisProxy.needCache()) {
            try {
                // 如果key为空，则取方法名为key
                Method m = ((MethodSignature) joinPoint.getSignature()).getMethod();
                DelCache dropCache = m.getAnnotation(DelCache.class);

                if (StringUtils.isBlank(dropCache.value())) {
                    this.logger.warn("{}的DelCache标签并未启用，请设置key参数",
                            joinPoint.getTarget().getClass().getName() + "." + m.getName());
                    return;
                }

                redisProxy.del(this.getKey(dropCache.value(), dropCache.argsIndex(), joinPoint.getArgs()));
            } catch (Exception e) {
                logger.error(joinPoint.getClass().getName() + "删除缓存失败", e);
            }
        }
    }

    /**
     * 执行分布式锁
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取到方法
        Method m = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Lock locker = m.getAnnotation(Lock.class);
        //如果key为空，则取方法名为key
        String key = StringUtils.defaultIfBlank(locker.value(), joinPoint.getTarget().getClass().getName() + "_" + m.getName());

        //如果设定了argsIndex，把argsIndex对应的args也添加到key里面来
        key = this.getKey(key, locker.argsIndex(), joinPoint.getArgs());

        // 获取最大等待时间
        short await = locker.await();
        // 每次睡100毫秒
        int sleepPerTimeMS = 100;
        // 总共睡多少次
        long sleepTimes = await / sleepPerTimeMS + 1;
        int count = 0;
        //开始锁定，非公平锁，谁运气好谁的
        while (!redisProxy.lock(key, locker.timeout())) {
            if (await <= 0 || count >= sleepTimes) {
                throw new NonConcurrencyException();
            }

            // TODO: 自旋锁优化，sleep 会导致核心态用户态切换，性能下降
            // 方案：可以自旋1秒，如果超过1秒还没有获得锁，则直接进入休眠
            Thread.sleep(sleepPerTimeMS);

            // 超过sleepTimes次没有获得锁，证明前序线程太费时间，更不能进去添乱。就直接熔断降级处理了。
            count++;
        }

        // 锁成功，开始执行
        try {
            return joinPoint.proceed();
        } finally {
            //执行完毕，释放锁
            redisProxy.del(key);
        }
    }

    private String getKey(String key, int[] argsIndex, Object[] args) {
        if (args == null || args.length == 0) {
            return key;
        }

        if (argsIndex.length == 0) {
            return key + "_" + Arrays.stream(args)
                    .map(s -> {
                        try {
                            return dataMapper.toJson(s);
                        } catch (Exception e) {
                            return s + "";
                        }
                    })
                    .reduce((a, b) -> a + "_" + b)
                    .orElse(StringUtils.EMPTY);
        } else {
            return key + "_" + MD5.getInstance().getMD5String(Arrays.stream(argsIndex)
                    .mapToObj(s -> {
                        Object arg = args[s];

                        // FIXME: 以后解决这里性能问题
                        try {
                            return dataMapper.toJson(arg);
                        } catch (Exception e) {
                            return arg + "";
                        }
                    })
                    .reduce((a, b) -> a + "_" + b)
                    .orElse(StringUtils.EMPTY)
            );
        }
    }

    /**
     * Json字符串是否为空(对象或集合)
     *
     * @param jsonData
     * @return
     */
    private boolean isEmptyJson(String jsonData) {
        return "null".equalsIgnoreCase(jsonData) || "[]".equals(jsonData);
    }


    public DataRedisInterceptor setRedisProxy(RedisProxy redisProxy) {
        this.redisProxy = redisProxy;
        return this;
    }
}

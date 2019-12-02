package com.gomcarter.frameworks.redis.factory;

import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.CustomStringUtils;
import com.gomcarter.frameworks.base.mapper.JsonMapper;
import com.gomcarter.frameworks.redis.tool.RedisProxy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class RedisConnectionBuilder {
    static final Logger logger = LoggerFactory.getLogger(RedisConnectionBuilder.class);

    static Pattern pattern = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    public static void of(RedisProxy proxy, Properties properties) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        //最大空闲数
        poolConfig.setMaxIdle(CustomStringUtils.parseInt(properties.getProperty("redis.maxIdle"), 200));
        poolConfig.setMinIdle(CustomStringUtils.parseInt(properties.getProperty("redis.minIdle"), 10));
        poolConfig.setMaxTotal(CustomStringUtils.parseInt(properties.getProperty("redis.maxTotal"), 1000));
        //最大建立连接等待时间
        poolConfig.setMaxWaitMillis(CustomStringUtils.parseInt(properties.getProperty("redis.maxWait"), 1000));
        //是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        poolConfig.setTestOnBorrow(Boolean.valueOf(properties.getProperty("redis.testOnBorrow")));

        Integer timeout = CustomStringUtils.parseInt(properties.getProperty("redis.timeout"), 300000);
        Integer maxAttempts = CustomStringUtils.parseInt(properties.getProperty("redis.maxAttempts"), 6);
        String password = properties.getProperty("redis.password");

        String mode = properties.getProperty("redis.mode");
        AssertUtils.notNull(mode);

        Set<HostAndPort> haps = new HashSet<>();
        for (Object key : properties.keySet()) {
            String k = (String) key;
            if (!StringUtils.startsWithIgnoreCase(k, mode)) {
                continue;
            }

            String val = properties.getProperty(k);

            boolean isIpPort = pattern.matcher(val).matches();
            if (!isIpPort) {
                throw new IllegalArgumentException("ip 或 port 不合法");
            }
            String[] ipAndPort = val.split(":");

            logger.info("ipAndPort: {}", JsonMapper.buildNonNullMapper().toJson(ipAndPort));
            HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            haps.add(hap);
        }

        synchronized (RedisProxy.class) {
            proxy.setNeedCache(Boolean.valueOf(properties.getProperty("redis.cache")));

            if ("cluster".equals(mode)) {
                proxy.setJedisCluster(new JedisCluster(haps, timeout, timeout, maxAttempts, password, poolConfig));
            } else if ("standalone".equals(mode)) {
                HostAndPort hostAndPort = haps.iterator().next();
                proxy.setJedisPool(new JedisPool(poolConfig, hostAndPort.getHost(), hostAndPort.getPort(), timeout, password));
            } else {
                logger.error("fail to initialized redis, cause of error mode");
                throw new RuntimeException("fail to initialized redis, cause of error mode");
            }
        }
    }
}

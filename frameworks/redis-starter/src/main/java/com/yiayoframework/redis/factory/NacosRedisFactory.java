package com.yiayoframework.redis.factory;

import com.yiayoframework.base.common.NacosClientUtils;
import com.yiayoframework.redis.tool.RedisProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * @author 李银 on 2019-11-19 11:07:19
 */
public class NacosRedisFactory implements FactoryBean<RedisProxy>, InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String dataId = "CONNECTION";
    private String group = "REDIS";

    private RedisProxy proxy = new RedisProxy();

    @Override
    public RedisProxy getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<? extends RedisProxy> getObjectType() {
        return (this.proxy != null ? this.proxy.getClass() : RedisProxy.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NacosClientUtils.addListenerAsProperties(dataId, group, (p) -> {
            RedisConnectionBuilder.of(proxy, p);
        });

        Properties properties = NacosClientUtils.getConfigAsProperties(this.dataId, this.group);
        RedisConnectionBuilder.of(proxy, properties);
    }

    public NacosRedisFactory setGroup(String group) {
        this.group = group;
        return this;
    }

    public NacosRedisFactory setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }
}

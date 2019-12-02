package com.yiayoframework.redis.factory;

import com.taobao.diamond.client.impl.DiamondEnv;
import com.taobao.diamond.common.Constants;
import com.taobao.diamond.manager.impl.PropertiesListener;
import com.yiayoframework.redis.tool.RedisProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.StringReader;
import java.util.Properties;

/**
 * @author 李银 on 2019-11-19 11:07:19
 */
public class DiamondRedisFactory implements FactoryBean<RedisProxy>, InitializingBean {
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

    private void initRedisClient(Properties properties) throws Exception {
        RedisConnectionBuilder.of(proxy, properties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DiamondEnv diamondEnv = DiamondEnv.instance();
        diamondEnv.addListeners(this.dataId, this.group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                try {
                    initRedisClient(properties);
                } catch (Exception e) {
                    logger.error("update redis failed:", e);
                }
            }
        });

        Properties properties = new Properties();
        properties.load(new StringReader(diamondEnv.getConfig(this.dataId, this.group, Constants.GETCONFIG_LOCAL_SERVER_SNAPSHOT, 20000)));
        this.initRedisClient(properties);
    }

    public DiamondRedisFactory setGroup(String group) {
        this.group = group;
        return this;
    }

    public DiamondRedisFactory setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }
}

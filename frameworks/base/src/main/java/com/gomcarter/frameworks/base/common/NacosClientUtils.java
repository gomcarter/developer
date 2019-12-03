package com.gomcarter.frameworks.base.common;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.gomcarter.frameworks.base.converter.PropertiesConverter;
import com.gomcarter.frameworks.base.mapper.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * @author gomcarter on 2019-11-15 15:20:46
 */
public class NacosClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(NacosClientUtils.class);

    private static class Holder {
        static ConfigService configService;

        static {
            String namespace = namespace();
            Properties properties = new Properties();
            if (StringUtils.isNotBlank(namespace)) {
                properties.put(PropertyKeyConst.NAMESPACE, namespace);
            }
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr());
            try {
                configService = NacosFactory.createConfigService(properties);
            } catch (NacosException e) {
                logger.error("获取nacos配置示例失败", e);
            }
        }
    }

    /**
     * get naocs namespace
     * -Dnacos.namespace=xx is first
     * <p>
     * export NACOS_NAMESPACE=xx is backup
     *
     * @return namespace
     */
    public static String namespace() {
        return StringUtils.defaultIfBlank(System.getProperty("nacos.namespace"), System.getenv("NACOS_NAMESPACE"));
    }

    /**
     * get naocs serverAddr
     * -Dnacos.server.addr=yy is first
     * <p>
     * export NACOS_SERVER_ADDR=yy is backup
     *
     * @return serverAddr
     */
    public static String serverAddr() {
        String serverAddr = StringUtils.defaultIfBlank(System.getProperty("nacos.server.addr"), System.getenv("NACOS_SERVER_ADDR"));
        AssertUtils.isTrue(StringUtils.isNotBlank(serverAddr), new RuntimeException("未配置环境变量NACOS_SERVER_ADDR"));
        return serverAddr;
    }

    /**
     * get naocs configService
     *
     * @return configService
     */
    public static ConfigService instance() {
        return Holder.configService;
    }

    /**
     * @param dataId    dataId
     * @param group     groupId
     * @param timeoutMs 超时时间，单位：毫秒
     * @return config result
     */
    public static String getConfigAsString(String dataId, String group, long timeoutMs) {
        try {
            String content = instance().getConfig(dataId, group, timeoutMs);
            logger.info("加载nacos配置 dataId: {}, group: {}, content: {}", dataId, group, content);

            return content;
        } catch (NacosException e) {
            logger.error("获取nacos配置示例失败：{} - {}", dataId, group, e);
            throw new RuntimeException("获取nacos配置示例失败");
        }
    }

    /**
     * @param dataId dataId
     * @param group  group
     * @return config result
     */
    public static String getConfigAsString(String dataId, String group) {
        return getConfigAsString(dataId, group, 5000);
    }

    /**
     * @param dataId dataId
     * @param group  group
     * @param kls    kls convert to
     * @param <T>    kls T
     * @return config result
     */
    public static <T> T getConfigAsObject(String dataId, String group, Class<T> kls) {
        return getConfigAsObject(dataId, group, 5000, kls);
    }

    /**
     * @param dataId    dataId
     * @param group     group
     * @param kls       kls convert to
     * @param timeoutMs read time out
     * @param <T>       kls T
     * @return config result
     */
    public static <T> T getConfigAsObject(String dataId, String group, long timeoutMs, Class<T> kls) {
        return JsonMapper.buildNonNullMapper().fromJson(getConfigAsString(dataId, group, timeoutMs), kls);
    }

    /**
     * @param dataId dataId
     * @param group  group
     * @param kls    kls convert to
     * @param <T>    kls T
     * @return config result
     */
    public static <T> List<T> getConfigAsListObject(String dataId, String group, Class<T> kls) {
        return getConfigAsListObject(dataId, group, 5000, kls);
    }

    /**
     * @param dataId    dataId
     * @param group     group
     * @param kls       kls convert to
     * @param timeoutMs read time out
     * @param <T>       kls T
     * @return config result
     */
    public static <T> List<T> getConfigAsListObject(String dataId, String group, long timeoutMs, Class<T> kls) {
        return JsonMapper.buildNonNullMapper().fromJsonToList(getConfigAsString(dataId, group, timeoutMs), kls);
    }

    /**
     * @param dataId dataId
     * @param group  group
     * @return config result
     */
    public static Properties getConfigAsProperties(String dataId, String group) {
        return getConfigAsProperties(dataId, group, 5000);
    }

    /**
     * @param dataId    dataId
     * @param group     group
     * @param timeoutMs read time out
     * @return config result
     */
    public static Properties getConfigAsProperties(String dataId, String group, long timeoutMs) {
        String content = StringUtils.defaultString(getConfigAsString(dataId, group, timeoutMs), StringUtils.EMPTY);
        return new PropertiesConverter().convert(content, Properties.class);
    }

    /**
     * @param dataId   dataId
     * @param group    group
     * @param consumer consumer
     * @param <T>      kls T
     * @param kls      kls convert to
     */
    public static <T> void addListenerAsListObject(String dataId, String group, Consumer<List<T>> consumer, Class<T> kls) {
        addListener(dataId, group, (content) -> consumer.accept(JsonMapper.buildNonNullMapper().fromJsonToList(content, kls)));
    }

    /**
     * @param dataId   dataId
     * @param group    group
     * @param consumer consumer
     * @param <T>      kls T
     * @param kls      kls convert to* @return config result
     */
    public static <T> void addListenerAsObject(String dataId, String group, Consumer<T> consumer, Class<T> kls) {
        addListener(dataId, group, (content) -> consumer.accept(JsonMapper.buildNonNullMapper().fromJson(content, kls)));
    }

    /**
     * @param dataId   dataId
     * @param group    group
     * @param consumer consumer
     */
    public static void addListenerAsProperties(String dataId, String group, Consumer<Properties> consumer) {
        addListener(dataId, group, (content) -> {
            Properties properties = new PropertiesConverter().convert(content, Properties.class);
            consumer.accept(properties);
        });
    }

    /**
     * @param dataId   dataId
     * @param group    group
     * @param consumer consumer
     */
    public static void addListener(String dataId, String group, Consumer<String> consumer) {
        try {
            instance().addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String content) {
                    logger.info("收到nacos配置变化：dataId: {}, group: {}, content: {}", dataId, group, content);
                    consumer.accept(content);
                }
            });
        } catch (NacosException e) {
            logger.error("监听nacos配置示例失败：{} - {}", dataId, group, e);
            throw new RuntimeException("监听nacos配置示例失败");
        }
    }
}

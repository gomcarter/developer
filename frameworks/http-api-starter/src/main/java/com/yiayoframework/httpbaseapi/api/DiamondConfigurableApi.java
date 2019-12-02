package com.yiayoframework.httpbaseapi.api;

import com.taobao.diamond.client.impl.DiamondEnv;
import com.taobao.diamond.common.Constants;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 李银 2018年2月28日 18:31:53
 */
public abstract class DiamondConfigurableApi extends BaseApi {

    private Map<String, Properties> propertiesMap = new HashMap<>();

    public abstract DiamondConfig getDiamondConfig();

    @Override
    protected Map<String, String> getUrlRouter() {
        Map<String, String> urlRouterMap = new HashMap<>();
        try {
            Properties properties = this.getProperties();
            for (Object key : properties.keySet()) {
                urlRouterMap.put((String) key, properties.getProperty((String) key));
            }
        } catch (IOException e) {
            logger.error("初始化接口地址失败：{}, {}",
                    getWhoami(), this.getDiamondConfig(), e);
        }

        return urlRouterMap;
    }

    synchronized private Properties getProperties() throws IOException {
        DiamondConfig config = getDiamondConfig();
        String dataId = config.getDataId(),
                group = config.getGroup(),
                key = dataId + "_" + group;
        Properties properties = propertiesMap.get(key);
        if (properties != null) {
            return properties;
        }

        properties = new Properties();
        DiamondEnv diamondEnv = DiamondEnv.instance();
        properties.load(new StringReader(
                diamondEnv.getConfig(dataId, group,
                        Constants.GETCONFIG_LOCAL_SERVER_SNAPSHOT, 20000)));

        propertiesMap.put(key, properties);
        return properties;
    }
}

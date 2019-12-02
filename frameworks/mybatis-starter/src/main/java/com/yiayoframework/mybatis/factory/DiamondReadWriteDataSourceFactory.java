package com.yiayoframework.mybatis.factory;

import com.taobao.diamond.client.impl.DiamondEnv;
import com.taobao.diamond.common.Constants;
import com.taobao.diamond.manager.impl.PropertiesListener;
import com.yiayoframework.mybatis.datasource.ReadWriteDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.StringReader;
import java.util.Properties;

/**
 * 完成从diamond读取mysql配置
 * 以及读库，主库故障转移
 * 基于druid实现，如果以后用别的数据库连接，需要重新实现
 *
 * @author 李银 on 2018年2月28日 11:26:07
 */
@Deprecated
public class DiamondReadWriteDataSourceFactory implements FactoryBean<ReadWriteDataSource>, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(DiamondReadWriteDataSourceFactory.class);

    private String dataId;
    private String group;

    private ReadWriteDataSource readWriteDataSource = new ReadWriteDataSource();

    @Override
    public ReadWriteDataSource getObject() throws Exception {
        return readWriteDataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return ReadWriteDataSource.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DiamondEnv diamondEnv = DiamondEnv.instance();

        diamondEnv.addListeners(this.dataId, this.group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                try {
                    initDatasource(properties);
                } catch (Exception e) {
                    logger.error("update datasource failed:", e);
                }
            }
        });

        Properties properties = new Properties();
        properties.load(new StringReader(diamondEnv.getConfig(this.dataId, this.group, Constants.GETCONFIG_LOCAL_SERVER_SNAPSHOT, 20000)));
        this.initDatasource(properties);
    }

    private void initDatasource(Properties properties) throws Exception {
        // 销毁之前的
        this.destroy();

        ReadWriteDataSourceBuilder.createDataSource(readWriteDataSource, properties);
    }

    @Override
    public void destroy() throws Exception {
        ReadWriteDataSourceBuilder.destroy(readWriteDataSource);
    }

    public DiamondReadWriteDataSourceFactory setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }

    public DiamondReadWriteDataSourceFactory setGroup(String group) {
        this.group = group;
        return this;
    }
}

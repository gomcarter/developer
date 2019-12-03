package com.gomcarter.frameworks.mybatis.factory;

import com.gomcarter.frameworks.base.common.NacosClientUtils;
import com.gomcarter.frameworks.mybatis.datasource.ReadWriteDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * 完成从nacos读取mysql配置
 * 以及读库，主库故障转移
 * 基于druid实现，如果以后用别的数据库连接，需要重新实现
 *
 * @author gomcarter on 2018年2月28日 11:26:07
 */
public class NacosReadWriteDataSourceFactory implements FactoryBean<ReadWriteDataSource>, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(NacosReadWriteDataSourceFactory.class);

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
        NacosClientUtils.addListenerAsProperties(dataId, group, (properties -> {
            try {
                this.initDatasource(properties);
            } catch (Exception e) {
                logger.error("update datasource failed:", e);
            }
        }));

        Properties properties = NacosClientUtils.getConfigAsProperties(this.dataId, this.group);
        this.initDatasource(properties);
    }

    private void initDatasource(Properties properties) throws Exception {
        // 销毁之前的
        this.destroy();

        ReadWriteDataSourceBuilder.createDataSource(this.readWriteDataSource, properties);
    }

    @Override
    public void destroy() throws Exception {
        ReadWriteDataSourceBuilder.destroy(readWriteDataSource);
    }

    public NacosReadWriteDataSourceFactory setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }

    public NacosReadWriteDataSourceFactory setGroup(String group) {
        this.group = group;
        return this;
    }
}

package com.gomcarter.frameworks.mybatis.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.gomcarter.frameworks.mybatis.datasource.ReadWriteDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author gomcarter on 2018年2月28日 11:26:07
 */
public class ReadWriteDataSourceBuilder {

    /**
     * @param readWriteDataSource readWriteDataSource
     * @param properties          properties
     * @return ReadWriteDataSource
     * @throws Exception Exception
     */
    public static ReadWriteDataSource createDataSource(ReadWriteDataSource readWriteDataSource, Properties properties) throws Exception {

        //初始化主库
        String writePrefix = "write", readPrefix = "read";
        DruidDataSource write = createDataSource(properties,
                writePrefix,
                properties.getProperty(writePrefix + ".jdbc.url"),
                properties.getProperty(writePrefix + ".jdbc.user"),
                properties.getProperty(writePrefix + ".jdbc.password")
        );

        readWriteDataSource.setWriteDataSource(write);

        String splitter = "\\|";
        String[] readUrls = properties.getProperty(readPrefix + ".jdbc.url").split(splitter),
                users = properties.getProperty(readPrefix + ".jdbc.user").split(splitter),
                passwords = properties.getProperty(readPrefix + ".jdbc.password").split(splitter);
        Map<String, DataSource> readDataSourceMap = new HashMap<>();
        for (int i = 0; i < readUrls.length; ++i) {
            readDataSourceMap.put("readDataSource" + i, createDataSource(properties, readPrefix, readUrls[i], users[i], passwords[i]));
        }
        readWriteDataSource.setReadDataSourceMap(readDataSourceMap);

        readWriteDataSource.afterPropertiesSet();

        return readWriteDataSource;
    }

    private static int valueOf(String value, int defaultValue) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static DruidDataSource createDataSource(Properties properties, String prefix, String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(valueOf(properties.getProperty(prefix + ".jdbc.initialSize"), 1));
        dataSource.setMaxActive(valueOf(properties.getProperty(prefix + ".jdbc.maxActive"), 80));
        dataSource.setMinIdle(valueOf(properties.getProperty(prefix + ".jdbc.minIdle"), 10));
        dataSource.setMaxWait(valueOf(properties.getProperty(prefix + ".jdbc.maxWait"), 60000));
        dataSource.setTestOnBorrow(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.testOnBorrow", "false")));
        dataSource.setTestOnReturn(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.testOnReturn", "false")));
        dataSource.setTestWhileIdle(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.testWhileIdle", "true")));
        dataSource.setTimeBetweenEvictionRunsMillis(valueOf(properties.getProperty(prefix + ".jdbc.timeBetweenEvictionRunsMillis"), 60000));
        dataSource.setMinEvictableIdleTimeMillis(valueOf(properties.getProperty(prefix + ".jdbc.minEvictableIdleTimeMillis"), 25200000));
        dataSource.setRemoveAbandoned(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.removeAbandoned", "true")));
        dataSource.setRemoveAbandonedTimeout(valueOf(properties.getProperty(prefix + ".jdbc.removeAbandonedTimeout"), 1800));
        dataSource.setLogAbandoned(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.logAbandoned", "true")));
        dataSource.setFilters(properties.getProperty(prefix + ".jdbc.filters"));

        String connectionInitSqls = properties.getProperty(prefix + ".jdbc.connectionInitSqls");
        if (connectionInitSqls != null) {
            dataSource.setConnectionInitSqls(Arrays.asList(connectionInitSqls.split("\\|")));
        }

        dataSource.init();

        return dataSource;
    }

    /**
     * @param dataSource dataSource
     * @throws Exception Exception
     */
    public static void destroy(ReadWriteDataSource dataSource) throws Exception {
        if (dataSource != null) {
            DataSource write = dataSource.getWriteDataSource();
            if (write instanceof DruidDataSource) {
                ((DruidDataSource) write).setBreakAfterAcquireFailure(true);
                ((DruidDataSource) write).close();
            }

            Map<String, DataSource> readDataSourceMap = dataSource.getReadDataSourceMap();
            if (readDataSourceMap != null) {
                readDataSourceMap.values().stream()
                        .filter(read -> read instanceof DruidDataSource)
                        .forEach(read -> {
                            ((DruidDataSource) read).setBreakAfterAcquireFailure(true);
                            ((DruidDataSource) read).close();
                        });
            }
        }
    }
}

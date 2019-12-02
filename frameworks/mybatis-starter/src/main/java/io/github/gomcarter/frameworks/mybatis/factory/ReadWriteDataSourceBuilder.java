package io.github.gomcarter.frameworks.mybatis.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import io.github.gomcarter.frameworks.base.common.CustomStringUtils;
import io.github.gomcarter.frameworks.mybatis.datasource.ReadWriteDataSource;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 完成从diamond读取mysql配置
 * 以及读库，主库故障转移
 * 基于druid实现，如果以后用别的数据库连接，需要重新实现
 *
 * @author gomcarter on 2018年2月28日 11:26:07
 */
public class ReadWriteDataSourceBuilder {
    public static ReadWriteDataSource createDataSource(ReadWriteDataSource readWriteDataSource, Properties properties) throws Exception {

        //初始化主库
        String writePrefix = "write", readPrefix = "read";
        DruidDataSource write = createDataSource(properties,
                writePrefix,
                properties.getProperty(writePrefix + ".jdbc.url"),
                properties.getProperty(writePrefix + ".jdbc.user"),
                properties.getProperty(writePrefix + ".jdbc.password")
        );
        String connectionInitSqls = properties.getProperty(writePrefix + ".jdbc.connectionInitSqls");
        if (StringUtils.isNotBlank(connectionInitSqls)) {
            write.setConnectionInitSqls(Lists.newArrayList(connectionInitSqls.split("\\|")));
        }

        readWriteDataSource.setWriteDataSource(write);

        String spitter = "|";
        String[] readUrls = StringUtils.split(properties.getProperty(readPrefix + ".jdbc.url"), spitter),
                users = StringUtils.split(properties.getProperty(readPrefix + ".jdbc.user"), spitter),
                passwords = StringUtils.split(properties.getProperty(readPrefix + ".jdbc.password"), spitter);
        Map<String, DataSource> readDataSourceMap = new HashMap<>();
        for (int i = 0; i < readUrls.length; ++i) {
            readDataSourceMap.put("readDataSource" + i, createDataSource(properties, readPrefix, readUrls[i], users[i], passwords[i]));
        }
        readWriteDataSource.setReadDataSourceMap(readDataSourceMap);

        readWriteDataSource.afterPropertiesSet();

        return readWriteDataSource;
    }

    private static DruidDataSource createDataSource(Properties properties, String prefix, String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.initialSize"), 1));
        dataSource.setMaxActive(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.maxActive"), 80));
        dataSource.setMinIdle(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.minIdle"), 10));
        dataSource.setMaxWait(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.maxWait"), 60000));
        dataSource.setTestOnBorrow(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.testOnBorrow", "false")));
        dataSource.setTestOnReturn(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.testOnReturn", "false")));
        dataSource.setTestWhileIdle(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.testWhileIdle", "true")));
        dataSource.setTimeBetweenEvictionRunsMillis(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.timeBetweenEvictionRunsMillis"), 60000));
        dataSource.setMinEvictableIdleTimeMillis(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.minEvictableIdleTimeMillis"), 25200000));
        dataSource.setRemoveAbandoned(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.removeAbandoned", "true")));
        dataSource.setRemoveAbandonedTimeout(CustomStringUtils.parseInt(properties.getProperty(prefix + ".jdbc.removeAbandonedTimeout"), 1800));
        dataSource.setLogAbandoned(Boolean.valueOf(properties.getProperty(prefix + ".jdbc.logAbandoned", "true")));
        dataSource.setFilters(properties.getProperty(prefix + ".jdbc.filters"));

        String connectionInitSqls = properties.getProperty(prefix + ".jdbc.connectionInitSqls");
        if (StringUtils.isNotBlank(connectionInitSqls)) {
            dataSource.setConnectionInitSqls(Lists.newArrayList(connectionInitSqls.split("\\|")));
        }

        dataSource.init();

        return dataSource;
    }

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

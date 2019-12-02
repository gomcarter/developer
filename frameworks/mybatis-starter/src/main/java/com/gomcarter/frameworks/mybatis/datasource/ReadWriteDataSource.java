package com.gomcarter.frameworks.mybatis.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 读/写动态选择数据库实现
 * 目前实现功能
 *   一写库多读库选择功能，请参考
 *      @see ReadWriteDataSourceDecision
 * @see ReadWriteDataSourceDecision.DataSourceType
 *
 *   默认按顺序轮询使用读库
 *   默认选择写库
 *
 *   已实现：一写多读、当写时默认读操作到写库、当写时强制读操作到写库
 *   TODO 读库负载均衡、读库故障转移
 * </pre>
 *
 * @author Zhang Kaitao
 */
public class ReadWriteDataSource extends AbstractDataSource implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ReadWriteDataSource.class);

    private DataSource writeDataSource;
    private Map<String, DataSource> readDataSourceMap;

    private String[] readDataSourceNames;
    private DataSource[] readDataSources;
    private int readDataSourceCount;

    private AtomicInteger counter = new AtomicInteger(1);


    /**
     * 设置读库（name, DataSource）
     *
     * @param readDataSourceMap
     */
    public void setReadDataSourceMap(Map<String, DataSource> readDataSourceMap) {
        this.readDataSourceMap = readDataSourceMap;
    }

    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public Map<String, DataSource> getReadDataSourceMap() {
        return readDataSourceMap;
    }

    public DataSource getWriteDataSource() {
        return writeDataSource;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (writeDataSource == null) {
            throw new IllegalArgumentException("property 'writeDataSource' is required");
        }
        if (CollectionUtils.isEmpty(readDataSourceMap)) {
            throw new IllegalArgumentException("property 'readDataSourceMap' is required");
        }
        readDataSourceCount = readDataSourceMap.size();

        readDataSources = new DataSource[readDataSourceCount];
        readDataSourceNames = new String[readDataSourceCount];

        int i = 0;
        for (Entry<String, DataSource> e : readDataSourceMap.entrySet()) {
            readDataSources[i] = e.getValue();
            readDataSourceNames[i] = e.getKey();
            i++;
        }
    }

    private DataSource determineDataSource() {
        if (ReadWriteDataSourceDecision.isChoiceWrite()) {
            log.debug("current determine write datasource");
            return writeDataSource;
        }

        if (ReadWriteDataSourceDecision.isChoiceNone()) {
            log.debug("no choice read/write, default determine write datasource");
            return writeDataSource;
        }
        return determineReadDataSource();
    }

    private DataSource determineReadDataSource() {
        //按照顺序选择读库
        //TODO 算法改进
        int index = counter.incrementAndGet() % readDataSourceCount;
        if (index < 0) {
            index = -index;
        }

        String dataSourceName = readDataSourceNames[index];

        log.debug("current determine read datasource : {}", dataSourceName);

        return readDataSources[index];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return determineDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineDataSource().getConnection(username, password);
    }
}

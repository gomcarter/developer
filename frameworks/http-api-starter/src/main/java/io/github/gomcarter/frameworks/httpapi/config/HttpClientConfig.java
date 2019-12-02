package io.github.gomcarter.frameworks.httpapi.config;

import java.util.Map;

/**
 * @author gomcarter on 2018年2月26日 10:52:20
 */
public class HttpClientConfig {

    public static final int DEFAULT_TIMEOUT = 30000;

    public static final int DEFAULT_CONNECT_TIMEOUT = 20000;

    private int soTimeout = DEFAULT_TIMEOUT;

    private int connectionTimeout = DEFAULT_CONNECT_TIMEOUT;

    private int retryCount = 1;

    /**
     * You can set this to custom specific host keep alive duration.
     */
    private Map<String, Long> hostToKeepAliveDuration;

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Map<String, Long> getHostToKeepAliveDuration() {
        return hostToKeepAliveDuration;
    }

    public void setHostToKeepAliveDuration(Map<String, Long> hostToKeepAliveDuration) {
        this.hostToKeepAliveDuration = hostToKeepAliveDuration;
    }
}

package it.gomcarter.frameworks.httpapi.config;

import org.apache.http.config.SocketConfig;

import java.util.Map;

/**
 * @author cn40387 on 15/5/8.
 */
public class ParamsConfig {

    public static final int DEFAULT_TIMEOUT = 5000;

    public static final int DEFAULT_CONNECTION_MAX_TOTAL = 1000;

    public static final int DEFAULT_CONNECTION_MAX_PER_ROUTE = 256;

    public static final long DEFAULT_KEEP_ALIVE_DURATION = -1; // 不keepalive

    public static final int DEFAULT_CHECK_IDLE_CONNECTION_INTERVAL = 5 * 1000; // 10s

    public static final int DEFAULT_CONNECTION_IDLE_DURATION_TO_CLOSE = 5 * 1000;

    private int soTimeout = DEFAULT_TIMEOUT;

    private boolean soKeepAlive = true;

    private boolean tcpNoDelay = true;

    private int sndBufSize;

    private int rcvBufSize;

    /**
     * DNS will be resolved to the setting ip.
     * When you want to mock external api call, you can set this.
     */
    private Map<String, String> dnsToIp;

    /**
     * When you want to custom some external http api config, you can set this.
     * Why we should do this?
     * 1. Maybe external server is so slow, so we have to set the timeout longer than default value.
     * 2. etc.
     */
    private Map<String, SocketConfig.Builder> hostToSocketConfig;

    /**
     * For security
     */
    private int maxLineLength = ConnectionConfig.DEFAULT_CONNECTION_MAX_LINE_LENGTH;

    /**
     * For security
     */
    private int maxHeaderCount = ConnectionConfig.DEFAULT_CONNECTION_MAX_HEADER_COUNT;

    private String connectionCharset = ConnectionConfig.DEFAULT_CHARSET;

    /**
     * You can set this to custom specific connection config, e.g. custom connection encode
     */
    private Map<String, ConnectionConfig> hostToConnectionConfig;

    private int connectionMaxTotal = DEFAULT_CONNECTION_MAX_TOTAL;

    private int connectionMaxPerRoute = DEFAULT_CONNECTION_MAX_PER_ROUTE;

    /**
     * You can set this to custom specific connection max connection count.
     */
    private Map<String, Integer> hostToRouteMaxConnection;

    private int checkIdleConnectionInterval = DEFAULT_CHECK_IDLE_CONNECTION_INTERVAL;

    private int connectionIdleDurationToClose = DEFAULT_CONNECTION_IDLE_DURATION_TO_CLOSE;

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public void setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getSndBufSize() {
        return sndBufSize;
    }

    public void setSndBufSize(int sndBufSize) {
        this.sndBufSize = sndBufSize;
    }

    public int getRcvBufSize() {
        return rcvBufSize;
    }

    public void setRcvBufSize(int rcvBufSize) {
        this.rcvBufSize = rcvBufSize;
    }

    public Map<String, String> getDnsToIp() {
        return dnsToIp;
    }

    public void setDnsToIp(Map<String, String> dnsToIp) {
        this.dnsToIp = dnsToIp;
    }

    public Map<String, SocketConfig.Builder> getHostToSocketConfig() {
        return hostToSocketConfig;
    }

    public void setHostToSocketConfig(Map<String, SocketConfig.Builder> hostToSocketConfig) {
        this.hostToSocketConfig = hostToSocketConfig;
    }

    public int getMaxLineLength() {
        return maxLineLength;
    }

    public void setMaxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public int getMaxHeaderCount() {
        return maxHeaderCount;
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public String getConnectionCharset() {
        return connectionCharset;
    }

    public void setConnectionCharset(String connectionCharset) {
        this.connectionCharset = connectionCharset;
    }

    public Map<String, ConnectionConfig> getHostToConnectionConfig() {
        return hostToConnectionConfig;
    }

    public void setHostToConnectionConfig(Map<String, ConnectionConfig> hostToConnectionConfig) {
        this.hostToConnectionConfig = hostToConnectionConfig;
    }

    public int getConnectionMaxTotal() {
        return connectionMaxTotal;
    }

    public void setConnectionMaxTotal(int connectionMaxTotal) {
        this.connectionMaxTotal = connectionMaxTotal;
    }

    public int getConnectionMaxPerRoute() {
        return connectionMaxPerRoute;
    }

    public void setConnectionMaxPerRoute(int connectionMaxPerRoute) {
        this.connectionMaxPerRoute = connectionMaxPerRoute;
    }

    public Map<String, Integer> getHostToRouteMaxConnection() {
        return hostToRouteMaxConnection;
    }

    public void setHostToRouteMaxConnection(Map<String, Integer> hostToRouteMaxConnection) {
        this.hostToRouteMaxConnection = hostToRouteMaxConnection;
    }

    public int getCheckIdleConnectionInterval() {
        return checkIdleConnectionInterval;
    }

    public void setCheckIdleConnectionInterval(int checkIdleConnectionInterval) {
        this.checkIdleConnectionInterval = checkIdleConnectionInterval;
    }

    public int getConnectionIdleDurationToClose() {
        return connectionIdleDurationToClose;
    }

    public void setConnectionIdleDurationToClose(int connectionIdleDurationToClose) {
        this.connectionIdleDurationToClose = connectionIdleDurationToClose;
    }
}

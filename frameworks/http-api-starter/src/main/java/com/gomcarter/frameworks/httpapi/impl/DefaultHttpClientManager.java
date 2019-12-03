package com.gomcarter.frameworks.httpapi.impl;

import com.gomcarter.frameworks.httpapi.HttpClientManager;
import com.gomcarter.frameworks.httpapi.config.ConnectionConfig;
import com.gomcarter.frameworks.httpapi.config.HttpClientConfig;
import com.gomcarter.frameworks.httpapi.config.ParamsConfig;
import com.gomcarter.frameworks.httpapi.utils.BasicThreadFactory;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author gomcarter
 */
public class DefaultHttpClientManager implements HttpClientManager {

    protected PoolingHttpClientConnectionManager connManager;
    private ScheduledExecutorService evictIdleConnectionScheduler;
    private ParamsConfig paramsConfig;

    public void init() {
        if (paramsConfig == null) {
            paramsConfig = new ParamsConfig();
        }

        connManager = buildConnectionManager();

        setSocketConfig(connManager);

        setConnectionConfig(connManager);

        setRouteConnection(connManager);

        this.evictIdleConnectionScheduler = Executors.newSingleThreadScheduledExecutor(
                new BasicThreadFactory("EvictIdleConnection"));

        this.evictIdleConnectionScheduler.scheduleAtFixedRate(new IdleConnectionMonitorTask(
                        connManager, paramsConfig.getConnectionIdleDurationToClose()),
                10 * 1000, paramsConfig.getCheckIdleConnectionInterval(), TimeUnit.MILLISECONDS);
    }

    private PoolingHttpClientConnectionManager buildConnectionManager() {
        DnsResolver dnsResolver = null;
        final Map<String, String> dnsToIp = paramsConfig.getDnsToIp();
        if (!MapUtils.isEmpty(dnsToIp)) {
            dnsResolver = new SystemDefaultDnsResolver() {

                @Override
                public InetAddress[] resolve(final String host) throws UnknownHostException {
                    String ip = dnsToIp.get(host.toLowerCase());

                    if (!StringUtils.isEmpty(ip)) {
                        try {
                            return new InetAddress[]{InetAddress.getByAddress(ip.getBytes("UTF-8"))};
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        return super.resolve(host);
                    }
                }

            };
        }

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                getSocketFactoryRegistry(), null, dnsResolver);

        return connManager;
    }

    protected Registry<ConnectionSocketFactory> getSocketFactoryRegistry() {
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        return socketFactoryRegistry;
    }

    private void setSocketConfig(PoolingHttpClientConnectionManager connManager) {
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(paramsConfig.isTcpNoDelay())
                .setSoTimeout(paramsConfig.getSoTimeout())
                .setSndBufSize(paramsConfig.getSndBufSize())
                .setRcvBufSize(paramsConfig.getRcvBufSize())
                .setSoKeepAlive(paramsConfig.isSoKeepAlive())
                .build();
        connManager.setDefaultSocketConfig(socketConfig);

        Map<String, SocketConfig.Builder> hostToSocketConfig = paramsConfig.getHostToSocketConfig();
        if (!MapUtils.isEmpty(hostToSocketConfig)) {
            for (Map.Entry<String, SocketConfig.Builder> entry : hostToSocketConfig.entrySet()) {
                connManager.setSocketConfig(HttpHost.create(entry.getKey()), entry.getValue().build());
            }
        }
    }

    private void setConnectionConfig(PoolingHttpClientConnectionManager connManager) {
        connManager.setDefaultConnectionConfig(buildConnectionConfig(paramsConfig.getMaxHeaderCount(),
                paramsConfig.getMaxLineLength(), paramsConfig.getConnectionCharset()));

        Map<String, ConnectionConfig> hostToConnectionConfig = paramsConfig.getHostToConnectionConfig();
        if (!MapUtils.isEmpty(hostToConnectionConfig)) {
            for (Map.Entry<String, ConnectionConfig> entry : hostToConnectionConfig.entrySet()) {
                ConnectionConfig connectionConfig = entry.getValue();

                connManager.setConnectionConfig(HttpHost.create(entry.getKey()),
                        buildConnectionConfig(connectionConfig.getMaxHeaderCount(),
                                connectionConfig.getMaxLineLength(),
                                connectionConfig.getConnectionCharset()));
            }
        }
    }

    private org.apache.http.config.ConnectionConfig buildConnectionConfig(int maxHeaderCount, int maxLineLength, String connectionCharset) {
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(maxHeaderCount)
                .setMaxLineLength(maxLineLength)
                .build();
        org.apache.http.config.ConnectionConfig connectionConfig = org.apache.http.config.ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Charset.forName(connectionCharset))
                .setMessageConstraints(messageConstraints)
                .build();

        return connectionConfig;
    }

    private void setRouteConnection(PoolingHttpClientConnectionManager connManager) {
        connManager.setMaxTotal(paramsConfig.getConnectionMaxTotal());
        connManager.setDefaultMaxPerRoute(paramsConfig.getConnectionMaxPerRoute());

        Map<String, Integer> hostToRouteMaxConnection = paramsConfig.getHostToRouteMaxConnection();
        if (!MapUtils.isEmpty(hostToRouteMaxConnection)) {
            for (Map.Entry<String, Integer> entry : hostToRouteMaxConnection.entrySet()) {
                connManager.setMaxPerRoute(new HttpRoute(HttpHost.create(entry.getKey())), entry.getValue());
            }
        }
    }

    @Override
    public CloseableHttpClient getHttpClient(HttpClientConfig httpClientConfig,
                                             HttpRequestRetryHandler httpRequestRetryHandler,
                                             ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setConnectTimeout(httpClientConfig.getConnectionTimeout())
                .setSocketTimeout(httpClientConfig.getSoTimeout())
                .build();

        CloseableHttpClient httpClientLocal = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setRetryHandler(httpRequestRetryHandler)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .build();

        return httpClientLocal;
    }

    public void destroy() {
        if (evictIdleConnectionScheduler != null) {
            evictIdleConnectionScheduler.shutdownNow();
        }
    }

    public ParamsConfig getParamsConfig() {
        return paramsConfig;
    }

    public void setParamsConfig(ParamsConfig paramsConfig) {
        this.paramsConfig = paramsConfig;
    }
}

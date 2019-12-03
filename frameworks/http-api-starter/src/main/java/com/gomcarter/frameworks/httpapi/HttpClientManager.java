package com.gomcarter.frameworks.httpapi;

import com.gomcarter.frameworks.httpapi.config.HttpClientConfig;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author gomcarter
 */
public interface HttpClientManager {

    /**
     * @param httpClientConfig            httpClientConfig
     * @param httpRequestRetryHandler     httpRequestRetryHandler
     * @param connectionKeepAliveStrategy connectionKeepAliveStrategy
     * @return CloseableHttpClient
     */
    CloseableHttpClient getHttpClient(HttpClientConfig httpClientConfig,
                                      HttpRequestRetryHandler httpRequestRetryHandler,
                                      ConnectionKeepAliveStrategy connectionKeepAliveStrategy);
}

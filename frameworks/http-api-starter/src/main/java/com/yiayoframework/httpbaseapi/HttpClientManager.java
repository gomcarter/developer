package com.yiayoframework.httpbaseapi;

import com.yiayoframework.httpbaseapi.config.HttpClientConfig;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author cn40387 on 15/5/8.
 */
public interface HttpClientManager {

    CloseableHttpClient getHttpClient(HttpClientConfig httpClientConfig,
                                      HttpRequestRetryHandler httpRequestRetryHandler,
                                      ConnectionKeepAliveStrategy connectionKeepAliveStrategy);
}

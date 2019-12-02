package it.gomcarter.frameworks.httpapi.impl;

import it.gomcarter.frameworks.httpapi.config.ParamsConfig;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

import java.util.Map;

/**
 * @author cn40387 on 15/5/8.
 */
public class BasicConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {

    private Map<String, Long> hostToKeepAliveDuration;

    public BasicConnectionKeepAliveStrategy() {
        super();
    }

    public BasicConnectionKeepAliveStrategy(Map<String, Long> hostToKeepAliveDuration) {
        super();
        this.hostToKeepAliveDuration = hostToKeepAliveDuration;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        long duration = super.getKeepAliveDuration(response, context);

        if (duration != -1) {
            return duration;
        }

        if (!MapUtils.isEmpty(hostToKeepAliveDuration)) {
            HttpHost target = (HttpHost) context.getAttribute(
                    HttpClientContext.HTTP_TARGET_HOST);
            Long hostDuration = hostToKeepAliveDuration.get(target.getHostName().toLowerCase());

            if (hostDuration != null) {
                return hostDuration;
            }
        }

        return ParamsConfig.DEFAULT_KEEP_ALIVE_DURATION;
    }

    public Map<String, Long> getHostToKeepAliveDuration() {
        return hostToKeepAliveDuration;
    }

    public void setHostToKeepAliveDuration(Map<String, Long> hostToKeepAliveDuration) {
        this.hostToKeepAliveDuration = hostToKeepAliveDuration;
    }
}

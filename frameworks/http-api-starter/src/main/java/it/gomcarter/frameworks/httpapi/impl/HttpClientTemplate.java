package it.gomcarter.frameworks.httpapi.impl;

import it.gomcarter.frameworks.httpapi.HttpClientManager;
import it.gomcarter.frameworks.httpapi.config.HttpClientConfig;
import it.gomcarter.frameworks.httpapi.impl.handler.DefaultResponseHandler;
import it.gomcarter.frameworks.httpapi.message.MessageConfig;
import it.gomcarter.frameworks.httpapi.message.request.PostRequestMessage;
import it.gomcarter.frameworks.httpapi.message.request.RequestMessage;
import it.gomcarter.frameworks.httpapi.message.request.RestfulRequestMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author cn40387 on 15/5/11.
 */
public class HttpClientTemplate {

    private HttpClientManager httpClientManager;

    private HttpClientConfig httpClientConfig;

    private HttpRequestRetryHandler httpRequestRetryHandler;

    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;

    private CloseableHttpClient httpClient;

    /**
     * Key: Api name
     * Value: MessageConfig
     */
    private Map<String, MessageConfig> requestRouter = new HashMap<>();

    /**
     * Key: Api name
     * Value: host url
     */
    private Map<String, String> urlRequestRouter = new HashMap<>();

    public void init() {
        if (httpClientConfig == null) {
            httpClientConfig = new HttpClientConfig();
        }
        if (httpRequestRetryHandler == null) {
            httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(httpClientConfig.getRetryCount(), true);
        }
        if (connectionKeepAliveStrategy == null) {
            connectionKeepAliveStrategy = new BasicConnectionKeepAliveStrategy(httpClientConfig.getHostToKeepAliveDuration());
        }

        httpClient = httpClientManager.getHttpClient(httpClientConfig, httpRequestRetryHandler, connectionKeepAliveStrategy);
    }

    public void destroy() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String executeGet(RequestMessage requestMessage) throws IOException, URISyntaxException {
        return executeGet(requestMessage, new DefaultResponseHandler());
    }

    public String executePost(PostRequestMessage requestMessage) throws IOException, URISyntaxException {
        return executePost(requestMessage, new DefaultResponseHandler());
    }

    public <T> T executeGet(RequestMessage requestMessage, ResponseHandler<T> responseHandler)
            throws URISyntaxException, IOException {
        HttpGet httpGet = (HttpGet) buildHttpRequest(requestMessage, HttpGet.METHOD_NAME);

        for (Map.Entry<String, String> entry : requestMessage.getHeaders().entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }

            httpGet.addHeader(entry.getKey(), entry.getValue());
        }

        return executeGet(httpGet, responseHandler);
    }

    public <T> T executePut(RequestMessage requestMessage, ResponseHandler<T> responseHandler)
            throws URISyntaxException, IOException {
        HttpPut httpPut = (HttpPut) buildHttpRequest(requestMessage, HttpPut.METHOD_NAME);

        for (Map.Entry<String, String> entry : requestMessage.getHeaders().entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }

            httpPut.addHeader(entry.getKey(), entry.getValue());
        }

        return executePut(httpPut, responseHandler);
    }

    public <T> T executePost(PostRequestMessage requestMessage, ResponseHandler<T> responseHandler)
            throws URISyntaxException, IOException {
        HttpPost httpPost = (HttpPost) buildHttpRequest(requestMessage, HttpPost.METHOD_NAME);

        for (Map.Entry<String, String> entry : requestMessage.getHeaders().entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }

            httpPost.addHeader(entry.getKey(), entry.getValue());
        }

        HttpEntity httpEntity = buildHttpEntity(requestMessage);
        if (httpEntity != null) {
            httpPost.setEntity(httpEntity);
            httpPost.setHeader(httpEntity.getContentType());
        }

        return executePost(httpPost, responseHandler);
    }

    private HttpEntity buildHttpEntity(PostRequestMessage requestMessage) throws UnsupportedEncodingException {
        if (isNeedMultipart(requestMessage)) {
            return buildMultipartEntity(requestMessage);
        } else {
            return buildSimpleEntity(requestMessage);
        }
    }

    private HttpEntity buildMultipartEntity(PostRequestMessage requestMessage) throws UnsupportedEncodingException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (!MapUtils.isEmpty(requestMessage.getParameters())) {
            for (Map.Entry<String, Set<String>> entry : requestMessage.getParameters().entrySet()) {
                for (String value : entry.getValue()) {
                    builder.addPart(entry.getKey(), new StringBody(value, requestMessage.getContentType()));
                }
            }
        }

        if (!StringUtils.isEmpty(requestMessage.getBody())) {
            builder.addPart(requestMessage.getBodyPartName(), new StringBody(requestMessage.getBody(), requestMessage.getContentType()));
        }

        if (!MapUtils.isEmpty(requestMessage.getFiles())) {
            for (Map.Entry<String, File> entry : requestMessage.getFiles().entrySet()) {
                builder.addPart(entry.getKey(), new FileBody(entry.getValue(), requestMessage.getFileContentType()));
            }
        }

        return builder.build();
    }

    private HttpEntity buildSimpleEntity(PostRequestMessage requestMessage) throws UnsupportedEncodingException {
        if (!MapUtils.isEmpty(requestMessage.getParameters())) {
            return buildParametersEntity(requestMessage);
        }

        if (!StringUtils.isEmpty(requestMessage.getBody())) {
            return buildBodyEntity(requestMessage);
        }

        if (!MapUtils.isEmpty(requestMessage.getFiles())) {
            return buildFileEntity(requestMessage);
        }

        return null;
    }

    private HttpEntity buildParametersEntity(PostRequestMessage requestMessage) {
        List<BasicNameValuePair> params = new ArrayList<>(requestMessage.getParameters().size());

        for (Map.Entry<String, Set<String>> entry : requestMessage.getParameters().entrySet()) {
            if (entry.getKey() == null || CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }

            for (String value : entry.getValue()) {
                params.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        return new UrlEncodedFormEntity(params, requestMessage.getCharset());
    }

    private HttpEntity buildBodyEntity(PostRequestMessage requestMessage) {
        return new StringEntity(requestMessage.getBody(), requestMessage.getContentType());
    }

    private HttpEntity buildFileEntity(PostRequestMessage requestMessage) {
        return new FileEntity(requestMessage.getFiles().values().iterator().next());
    }

    private boolean isNeedMultipart(PostRequestMessage requestMessage) {
        if (!MapUtils.isEmpty(requestMessage.getFiles())) {
            return true;
        }

        int bodyPartNum = 0;

        if (!StringUtils.isEmpty(requestMessage.getBody())) {
            bodyPartNum++;
        }

        if (!MapUtils.isEmpty(requestMessage.getParameters())) {
            bodyPartNum++;
        }

        return bodyPartNum > 1;
    }

    public <T> T executeGet(HttpGet httpGet, ResponseHandler<T> responseHandler) throws IOException {
        return httpClient.execute(httpGet, responseHandler);
    }

    public <T> T executePost(HttpPost httpPost, ResponseHandler<T> responseHandler) throws IOException {
        return httpClient.execute(httpPost, responseHandler);
    }

    public <T> T executePut(HttpPut httpPut, ResponseHandler<T> responseHandler) throws IOException {
        return httpClient.execute(httpPut, responseHandler);
    }

    private String replacePlaceHolders(RestfulRequestMessage requestMessage, String apiPath) {
        if (!CollectionUtils.isEmpty(requestMessage.getRestParameters())) {
            apiPath = String.format(apiPath, requestMessage.getRestParameters().toArray());
        }

        return apiPath;
    }

    private HttpRequestBase buildHttpRequest(RequestMessage requestMessage, String methodName) throws URISyntaxException {
        MessageConfig messageConfig = requestRouter.get(requestMessage.getApiName());

        URIBuilder uriBuilder = null;
        if (messageConfig != null) {
            String path = replacePlaceHolders(requestMessage, messageConfig.getApiPath());

            uriBuilder = new URIBuilder().setScheme(messageConfig.getUrlSchema())
                    .setHost(messageConfig.getHost())
                    .setPort(messageConfig.getPort())
                    .setPath(path);
        } else {
            String url = urlRequestRouter.get(requestMessage.getApiName());
            if (url != null) {
                url = replacePlaceHolders(requestMessage, url);
                uriBuilder = new URIBuilder(URI.create(url));
            }
        }

        if (uriBuilder == null) {
            throw new IllegalArgumentException("No message config for api " + requestMessage.getApiName());
        }

        return buildMethod(uriBuilder, requestMessage, methodName);
    }

    private HttpRequestBase buildMethod(URIBuilder uriBuilder, RequestMessage requestMessage, String methodName) throws URISyntaxException {
        if (HttpGet.METHOD_NAME.equals(methodName)) {
            for (Map.Entry<String, Set<String>> entry : requestMessage.getParameters().entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }

                String value = StringUtils.join(entry.getValue(), ",");
                uriBuilder.setParameter(entry.getKey(), value);
            }

            return new HttpGet(uriBuilder.build());
        }

        if (HttpPost.METHOD_NAME.equals(methodName)) {
            return new HttpPost(uriBuilder.build());
        }

        if (HttpPut.METHOD_NAME.equals(methodName)) {
            return new HttpPut(uriBuilder.build());
        }

        throw new IllegalArgumentException("Not support method " + methodName);
    }

    public HttpClientManager getHttpClientManager() {
        return httpClientManager;
    }

    public void setHttpClientManager(HttpClientManager httpClientManager) {
        this.httpClientManager = httpClientManager;
    }

    public Map<String, MessageConfig> getRequestRouter() {
        return requestRouter;
    }

    public void setRequestRouter(Map<String, MessageConfig> requestRouter) {
        this.requestRouter = requestRouter;
    }

    public Map<String, String> getUrlRequestRouter() {
        return urlRequestRouter;
    }

    public void setUrlRequestRouter(Map<String, String> urlRequestRouter) {
        this.urlRequestRouter = urlRequestRouter;
    }

    public HttpClientConfig getHttpClientConfig() {
        return httpClientConfig;
    }

    public void setHttpClientConfig(HttpClientConfig httpClientConfig) {
        this.httpClientConfig = httpClientConfig;
    }

    public HttpRequestRetryHandler getHttpRequestRetryHandler() {
        return httpRequestRetryHandler;
    }

    public void setHttpRequestRetryHandler(HttpRequestRetryHandler httpRequestRetryHandler) {
        this.httpRequestRetryHandler = httpRequestRetryHandler;
    }

    public ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        return connectionKeepAliveStrategy;
    }

    public void setConnectionKeepAliveStrategy(ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        this.connectionKeepAliveStrategy = connectionKeepAliveStrategy;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void appendUrlRequestRouter(String urlKey, String url) {
        this.urlRequestRouter.put(urlKey, url);
    }
}

package io.github.gomcarter.frameworks.httpapi.api;

import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.gomcarter.frameworks.httpapi.auth.ApiAuthStrategy;
import io.github.gomcarter.frameworks.httpapi.auth.AuthStrategy;
import io.github.gomcarter.frameworks.httpapi.impl.DefaultHttpClientManager;
import io.github.gomcarter.frameworks.httpapi.impl.HttpClientTemplate;
import io.github.gomcarter.frameworks.httpapi.impl.handler.DefaultResponseHandler;
import io.github.gomcarter.frameworks.httpapi.message.request.PostRequestMessage;
import io.github.gomcarter.frameworks.httpapi.message.request.RequestMessage;
import io.github.gomcarter.frameworks.httpapi.request.GetRequest;
import io.github.gomcarter.frameworks.httpapi.request.PostRequest;
import io.github.gomcarter.frameworks.base.json.JsonTData;
import io.github.gomcarter.frameworks.base.mapper.JsonMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仅只能处理系统内部接口调用，因为这里会统一处理返回结果为JsonObject的对象；
 * 如果需要用到外部调用，请另行封装
 * <p>
 * 如果需要扩展： 找  gomcarter
 */
public abstract class BaseApi implements DisposableBean, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected JsonMapper baseApiJsonMapper = JsonMapper.buildNotNullMapper();

    protected TypeFactory typeFactory = TypeFactory.defaultInstance();

    protected HttpClientTemplate httpClientTemplate;

    protected AuthStrategy authStrategy = new ApiAuthStrategy();

    private String whoami = System.getProperty("processName");

    public String postWithFile(String urlKey, Map<String, Object> params, Map<String, String> headers, Map<String, File> files) {
        return this.httpExcute(Method.POST, urlKey, params, null, null, headers, files);
    }

    public enum Method {
        GET, POST
    }

    public void init() {
        if (httpClientTemplate == null) {
            httpClientTemplate = new HttpClientTemplate();
        }

        if (httpClientTemplate.getHttpClientManager() == null) {
            DefaultHttpClientManager httpClientManager = new DefaultHttpClientManager();
            httpClientManager.init();

            this.httpClientTemplate.setHttpClientManager(httpClientManager);
        }

        httpClientTemplate.setUrlRequestRouter(getUrlRouter());

        httpClientTemplate.init();
    }

    protected abstract Map<String, String> getUrlRouter();


    /********************
     * 以下POST方法
     ********************/
    protected String postWithBody(String urlKey, String body, Map<String, String> headers) {
        return this.httpExcute(Method.POST, urlKey, null, null, body, headers, null);
    }

    protected <T> T postWithBody(String urlKey, Class<T> kls, String body, Map<String, String> headers) {
        return this.post(urlKey, kls, null, null, headers, body);
    }

    protected String postWithBody(String urlKey, List<String> restParams, String body, Map<String, String> headers) {
        return this.httpExcute(Method.POST, urlKey, null, restParams, body, headers, null);
    }

    protected <T> List<T> postList(String urlKey, Class<T> kls) {
        return this.postList(urlKey, kls, null, null);
    }

    protected <T> List<T> postList(String urlKey, Class<T> kls, Map<String, Object> params) {
        return this.postList(urlKey, kls, params, null);
    }

    protected <T> List<T> postList(String urlKey, Class<T> kls, Map<String, Object> params, Map<String, String> headers) {
        String r = this.httpExcute(Method.POST, urlKey, params, headers);

        JsonTData<List<T>> data = baseApiJsonMapper.fromJson(r, typeFactory.constructParametricType(JsonTData.class,
                typeFactory.constructParametricType(List.class, kls)));

        if (!data.isSuccess()) {
            throw new RuntimeException("接口调用失败：" + data.getMessage());
        }
        return data.getExtra();
    }

    protected <T> T post(String urlKey, Class<T> kls) {
        return this.post(urlKey, kls, null, null, null, null);
    }

    protected <T> T post(String urlKey, Class<T> kls, Map<String, Object> params) {
        return this.post(urlKey, kls, params, null, null, null);
    }

    protected <T> T post(String urlKey, Class<T> kls, List<String> restParams) {
        return this.post(urlKey, kls, null, restParams, null, null);
    }

    protected <T> T post(String urlKey, Class<T> kls,  Map<String, Object> params,  Map<String, String> header) {
        return this.post(urlKey, kls, params, null, header, null);
    }

    protected <T> T post(String urlKey, Class<T> kls, Map<String, Object> params, List<String> restParams, Map<String, String> headers, String body) {
        String r = this.httpExcute(Method.POST, urlKey, params, restParams, body, headers, null);

        JsonTData<T> data = baseApiJsonMapper.fromJson(r, typeFactory.constructParametricType(JsonTData.class, typeFactory.constructType(kls)));

        if (!data.isSuccess()) {
            throw new RuntimeException("接口调用失败：" + data.getMessage());
        }

        return data.getExtra();
    }

    /********************
     * 以下GET方法
     ********************/
    protected <T> List<T> getList(String urlKey, Class<T> kls) {
        return this.getList(urlKey, kls, null, null);
    }

    protected <T> List<T> getList(String urlKey, Class<T> kls, Map<String, Object> params) {
        return this.getList(urlKey, kls, params, null);
    }

    protected <T> List<T> getList(String urlKey, Class<T> kls, Map<String, Object> params, Map<String, String> headers) {
        String r = this.httpExcute(Method.GET, urlKey, params, headers);

        JsonTData<List<T>> data = baseApiJsonMapper.fromJson(r, typeFactory.constructParametricType(JsonTData.class,
                typeFactory.constructParametricType(List.class, kls)));

        if (!data.isSuccess()) {
            throw new RuntimeException("接口调用失败：" + data.getMessage());
        }
        return data.getExtra();
    }

    protected <T> T get(String urlKey, Class<T> kls) {
        return this.get(urlKey, kls, null, null);
    }

    protected <T> T get(String urlKey, Class<T> kls, Map<String, Object> params) {
        return this.get(urlKey, kls, params, null);
    }

    protected <T> T get(String urlKey, Class<T> kls, Map<String, Object> params, Map<String, String> headers) {
        String r = this.httpExcute(Method.GET, urlKey, params, headers);


        JsonTData<T> data = baseApiJsonMapper.fromJson(r, typeFactory.constructParametricType(JsonTData.class, typeFactory.constructType(kls)));

        if (!data.isSuccess()) {
            throw new RuntimeException("接口调用失败：" + data.getMessage());
        }

        return data.getExtra();
    }

    protected <T> T httpExcute(Method method, String urlKey, Class<T> kls, Map<String, Object> params, Map<String, String> headers) {
        return baseApiJsonMapper.fromJson(this.httpExcute(method, urlKey, params, headers), kls);
    }

    protected String httpExcute(Method method, String urlKey) {
        return this.httpExcute(method, urlKey, null, null);
    }

    protected String httpExcute(Method method, String urlKey, Map<String, Object> params) {
        return this.httpExcute(method, urlKey, params, null);
    }

    protected String httpExcute(Method method, String urlKey, Map<String, Object> params, Map<String, String> headers) {
        return this.httpExcute(method, urlKey, params, null, null, headers, null);
    }

    /**
     * @param method  POST OR GET
     * @param urlKey
     * @param params
     * @param headers
     * @return String
     */
    public String httpExcute(Method method, String urlKey, Map<String, Object> params, List<String> restParams, String body, Map<String, String> headers, Map<String, File> files) {
        params = ObjectUtils.defaultIfNull(params, new HashMap<>());
        logger.info("{}调用了接口：{}，参数 ：{}, {}，{}",
                this.whoami,
                httpClientTemplate.getUrlRequestRouter().get(urlKey),
                baseApiJsonMapper.toJson(params),
                body,
                headers);
        long start = System.currentTimeMillis();

        String result = null;
        try {
            if (method == Method.GET) {

                /*初始化参数，url，api公有认证headers*/
                RequestMessage message = authStrategy.init(new GetRequest(urlKey).addAllParams(params).getRequestMessage());

                /*额外headers*/
                addHeaders(message, headers);

                result = httpClientTemplate.executeGet(message, new DefaultResponseHandler());
            } else if (method == Method.POST) {
                PostRequestMessage message = new PostRequest<>(urlKey).addAllParams(params).getPostRequestMessage();

                if (StringUtils.isNotBlank(body)) {
                    message.setBody(body);
                }

                if (null != restParams) {
                    message.setRestParameters(restParams);
                }

                if (files != null && !files.isEmpty()) {
                    message.setFiles(files);
                }

                authStrategy.init(message);

                addHeaders(message, headers);

                result = httpClientTemplate.executePost(message, new DefaultResponseHandler());
            }
        } catch (Exception e) {
            logger.error("接口调用失败，用时：{}", System.currentTimeMillis() - start, e);

            throw new RuntimeException("接口调用失败！");
        }

        logger.info("调用结果：{}，用时：{}", result, System.currentTimeMillis() - start);
        return result;
    }

    private void addHeaders(RequestMessage message, Map<String, String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                message.addHeader(key, headers.get(key));
            }
        }
    }

    ///可以自定义httpclientTemplate注入
    public void setHttpClientTemplate(HttpClientTemplate httpClientTemplate) {
        this.httpClientTemplate = httpClientTemplate;
    }

    public void appendUrlRequestRouter(String urlKey, String url) {
        this.httpClientTemplate.appendUrlRequestRouter(urlKey, url);
    }

    public BaseApi setWhoami(String whoami) {
        this.whoami = whoami;
        return this;
    }

    public String getWhoami() {
        return whoami;
    }

    @Override
    public void destroy() throws Exception {
        this.httpClientTemplate.destroy();
        logger.info("api destroyed");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
}

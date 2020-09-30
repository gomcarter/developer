package com.gomcarter.developer.publics;

import com.fasterxml.jackson.databind.JavaType;
import com.gomcarter.developer.params.Xhr;
import com.gomcarter.frameworks.config.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("publics/xhr")
@Slf4j
public class PublicsXhrController {

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static JsonMapper mapper = JsonMapper.buildNonNullMapper();
    private static JavaType mapJavaType = mapper.constructParametricType(Map.class, String.class, String.class);

    private static class Holder {
        private static OkHttpClient client;

        static {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder().addInterceptor(loggingInterceptor);
            client = builder.build();
        }
    }

    /**
     * 生成最终使用的url
     *
     * @param url    业务url
     * @param params 自有参数
     * @return 最终使用的接口url
     */
    public static String generateUrl(String url, String params) {
        if (StringUtils.isBlank(params)) {
            return url;
        }

        Map<String, Object> paramsMap = mapper.fromJson(params, Map.class);
        List<String> paramsList = new ArrayList<>();
        paramsMap.forEach((k, v) -> {
            if (v instanceof Iterable) {
                ((Iterable) v).forEach(l -> paramsList.add(k + "=" + l));
            } else {
                paramsList.add(k + "=" + v);
            }
        });

        if (StringUtils.contains(url, "\\?")) {
            return url + "&" + StringUtils.join(paramsList, "&");
        } else {
            return url + "?" + StringUtils.join(paramsList, "&");
        }
    }


    @PostMapping(value = "", name = "发送http接口")
    Object xhr(@org.springframework.web.bind.annotation.RequestBody Xhr xhr) {
        String url = generateUrl(xhr.getUrl(), xhr.getParams());

        Request.Builder requestBuilder = new Request.Builder().url(url);
        // 生成headers
        if (xhr.getHeaders() != null) {
            Map<String, String> headersMap = mapper.fromJson(xhr.getHeaders(), mapJavaType);
            headersMap.forEach(requestBuilder::addHeader);
        }

        String method = xhr.getMethod().toUpperCase();
        String body = xhr.getBody();
        if (RequestMethod.PATCH.name().equals(method)
                || RequestMethod.PUT.name().equals(method)
                || RequestMethod.POST.name().equals(method)) {
            requestBuilder.method(method, RequestBody.create(StringUtils.defaultIfBlank(body, ""),
                    MediaType.parse("application/json")));
        } else {
            requestBuilder.method(method, null);
        }

        try {
            OkHttpClient client = Holder.client;
            ResponseBody response = client.newCall(requestBuilder.build()).execute().body();
            String result = response == null ? null : response.string();

            log.info("调用接口：{}，结果：{}", xhr, result);

            return ObjectUtils.defaultIfNull(mapper.fromJson(result, Object.class), result);
        } catch (Exception e) {
            log.error("请求接口失败：{}", xhr, e);
            throw new RuntimeException(e);
        }
    }
}

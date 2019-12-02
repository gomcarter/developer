package com.yiayoframework.httpbaseapi.request;

import com.yiayoframework.base.mapper.JsonMapper;
import com.yiayoframework.httpbaseapi.message.request.PostRequestMessage;
import com.yiayoframework.httpbaseapi.utils.RequestTool;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author 李银 on 2019-11-13 10:27:42
 */
public class CpsPostRequest<T> implements CpsApiRequest {

    private PostRequestMessage postRequestMessage;

    private T message;

    public CpsPostRequest(String apiName) {
        this(apiName, null);
    }

    public CpsPostRequest(String apiName, T message) {
        postRequestMessage = new PostRequestMessage(apiName, ContentType.APPLICATION_JSON, Charset.forName("UTF-8"));
        if (message != null) {
            setMessage(message);
        }
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
        setMessageBody();
    }

    public PostRequestMessage getPostRequestMessage() {
        return postRequestMessage;
    }

    private CpsPostRequest setMessageBody() {
        postRequestMessage.setBody(JsonMapper.buildNonNullMapper().toJson(message));
        return this;
    }

    @Override
    public CpsPostRequest addHeader(String name, String value) {
        postRequestMessage.addHeader(name, value);
        return this;
    }

    @Override
    public CpsPostRequest addParameter(String name, String value) {
        postRequestMessage.addParameter(name, value);
        return this;
    }

    public CpsPostRequest addFiles(Map<String, File> fileMap) {
        postRequestMessage.setFiles(fileMap);
        return this;
    }

    public CpsPostRequest addParameter(String name, Object value) {
        RequestTool.addParameter(postRequestMessage, name, value);
        return this;
    }

    public CpsPostRequest addAllParams(Map<String, Object> params) {
        for (String key : params.keySet()) {
            this.addParameter(key, params.get(key));
        }
        return this;
    }
}

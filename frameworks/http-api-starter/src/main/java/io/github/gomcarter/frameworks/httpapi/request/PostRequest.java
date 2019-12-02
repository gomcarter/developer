package io.github.gomcarter.frameworks.httpapi.request;

import io.github.gomcarter.frameworks.httpapi.message.request.PostRequestMessage;
import io.github.gomcarter.frameworks.httpapi.utils.RequestTool;
import io.github.gomcarter.frameworks.base.mapper.JsonMapper;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author gomcarter on 2019-11-13 10:27:42
 */
public class PostRequest<T> implements ApiRequest {

    private PostRequestMessage postRequestMessage;

    private T message;

    public PostRequest(String apiName) {
        this(apiName, null);
    }

    public PostRequest(String apiName, T message) {
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

    private PostRequest setMessageBody() {
        postRequestMessage.setBody(JsonMapper.buildNonNullMapper().toJson(message));
        return this;
    }

    @Override
    public PostRequest addHeader(String name, String value) {
        postRequestMessage.addHeader(name, value);
        return this;
    }

    @Override
    public PostRequest addParameter(String name, String value) {
        postRequestMessage.addParameter(name, value);
        return this;
    }

    public PostRequest addFiles(Map<String, File> fileMap) {
        postRequestMessage.setFiles(fileMap);
        return this;
    }

    public PostRequest addParameter(String name, Object value) {
        RequestTool.addParameter(postRequestMessage, name, value);
        return this;
    }

    public PostRequest addAllParams(Map<String, Object> params) {
        for (String key : params.keySet()) {
            this.addParameter(key, params.get(key));
        }
        return this;
    }
}

package com.gomcarter.frameworks.httpapi.message.request;

import org.apache.http.entity.ContentType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author gomcarter
 */
public class RequestMessage extends RestfulRequestMessage {

    private String apiName;

    private ContentType contentType;

    private Map<String, String> headers = new HashMap<String, String>();

    private Map<String, Set<String>> parameters = new HashMap<>();

    public RequestMessage(String apiName, ContentType contentType) {
        super();
        this.apiName = apiName;
        this.contentType = contentType == null ? ContentType.APPLICATION_JSON : contentType;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public Map<String, Set<String>> getParameters() {
        return parameters;
    }

    public void addParameter(String name, String value) {
        Set<String> values = parameters.computeIfAbsent(name, k -> new HashSet<>());

        values.add(value);
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}

package com.gomcarter.frameworks.httpapi.request;

import com.gomcarter.frameworks.httpapi.message.request.RequestMessage;
import com.gomcarter.frameworks.httpapi.utils.RequestTool;
import org.apache.http.entity.ContentType;

import java.util.Map;

/**
 * @author gomcarter on 2019-11-13 10:27:42
 */
public class GetRequest implements ApiRequest {

    private RequestMessage requestMessage;

    public GetRequest(String apiName) {
        requestMessage = new RequestMessage(apiName, ContentType.APPLICATION_JSON);
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }

    @Override
    public GetRequest addHeader(String name, String value) {
        requestMessage.addHeader(name, value);
        return this;
    }

    @Override
    public GetRequest addParameter(String name, String value) {
        requestMessage.addParameter(name, value);
        return this;
    }

    public GetRequest addParameter(String name, Object value) {
        RequestTool.addParameter(requestMessage, name, value);
        return this;
    }

    public GetRequest addAllParams(Map<String, Object> params) {
        for (String key : params.keySet()) {
            this.addParameter(key, params.get(key));
        }

        return this;
    }
}

package com.yiayoframework.httpbaseapi.request;

import com.yiayoframework.httpbaseapi.message.request.RequestMessage;
import com.yiayoframework.httpbaseapi.utils.RequestTool;
import org.apache.http.entity.ContentType;

import java.util.Map;

/**
 * @author 李银 on 2019-11-13 10:27:42
 */
public class CpsGetRequest implements CpsApiRequest {

    private RequestMessage requestMessage;

    public CpsGetRequest(String apiName) {
        requestMessage = new RequestMessage(apiName, ContentType.APPLICATION_JSON);
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }

    @Override
    public CpsGetRequest addHeader(String name, String value) {
        requestMessage.addHeader(name, value);
        return this;
    }

    @Override
    public CpsGetRequest addParameter(String name, String value) {
        requestMessage.addParameter(name, value);
        return this;
    }

    public CpsGetRequest addParameter(String name, Object value) {
        RequestTool.addParameter(requestMessage, name, value);
        return this;
    }

    public CpsGetRequest addAllParams(Map<String, Object> params) {
        for (String key : params.keySet()) {
            this.addParameter(key, params.get(key));
        }

        return this;
    }
}

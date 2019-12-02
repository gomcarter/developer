package com.yiayoframework.httpbaseapi.message.request;

import java.util.ArrayList;
import java.util.List;

public abstract class RestfulRequestMessage {

    private List<String> restParameters = new ArrayList<String>();

    public RestfulRequestMessage() {
    }

    public List<String> getRestParameters() {
        return restParameters;
    }

    public void setRestParameters(List<String> restParameters) {
        this.restParameters = restParameters;
    }
}

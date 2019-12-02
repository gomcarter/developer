package com.gomcarter.frameworks.interfaces.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;

import java.util.List;

/**
 * @author gomcarter on 2019-12-02 09:23:09
 */
public class ApiInterface {
    @Notes("which controller")
    private String controller;

    @Notes("interface's name")
    private String name;

    @Notes("http://g.xx.com/platform/category，the part of： platform/category")
    private String url;

    @Notes("GET, POST, PUT, PATCH, DELETE")
    private String method;

    @Notes("use @Deprecated to make this interface to be Deprecated")
    private boolean deprecated = false;

    @Notes("use @Note to make some comment for this interface")
    private String mark;

    @Notes("returns")
    private ApiBean returns;

    @Notes("parameters")
    private List<ApiBean> parameters;

    public String getController() {
        return controller;
    }

    public ApiInterface setController(String controller) {
        this.controller = controller;
        return this;
    }

    public String getName() {
        return name;
    }

    public ApiInterface setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ApiInterface setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ApiInterface setMethod(String method) {
        this.method = method;
        return this;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public ApiInterface setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public ApiInterface setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public ApiBean getReturns() {
        return returns;
    }

    public ApiInterface setReturns(ApiBean returns) {
        this.returns = returns;
        return this;
    }

    public List<ApiBean> getParameters() {
        return parameters;
    }

    public ApiInterface setParameters(List<ApiBean> parameters) {
        this.parameters = parameters;
        return this;
    }
}

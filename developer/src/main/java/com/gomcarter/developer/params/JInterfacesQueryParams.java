package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;

/**
 * @ClassName JInterfaces
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public class JInterfacesQueryParams {
    @Notes("主键")
    private Long id;

    @Notes("接口名称")
    private String name;

    @Notes("接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category")
    private String url;

    @Notes("GET, POST, PUT, PATCH, DELETE")
    private String method;

    @Notes("是否已经废弃")
    private Boolean deprecated;

    @Notes("详细描述：如返回值说明，接口的一些说明等")
    private String mark;

    @Notes("属于哪个java项目")
    private Long javaId;

    @Notes("属于哪个前端项目")
    private Long endId;

    public Long getId() {
        return id;
    }

    public JInterfacesQueryParams setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JInterfacesQueryParams setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public JInterfacesQueryParams setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public JInterfacesQueryParams setMethod(String method) {
        this.method = method;
        return this;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public JInterfacesQueryParams setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public JInterfacesQueryParams setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public Long getJavaId() {
        return javaId;
    }

    public JInterfacesQueryParams setJavaId(Long javaId) {
        this.javaId = javaId;
        return this;
    }

    public Long getEndId() {
        return endId;
    }

    public JInterfacesQueryParams setEndId(Long endId) {
        this.endId = endId;
        return this;
    }
}

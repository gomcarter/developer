package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public class JInterfacesDetail {

    @Notes(value = "主键", notNull = true)
    private Long id;

    @Notes(value = "接口唯一标识符", notNull = true)
    private String hash;

    @Notes(value = "接口名称", notNull = true)
    private String name;

    @Notes(value = "接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category", notNull = true)
    private String url;

    @Notes(value = "GET, POST, PUT, PATCH, DELETE", notNull = true)
    private String method;

    @Notes(value = "是否已经废弃", notNull = true)
    private Boolean deprecated;

    @Notes(value = "详细描述：如返回值说明，接口的一些说明等", notNull = true)
    private String mark;

    @Notes(value = "返回值数据结构", notNull = true)
    private String returns;

    @Notes(value = "参数数据结构", notNull = true)
    private String parameters;

    @Notes(value = "属于哪个java项目", notNull = true)
    private JJava java;

    @Notes(value = "属于哪个前端项目", notNull = true)
    private JEnd end;

    @Notes(value = "创建时间", notNull = true)
    private Date createTime;

    @Notes(value = "更新时间", notNull = true)
    private Date modifyTime;

    public Long getId() {
        return id;
    }

    public JInterfacesDetail setId(Long id) {
        this.id = id;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public JInterfacesDetail setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getName() {
        return name;
    }

    public JInterfacesDetail setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public JInterfacesDetail setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public JInterfacesDetail setMethod(String method) {
        this.method = method;
        return this;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public JInterfacesDetail setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public JInterfacesDetail setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public String getReturns() {
        return returns;
    }

    public JInterfacesDetail setReturns(String returns) {
        this.returns = returns;
        return this;
    }

    public String getParameters() {
        return parameters;
    }

    public JInterfacesDetail setParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public JJava getJava() {
        return java;
    }

    public JInterfacesDetail setJava(JJava java) {
        this.java = java;
        return this;
    }

    public JEnd getEnd() {
        return end;
    }

    public JInterfacesDetail setEnd(JEnd end) {
        this.end = end;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public JInterfacesDetail setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public JInterfacesDetail setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }
}

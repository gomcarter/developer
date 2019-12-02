package com.gomcarter.developer.entity;

import java.util.Date;

/**
 * @ClassName Interfaces
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public class Interfaces {

    /**
     * @Description 主键
     */
    private Long id;

    /**
     * @Description 认定唯一接口标识符
     */
    private String hash;
    /**
     * @Description 接口名称
     */
    private String name;
    /**
     * @Description 接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category
     */
    private String url;
    /**
     * @Description GET, POST, PUT, PATCH, DELETE
     */
    private String method;
    /**
     * @Description 是否已经废弃
     */
    private Boolean deprecated;
    /**
     * @Description 详细描述：如返回值说明，接口的一些说明等
     */
    private String mark;
    /**
     * @Description 返回值数据结构
     */
    private String returns;
    /**
     * @Description 参数数据结构
     */
    private String parameters;
    /**
     * @Description 数据那个java项目
     */
    private Long fkJavaId;
    /**
     * @Description 属于哪个前端项目
     */
    private Long fkEndId;
    /**
     * @Description
     */
    private Date createTime;
    /**
     * @Description
     */
    private Date modifyTime;

    public Long getId() {
       	return id;
    }

    public Interfaces setId(Long id) {
        this.id = id;
        return this;
    }
    public String getHash() {
        return hash;
    }

    public Interfaces setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getName() {
        return name;
    }

    public Interfaces setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Interfaces setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Interfaces setMethod(String method) {
        this.method = method;
        return this;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public Interfaces setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public Interfaces setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public String getReturns() {
        return returns;
    }

    public Interfaces setReturns(String returns) {
        this.returns = returns;
        return this;
    }

    public String getParameters() {
        return parameters;
    }

    public Interfaces setParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public Long getFkJavaId() {
        return fkJavaId;
    }

    public Interfaces setFkJavaId(Long fkJavaId) {
        this.fkJavaId = fkJavaId;
        return this;
    }

    public Long getFkEndId() {
        return fkEndId;
    }

    public Interfaces setFkEndId(Long fkEndId) {
        this.fkEndId = fkEndId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Interfaces setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Interfaces setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

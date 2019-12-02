package com.gomcarter.developer.entity;

import java.util.Date;

/**
 * @ClassName Java
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public class Java {

    /**
     * @Description 主键
     */
    private Long id;


    /**
     * @Description 模块名称
     */
    private String name;
    /**
     * @Description 开发环境域名
     */
    private String devDomain;
    /**
     * @Description 测试环境域名
     */
    private String testDomain;
    /**
     * @Description 预发环境域名
     */
    private String prevDomain;
    /**
     * @Description 线上环境域名
     */
    private String onlineDomain;
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

    public Java setId(Long id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }

    public Java setName(String name) {
        this.name = name;
        return this;
    }

    public String getDevDomain() {
        return devDomain;
    }

    public Java setDevDomain(String devDomain) {
        this.devDomain = devDomain;
        return this;
    }

    public String getTestDomain() {
        return testDomain;
    }

    public Java setTestDomain(String testDomain) {
        this.testDomain = testDomain;
        return this;
    }

    public String getPrevDomain() {
        return prevDomain;
    }

    public Java setPrevDomain(String prevDomain) {
        this.prevDomain = prevDomain;
        return this;
    }

    public String getOnlineDomain() {
        return onlineDomain;
    }

    public Java setOnlineDomain(String onlineDomain) {
        this.onlineDomain = onlineDomain;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Java setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Java setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

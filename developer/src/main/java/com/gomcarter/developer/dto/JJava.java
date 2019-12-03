package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;

import java.util.Date;

/**
 * @ClassName JJava
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public class JJava {

    @Notes("主键")
    private Long id;

    @Notes("java模块名称")
    private String name;

    @Notes("开发环境域名")
    private String devDomain;

    @Notes("测试环境域名")
    private String testDomain;

    @Notes("预发环境域名")
    private String prevDomain;

    @Notes("线上环境域名")
    private String onlineDomain;

    @Notes("创建时间")
    private Date createTime;

    public Long getId() {
       	return id;
    }

    public JJava setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JJava setName(String name) {
        this.name = name;
        return this;
    }

    public String getDevDomain() {
        return devDomain;
    }

    public JJava setDevDomain(String devDomain) {
        this.devDomain = devDomain;
        return this;
    }

    public String getTestDomain() {
        return testDomain;
    }

    public JJava setTestDomain(String testDomain) {
        this.testDomain = testDomain;
        return this;
    }

    public String getPrevDomain() {
        return prevDomain;
    }

    public JJava setPrevDomain(String prevDomain) {
        this.prevDomain = prevDomain;
        return this;
    }

    public String getOnlineDomain() {
        return onlineDomain;
    }

    public JJava setOnlineDomain(String onlineDomain) {
        this.onlineDomain = onlineDomain;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public JJava setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

}

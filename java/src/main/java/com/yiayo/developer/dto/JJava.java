package com.yiayo.developer.dto;

import com.yiayoframework.liyinapi.annotation.Notes;

import java.util.Date;

/**
 * @ClassName JJava
 * @Description
 * @author 李银
 * @date 2019-06-17 16:41:01
 */
public class JJava {

    /**
     * @Description 主键
     */
    @Notes("主键")
    private Long id;
    /**
     * @Description java模块名称
     */
    @Notes("java模块名称")
    private String name;
    /**
     * @Description 开发环境域名
     */
    @Notes("开发环境域名")
    private String devDomain;
    /**
     * @Description 测试环境域名
     */
    @Notes("测试环境域名")
    private String testDomain;
    /**
     * @Description 预发环境域名
     */
    @Notes("预发环境域名")
    private String prevDomain;
    /**
     * @Description 线上环境域名
     */
    @Notes("线上环境域名")
    private String onlineDomain;
    /**
     * @Description
     */
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

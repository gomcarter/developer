package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * @author gomcarter on 2019-06-17 16:41:01
 */
public class Java {

    /**
     * 主键
     */

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模块名称
     */
    private String name;
    /**
     * 开发环境域名
     */
    private String devDomain;
    /**
     * 测试环境域名
     */
    private String testDomain;
    /**
     * 预发环境域名
     */
    private String prevDomain;
    /**
     * 线上环境域名
     */
    private String onlineDomain;
    /**
     *
     */
    private Date createTime;
    /**
     *
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

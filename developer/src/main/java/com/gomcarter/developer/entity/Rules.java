package com.gomcarter.developer.entity;

import java.util.Date;

/**
 *  Rules
 *
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public class Rules {

    /**
     * 主键
     */
    private Long id;


    /**
     * 规则名称
     */
    private String name;
    /**
     * 自动生成变量的javascript脚本
     */
    private String generator;
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

    public Rules setId(Long id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }

    public Rules setName(String name) {
        this.name = name;
        return this;
    }

    public String getGenerator() {
        return generator;
    }

    public Rules setGenerator(String generator) {
        this.generator = generator;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Rules setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Rules setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

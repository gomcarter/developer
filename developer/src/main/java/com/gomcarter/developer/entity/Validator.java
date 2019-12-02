package com.gomcarter.developer.entity;

import java.util.Date;

/**
 * @ClassName Validator
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
public class Validator {

    /**
     * @Description 主键
     */
    private Long id;


    /**
     * @Description 规则名称
     */
    private String name;
    /**
     * @Description 验证值的javascript脚本
     */
    private String handler;
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

    public Validator setId(Long id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }

    public Validator setName(String name) {
        this.name = name;
        return this;
    }

    public String getHandler() {
        return handler;
    }

    public Validator setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Validator setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Validator setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

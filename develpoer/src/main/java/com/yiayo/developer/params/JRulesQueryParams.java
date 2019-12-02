package com.yiayo.developer.params;

import java.util.Date;

/**
 * @ClassName JRules
 * @Description
 * @author 李银
 * @date 2019-06-17 16:41:01
 */
public class JRulesQueryParams {

    /**
     * @Description 主键
     */
    private Long id;

    /**
     * @Description 规则名称
     */
    private String name;
    /**
     * @Description 自动生成变量的javascript脚本
     */
    private String generator;
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

    public JRulesQueryParams setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JRulesQueryParams setName(String name) {
        this.name = name;
        return this;
    }

    public String getGenerator() {
        return generator;
    }

    public JRulesQueryParams setGenerator(String generator) {
        this.generator = generator;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public JRulesQueryParams setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public JRulesQueryParams setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

package com.yiayo.developer.entity;

import java.util.Date;

/**
 * @ClassName TestCaseItem
 * @Description
 * @author 李银
 * @date 2019-06-17 16:41:02
 */
public class TestCaseItem {

    /**
     * @Description 主键
     */
    private Long id;


    /**
     * @Description 具体接口
     */
    private String name;
    /**
     * @Description 结果处理器，存储javascript脚本，此脚本应该return下一个接口的参数
     */
    private String resultHandler;
    /**
     * @Description 接口hash值
     */
    private String hash;
    /**
     * @Description 配置
     */
    private String config;
    /**
     * @Description 接口外键
     */
    private Long fkInterfacesId;
    /**
     * @Description 接口用例外键
     */
    private Long fkTestCaseId;
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

    public TestCaseItem setId(Long id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }

    public TestCaseItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getResultHandler() {
        return resultHandler;
    }

    public TestCaseItem setResultHandler(String resultHandler) {
        this.resultHandler = resultHandler;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public TestCaseItem setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getConfig() {
        return config;
    }

    public TestCaseItem setConfig(String config) {
        this.config = config;
        return this;
    }

    public Long getFkInterfacesId() {
        return fkInterfacesId;
    }

    public TestCaseItem setFkInterfacesId(Long fkInterfacesId) {
        this.fkInterfacesId = fkInterfacesId;
        return this;
    }

    public Long getFkTestCaseId() {
        return fkTestCaseId;
    }

    public TestCaseItem setFkTestCaseId(Long fkTestCaseId) {
        this.fkTestCaseId = fkTestCaseId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TestCaseItem setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public TestCaseItem setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

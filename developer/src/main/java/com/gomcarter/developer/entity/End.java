package com.gomcarter.developer.entity;

import java.util.Date;

/**
 * @author gomcarter
 * @ClassName End
 * @Description
 * @date 2019-06-17 16:41:01
 */
public class End {

    /**
     * @Description 主键
     */
    private Long id;


    /**
     * @Description 项目名称
     */
    private String name;
    /**
     * @Description 前缀
     */
    private String prefix;

    private String jarUrl;

    private String kls;

    private String method;

    private String args;

    /**
     * @Description header
     */
    private String header;

    private String mark;

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

    public End setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public End setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public End setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public End setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getJarUrl() {
        return jarUrl;
    }

    public End setJarUrl(String jarUrl) {
        this.jarUrl = jarUrl;
        return this;
    }

    public String getKls() {
        return kls;
    }

    public End setKls(String kls) {
        this.kls = kls;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public End setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getArgs() {
        return args;
    }

    public End setArgs(String args) {
        this.args = args;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public End setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public End setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public End setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

}

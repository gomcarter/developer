package com.yiayo.developer.dto;

import com.yiayoframework.liyinapi.annotation.Notes;

import java.util.Date;

/**
 * @author 李银
 * @ClassName JEnd
 * @Description
 * @date 2019-06-17 16:41:01
 */
public class JEnd {

    @Notes("主键")
    private Long id;

    @Notes("项目名称")
    private String name;

    @Notes("前缀")
    private String prefix;

    @Notes("登录使用的jar包地址")
    private String jarUrl;

    @Notes("登录使用的类名")
    private String kls;

    @Notes("登录使用的方法")
    private String method;

    @Notes("登录使用的jar对应方法的参数: json字符串格式:[{\"key\":\"java.lang.Long\", \"value\": 6}], key是参数的类,value是对应的值")
    private String args;

    @Notes("header值是什么")
    private String header;

    @Notes("备注")
    private String mark;

    @Notes("创建时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public JEnd setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JEnd setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public JEnd setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public JEnd setHeader(String header) {
        this.header = header;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public JEnd setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getJarUrl() {
        return jarUrl;
    }

    public JEnd setJarUrl(String jarUrl) {
        this.jarUrl = jarUrl;
        return this;
    }

    public String getKls() {
        return kls;
    }

    public JEnd setKls(String kls) {
        this.kls = kls;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public JEnd setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getArgs() {
        return args;
    }

    public JEnd setArgs(String args) {
        this.args = args;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public JEnd setMark(String mark) {
        this.mark = mark;
        return this;
    }
}

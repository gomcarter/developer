package com.yiayo.developer.dto;

import com.yiayoframework.liyinapi.annotation.Notes;

import java.util.Date;

/**
 * @ClassName JInterfaces
 * @Description
 * @author 李银
 * @date 2019-06-17 16:41:01
 */
public class JInterfaces {

    @Notes(value = "主键", notNull = true)
    private Long id;

    @Notes(value = "接口唯一标识符", notNull = true)
    private String hash;

    @Notes(value = "接口名称", notNull = true)
    private String name;

    @Notes(value = "接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category", notNull = true)
    private String url;

    @Notes(value = "GET, POST, PUT, PATCH, DELETE", notNull = true)
    private String method;

    @Notes(value = "是否已经废弃", notNull = true)
    private Boolean deprecated;

    @Notes(value = "详细描述：如返回值说明，接口的一些说明等", notNull = true)
    private String mark;

    @Notes(value = "属于哪个java项目", notNull = true)
    private String java;

    @Notes(value = "属于哪个前端项目", notNull = true)
    private String end;

    @Notes(value = "创建时间", notNull = true)
    private Date createTime;

    @Notes(value = "更新时间", notNull = true)
    private Date modifyTime;

    public Long getId() {
       	return id;
    }

    public JInterfaces setId(Long id) {
        this.id = id;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public JInterfaces setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getName() {
        return name;
    }

    public JInterfaces setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public JInterfaces setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public JInterfaces setMethod(String method) {
        this.method = method;
        return this;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public JInterfaces setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public JInterfaces setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public String getJava() {
        return java;
    }

    public JInterfaces setJava(String java) {
        this.java = java;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public JInterfaces setEnd(String end) {
        this.end = end;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public JInterfaces setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public JInterfaces setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }
}

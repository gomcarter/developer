package com.yiayo.developer.params;

import com.yiayoframework.liyinapi.annotation.Notes;

/**
 * @ClassName JJava
 * @Description
 * @author 李银
 * @date 2019-06-17 16:41:01
 */
public class JJavaQueryParams {

    /**
     * @Description 主键
     */
    @Notes("主键")
    private Long id;

    /**
     * @Description 模块名称
     */
    @Notes("模块名称")
    private String name;

    public Long getId() {
       	return id;
    }

    public JJavaQueryParams setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JJavaQueryParams setName(String name) {
        this.name = name;
        return this;
    }
}

package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;

/**
 * @author gomcarter on 2019-06-17 16:41:01
 */
public class JEndQueryParams {

    @Notes("主键")
    private Long id;

    @Notes("项目名称")
    @Condition(type = MatchType.LIKE)
    private String name;

    public Long getId() {
        return id;
    }

    public JEndQueryParams setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JEndQueryParams setName(String name) {
        this.name = name;
        return this;
    }
}

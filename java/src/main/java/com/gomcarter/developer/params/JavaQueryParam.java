package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class JavaQueryParam {
    /**
     * 主键
     */
    @Notes("主键")
    private Long id;
    /**
     * 模块名称
     */
    @Notes("模块名称")
    @Condition(type = MatchType.LIKE)
    private String name;
}

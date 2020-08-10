package com.gomcarter.developer.params;

import com.gomcarter.developer.dto.CustomFunctionDto;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Set;

/**
 * @author gomcarter on 2020-01-06 10:09:59
 */
@Data
@Accessors(chain = true)
public class FunctionParam {
    @Notes("主键")
    private Long id;

    @Notes("id list")
    @Condition(field = "id", type = MatchType.IN)
    private Set<Long> idList;

    @Notes("函数名称")
    @Condition(type = MatchType.LIKE)
    private String name;

    @Notes("脚本备注")
    @Condition(type = MatchType.LIKE)
    private String mark;

    @Notes("username or is_public")
    @Condition(type = MatchType.AND)
    private CustomFunctionDto customFunctionDto;

    @Notes("创建时间大于这个时间")
    @Condition(field = "create_time", type = MatchType.GE)
    private Date createTimeGE;

    @Notes("创建时间小于这个时间")
    @Condition(field = "create_time", type = MatchType.LE)
    private Date createTimeLE;
}

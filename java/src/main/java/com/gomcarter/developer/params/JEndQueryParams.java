package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter on 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class JEndQueryParams {

    @Notes("主键")
    private Long id;

    @Notes("项目名称")
    @Condition(type = MatchType.LIKE)
    private String name;
}

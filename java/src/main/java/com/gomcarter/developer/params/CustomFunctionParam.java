package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yangxiaohua
 * @date 2020-7-8 13:21
 */
@Data
@Accessors(chain = true)
public class CustomFunctionParam {
    @Notes("username")
    @Condition(field ="user_name" , type = MatchType.OR)
    private String userName;

    @Notes("is_public")
    @Condition(field ="is_public" , type = MatchType.OR)
    private Boolean isPublic;
}

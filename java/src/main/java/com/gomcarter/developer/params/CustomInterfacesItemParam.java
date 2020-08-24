package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 李银 on 2020-08-20 18:09:06
 */
@Data
@Accessors(chain = true)
public class CustomInterfacesItemParam {

    @Notes("主键")
    @Condition(type = MatchType.LE)
    private Long id;

    @Notes("收藏接口 id")
    private Long customInterfacesId;

    @Notes("用例名称")
    @Condition(type = MatchType.LIKE)
    private String name;
}

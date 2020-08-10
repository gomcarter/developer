package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter on 2020-03-06 14:08:14
 */
@Data
@Accessors(chain = true)
public class InterfacesPackageParam {

    @Notes("主键")
    private Long id;

    @Notes("接口名称")
    @Condition(type = MatchType.LIKE)
    private String name;

    @Notes("创建人")
    @Condition(type = MatchType.LIKE)
    private String userName;

    @Notes("详细描述：如返回值说明，接口的一些说明等")
    @Condition(type = MatchType.LIKE)
    private String mark;

    @Notes("绑定一个test_case id")
    private Long testCaseId;
}

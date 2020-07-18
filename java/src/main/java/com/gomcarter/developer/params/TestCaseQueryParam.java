package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class TestCaseQueryParam {
    @Notes("主键")
    private Long id;

    @Notes("用例名称")
    @Condition(type = MatchType.LIKE)
    private String name;

    @Notes("用户名称（谁建的）")
    private String userName;

    @Notes("备注")
    private String mark;

    @Notes("创建时间大于这个时间")
    @Condition(field = "create_time", type = MatchType.GE)
    private Date createTimeGE;

    @Notes("创建时间小于这个时间")
    @Condition(field = "create_time", type = MatchType.LE)
    private Date createTimeLE;
}

package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Ignore;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 李银 on 2020-07-17 21:35:29
 */
@Data
@Accessors(chain = true)
public class TestCaseHistoryQueryParam {

    @Notes("主键")
    private Long id;

    @Notes("环境")
    private String env;

    @Notes("用例名称")
    @Condition(type = MatchType.LIKE)
    private String name;

    @Notes("用户名称（谁执行的）")
    @Ignore
    private String userName;
}

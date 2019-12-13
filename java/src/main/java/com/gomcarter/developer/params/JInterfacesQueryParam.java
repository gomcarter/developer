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
public class JInterfacesQueryParam {
    @Notes("主键")
    private Long id;

    @Notes("接口名称")
    @Condition(type = MatchType.LIKE)
    private String name;

    @Notes("控制器")
    @Condition(type = MatchType.LIKE)
    private String controller;

    @Notes("接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category")
    @Condition(type = MatchType.LIKE)
    private String url;

    @Notes("GET, POST, PUT, PATCH, DELETE")
    @Condition(type = MatchType.LIKE)
    private String method;

    @Notes("是否已经废弃")
    private Boolean deprecated;

    @Notes("详细描述：如返回值说明，接口的一些说明等")
    private String mark;

    @Notes("属于哪个java项目")
    @Condition(field = "fk_java_id")
    private Long javaId;

    @Notes("属于哪个前端项目")
    @Condition(field = "fk_end_id")
    private Long endId;
}

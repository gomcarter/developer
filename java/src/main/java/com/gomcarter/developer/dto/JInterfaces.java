package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class JInterfaces {

    @Notes(value = "主键", notNull = true)
    private Long id;

    @Notes(value = "接口唯一标识符", notNull = true)
    private String hash;

    @Notes(value = "控制器", notNull = true)
    private String controller;

    @Notes(value = "接口名称", notNull = true)
    private String name;

    @Notes(value = "接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category", notNull = true)
    private String url;

    @Notes(value = "GET, POST, PUT, PATCH, DELETE", notNull = true)
    private String method;

    @Notes(value = "是否已经废弃", notNull = true)
    private Boolean deprecated;

    @Notes(value = "详细描述：如返回值说明，接口的一些说明等", notNull = true)
    private String mark;

    @Notes(value = "属于哪个java项目", notNull = true)
    private String java;

    @Notes(value = "属于哪个前端项目", notNull = true)
    private String end;

    @Notes(value = "创建时间", notNull = true)
    private Date createTime;

    @Notes(value = "更新时间", notNull = true)
    private Date modifyTime;
}

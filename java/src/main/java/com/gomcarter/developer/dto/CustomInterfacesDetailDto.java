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
public class CustomInterfacesDetailDto {

    @Notes(value = "主键", notNull = true)
    private Long id;

    @Notes(value = "接口id", notNull = true)
    private Long interfacesId;

    @Notes(value = "用户名", notNull = true)
    private String username;

    @Notes(value = "接口唯一标识符", notNull = true)
    private String hash;

    @Notes(value = "控制器", notNull = true)
    private String controller;

    @Notes(value = "接口名称", notNull = true)
    private String name;

    @Notes(value = "拼接了端、服务的接口名称", notNull = true)
    private String complexName;

    @Notes(value = "接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category", notNull = true)
    private String url;

    @Notes(value = "GET, POST, PUT, PATCH, DELETE", notNull = true)
    private String method;

    @Notes(value = "是否已经废弃", notNull = true)
    private Boolean deprecated;

    @Notes(value = "详细描述：如返回值说明，接口的一些说明等", notNull = true)
    private String mark;

    @Notes(value = "返回值数据结构", notNull = true)
    private String returns;

    @Notes(value = "参数数据结构", notNull = true)
    private String parameters;

    @Notes(value = "属于哪个java项目", notNull = true)
    private JavaDto java;

    @Notes(value = "属于哪个前端项目", notNull = true)
    private EndDto end;

    @Notes(value = "创建时间", notNull = true)
    private Date createTime;

    @Notes(value = "更新时间", notNull = true)
    private Date modifyTime;

    @Notes(value = "参数", notNull = true)
    private String cusParameters;

    @Notes("数据那个java项目")
    private Long javaId;

    @Notes("属于哪个前端项目")
    private Long endId;

    @Notes("检查点")
    private String javascript;

    @Notes("预置参数")
    private String preParams;
}

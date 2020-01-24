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
public class EndDto {

    @Notes("主键")
    private Long id;

    @Notes("项目名称")
    private String name;

    @Notes("前缀")
    private String prefix;

    @Notes("登录使用的jar包地址")
    private String jarUrl;

    @Notes("登录使用的类名")
    private String kls;

    @Notes("登录使用的方法")
    private String method;

    @Notes("登录使用的jar对应方法的参数: json字符串格式:[{\"key\":\"java.lang.Long\", \"value\": 6}], key是参数的类,value是对应的值")
    private String args;

    @Notes("header值是什么")
    private String header;

    @Notes("备注")
    private String mark;

    @Notes("创建时间")
    private Date createTime;
}

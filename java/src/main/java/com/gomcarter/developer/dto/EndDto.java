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

    @Notes("header")
    private String header;

    @Notes("备注")
    private String mark;

    @Notes("鉴权配置")
    private String config;

    @Notes("创建时间")
    private Date createTime;
}

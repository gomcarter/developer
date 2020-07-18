package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020-01-06 10:09:59
 */
@Data
@Accessors(chain = true)
public class FunctionDto {

    /**
     * 主键
     */
    @Notes("主键")
    private Long id;
    /**
     * 用户id
     *
     */
    @Notes("用户名")
    private String userName;
    /**
     * 函数名称
     */
    @Notes("函数名称")
    private String name;
    /**
     * javascript脚本
     */
    @Notes("javascript脚本")
    private String scriptText;
    /**
     * 脚本备注
     */
    @Notes("脚本备注")
    private String mark;

    @Notes("示例参数")
    private String arguments;
    /**
     是否公用
     **/
    @Notes("是否公用")
    private Boolean isPublic;
    /**
     *
     */
    @Notes("")
    private Date createTime;
    /**
     *
     */
    @Notes("")
    private Date modifyTime;
}

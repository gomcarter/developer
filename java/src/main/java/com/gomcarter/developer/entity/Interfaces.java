package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 *  Interfaces
 *
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class Interfaces {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 认定唯一接口标识符
     */
    private String hash;
    /**
     * 控制器
     */
    private String controller;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口地址，域名后面的一截如：http://g.yiayo.com/platform/category中的platform/category
     */
    private String url;
    /**
     * GET, POST, PUT, PATCH, DELETE
     */
    private String method;
    /**
     * 是否已经废弃
     */
    private Boolean deprecated;
    /**
     * 详细描述：如返回值说明，接口的一些说明等
     */
    private String mark;
    /**
     * 返回值数据结构
     */
    private String returns;
    /**
     * 参数数据结构
     */
    private String parameters;
    /**
     * 数据那个java项目
     */
    private Long fkJavaId;
    /**
     * 属于哪个前端项目
     */
    private Long fkEndId;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;
}

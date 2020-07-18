package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class End {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 必备header
     */
    private String header;
    /**
     * 配置参数，json数据结构
     */
    private String config;
    /**
     * 备注
     */
    private String mark;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;

}

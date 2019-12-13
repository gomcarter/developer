package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class Validator {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 验证值的javascript脚本
     */
    private String handler;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;
}

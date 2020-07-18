package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class TestCase {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用例名称
     */
    private String name;
    /**
     * 用户名称（谁建的）
     */
    private String userName;
    /**
     * 预置参数
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String presetParams;
    /**
     * 备注
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String mark;

    @Notes("详细接口配置")
    private String workflow;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;
}

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
     * 用户id（谁建的）
     */
    private Long fkUserId;
    /**
     * 用户名称（谁建的）
     */
    private String userName;
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

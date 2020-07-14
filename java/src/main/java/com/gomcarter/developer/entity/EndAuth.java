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
public class EndAuth {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 哪个用户的
     */
    private String user;
    /**
     * 前端项目id
     */
    private Long fkEndId;
    /**
     * 配置参数，json数据结构
     */
    private String config;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;

}

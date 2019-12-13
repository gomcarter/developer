package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class Java {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 模块名称
     */
    private String name;
    /**
     * 开发环境域名
     */
    private String devDomain;
    /**
     * 测试环境域名
     */
    private String testDomain;
    /**
     * 预发环境域名
     */
    private String prevDomain;
    /**
     * 线上环境域名
     */
    private String onlineDomain;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;
}

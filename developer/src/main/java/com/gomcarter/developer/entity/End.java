package com.gomcarter.developer.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 *
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class End {

    /**
     * 主键
     */
    private Long id;


    /**
     * 项目名称
     */
    private String name;
    /**
     * 前缀
     */
    private String prefix;

    private String jarUrl;

    private String kls;

    private String method;

    private String args;

    /**
     * header
     */
    private String header;

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

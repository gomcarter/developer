package com.gomcarter.developer.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class JRulesQueryParams {
    /**
     * 主键
     */
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 自动生成变量的javascript脚本
     */
    private String generator;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;
}

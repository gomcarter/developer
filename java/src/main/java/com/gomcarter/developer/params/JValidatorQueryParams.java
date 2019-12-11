package com.gomcarter.developer.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class JValidatorQueryParams {
    /**
     * 主键
     */
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

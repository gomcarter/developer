package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020-02-10 16:05:03
 */
@Data
@Accessors(chain = true)
public class MarkDto {
    /**
     * 主键
     */
    @Notes("主键")
    private Long id;
    /**
     * 用户
     */
    @Notes("用户")
    private String user;
    /**
     * 备注内容
     */
    @Notes("备注内容")
    private String mark;
    /**
     *
     */
    @Notes("")
    private Date createTime;
}

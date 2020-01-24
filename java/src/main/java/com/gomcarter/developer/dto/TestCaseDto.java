package com.gomcarter.developer.dto;

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
public class TestCaseDto {

    @Notes("主键")
    private Long id;

    @Notes("用例名称")
    private String name;

    @Notes("用户名称（谁建的）")
    private String userName;

    @Notes("备注")
    private String mark;

    @Notes("预置参数")
    private String presetParams;

    @Notes("详细接口配置")
    private String workflow;

    @Notes("创建时间")
    private Date createTime;

    @Notes("上次修改时间")
    private Date modifyTime;

}

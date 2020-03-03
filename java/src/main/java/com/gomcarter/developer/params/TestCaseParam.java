package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class TestCaseParam {

    @Notes("用例名称")
    @NotNull
    private String name;

    @Notes("预置参数")
    private String presetParams;

    @Notes("备注")
    private String mark;

    @Notes("详细接口配置")
    private String workflow;
}

package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 李银 on 2020-08-20 18:09:06
 */
@Data
@Accessors(chain = true)
public class CustomInterfacesItemDto {

    @Notes("主键")
    private Long id;

    @Notes("用例名称")
    private String name;

    @Notes("收藏接口 id")
    private Long customInterfacesId;

    @Notes("参数数据结构")
    private String cusParameters;

    @Notes("检查点脚本")
    private String javascript;

    @Notes("预置参数")
    private String preParams;

    @Notes("自定义header")
    private String cusHeaders;
}

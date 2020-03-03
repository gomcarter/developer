package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter on 2020-02-10 16:05:03
 */
@Data
@Accessors(chain = true)
public class MarkParam {
    /**
     * 主键
     */
    @Notes("主键")
    private Long id;
    /**
     * 备注内容
     */
    @Notes("备注内容")
    private String mark;
    /**
     * 接口 id
     */
    @Notes("接口 id")
    private Long fkInterfacesId;
}

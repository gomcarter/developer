package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @author gomcarter on 2019-12-18 17:59:17
 */
@Data
@Accessors(chain = true)
public class RulesParam {

    /**
     * 主键
     */
    @Notes("主键")
    private Long id;

    /**
     * 规则名称
     */
    @Notes("规则名称")
    private String name;
    /**
     * 自动生成变量的javascript脚本
     */
    @Notes("自动生成变量的javascript脚本")
    private String generator;
    /**
     * 
     */
    @Notes("")
    private Date createTime;
    /**
     * 
     */
    @Notes("")
    private Date modifyTime;
}

package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @author gomcarter on 2019-12-18 18:05:57
 */
@Data
@Accessors(chain = true)
public class Rules {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
    /**
     * 脚本备注
     */
    private String mark;

    //@NotReplaceableStart
    // 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖


    //@NotReplaceableEnd
}

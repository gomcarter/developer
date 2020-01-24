package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @author gomcarter on 2020-01-06 10:09:59
 */
@Data
@Accessors(chain = true)
public class Function {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 函数名称
     */
    private String name;
    /**
     * javascript脚本
     */
    private String script;
    /**
     * 脚本备注
     */
    private String mark;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date modifyTime;

    //@NotReplaceableStart
    // 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖


    //@NotReplaceableEnd
}

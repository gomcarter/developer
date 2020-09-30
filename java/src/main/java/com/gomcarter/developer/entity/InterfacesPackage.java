package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020-03-06 14:08:14
 */
@Data
@Accessors(chain = true)
public class InterfacesPackage {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 打包名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;
    /**
     * 详细描述：如返回值说明，接口的一些说明等
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String mark;
    /**
     * 接口聚合相关配置
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String config;

    /**
     * 绑定一个test_case id
     */
    private Long testCaseId;
    /**
     * 创建者
     */
    private String userName;
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

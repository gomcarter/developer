package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020-03-02 10:29:14
 */
@Data
@Accessors(chain = true)
public class SettingOfUser {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 是否使用远程登录: 1-调用远程登录接口；2-使用本地user数据库
     */
    private Boolean remote;
    /**
     * 远程登录地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String url;
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

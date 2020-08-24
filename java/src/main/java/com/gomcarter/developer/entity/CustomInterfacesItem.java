package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 李银 on 2020-08-20 18:26:55
 */
@Data
@Accessors(chain = true)
public class CustomInterfacesItem {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 用例名称
     */
    private String name;
    /**
     * 收藏接口 id
     */
    private Long customInterfacesId;
    /**
     * 参数数据结构
     */
    private String cusParameters;
    /**
     * 检查点脚本
     */
    private String javascript;
    /**
     * 预置参数
     */
    private String preParams;
    /**
     * 自定义header
     */
    private String cusHeaders;
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

package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
@Data
@Accessors(chain = true)
public class InterfacesVersioned {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 接口 id
     */
    private Long fkInterfacesId;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 控制器
     */
    private String controller;
    /**
     * 接口地址，域名后面的一截如：http://g.cpsdb.com/platform/category中的platform/category
     */
    private String url;
    /**
     * GET, POST, PUT, PATCH, DELETE
     */
    private String method;
    /**
     * 是否已经废弃
     */
    private Boolean deprecated;
    /**
     * 详细描述：如返回值说明，接口的一些说明等
     */
    private String mark;
    /**
     * 返回值数据结构
     */
    private String returns;
    /**
     * 参数数据结构
     */
    private String parameters;
    /**
     * 属于哪个java项目
     */
    private Long fkJavaId;
    /**
     * 属于哪个前端项目
     */
    private Long fkEndId;
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

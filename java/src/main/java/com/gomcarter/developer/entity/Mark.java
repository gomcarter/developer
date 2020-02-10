package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @author gomcarter on 2020-02-10 16:05:03
 */
@Data
@Accessors(chain = true)
public class Mark {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户
     */
    private String user;
    /**
     * 备注内容
     */
    private String mark;
    /**
     * 接口 id
     */
    private Long fkInterfacesId;
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

package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CustomInterfaces {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 接口 id
     */
    private Long interfacesId;
    /**
     * 用户名
     **/
    private String username;

    /**
     * 收藏夹code
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String favoriteCode;

    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;
}

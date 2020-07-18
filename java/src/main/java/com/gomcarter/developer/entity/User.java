package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020-03-02 10:16:26
 */
@Data
@Accessors(chain = true)
public class User {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录账户名
     */
    private String username;
    /**
     * 姓名
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;
    /**
     * 邮箱
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String mail;
    /**
     * 联系电话
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String cellphone;
    /**
     * 密码
     */
    private String password;
    /**
     * 随机数
     */
    private String random;
    /**
     * 角色ID , 1002为普通用户，1001为项目经理，1000为管理员
     */
    private Integer roleId;
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

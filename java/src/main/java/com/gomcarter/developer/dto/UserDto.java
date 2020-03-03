package com.gomcarter.developer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020年03月02日15:33:30
 */
@Data
@Accessors(chain = true)
public class UserDto {

    private Long id;

    /**
     * 登录账户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 联系电话
     */
    private String cellphone;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次修改时间
     */
    private Date modifyTime;

    //@NotReplaceableStart
    // 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖


    //@NotReplaceableEnd
}

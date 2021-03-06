package com.gomcarter.developer.holder;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class LoginUser {
    @Notes("用户名称")
    private String name;

    @Notes("token")
    private String token;
}

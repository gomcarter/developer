package com.gomcarter.developer.controller.publics;

import com.gomcarter.developer.dto.JUser;
import com.gomcarter.developer.holder.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("publics/user")
public class PublicsUserController {

    @PostMapping(value = "login", name = "登录")
    public JUser login(@RequestParam String user, @RequestParam String password) throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/config/user.properties"));

        if (StringUtils.equals(properties.getProperty(user), password)) {
            return new JUser()
                    .setName(user)
                    .setToken(UserHolder.login(user));
        }

        throw new RuntimeException("账号或者密码错误！");
    }
}

package com.gomcarter.developer.controller.publics;

import com.gomcarter.developer.entity.SettingOfUser;
import com.gomcarter.developer.holder.LoginUser;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.service.SettingOfUserService;
import com.gomcarter.developer.service.UserService;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.json.JsonData;
import com.gomcarter.frameworks.httpapi.annotation.Method;
import com.gomcarter.frameworks.httpapi.api.BaseApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("publics/user")
public class PublicsUserController {

    @Resource
    UserService userService;

    @Resource
    SettingOfUserService settingOfUserService;

    class R {
        boolean validate;
        String message = "密码错误！";
    }

    @PostMapping(value = "test/login", name = "测试登录")
    void test(@RequestParam String url, @RequestParam String username, @RequestParam String password) throws Exception {
        R r = remoteLogin(url, username, password);
        AssertUtils.isTrue(r.validate, new CustomException(r.message));
    }

    private R remoteLogin(String url, String username, String password) throws Exception {
        R r = new R();
        BaseApi api = new BaseApi() {
            @Override
            protected Map<String, String> getUrlRouter() {
                return new HashMap<String, String>() {{
                    put("url", url);
                }};
            }
        };
        api.init();

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JsonData data = api.httpExecute(Method.POST, "url", JsonData.class, params, null);
        r.validate = data.isSuccess();
        r.message = data.getMessage();

        api.destroy();
        return r;
    }

    @PostMapping(value = "login", name = "登录")
    LoginUser login(@RequestParam String username, @RequestParam String password) throws Exception {
        R r;
        SettingOfUser setting;
        // admin账户登录还是走默认的，其他的账户走配置
        if (!UserHolder.ADMIN.equals(username) && SettingOfUserService.remote(setting = settingOfUserService.get())) {
            r = remoteLogin(setting.getUrl(), username, password);
        } else {
            r = new R();
            r.validate = this.userService.validate(username, password);
        }

        AssertUtils.isTrue(r.validate, new CustomException(r.message));

        return new LoginUser()
                .setName(username)
                .setToken(UserHolder.login(username));
    }

    @PostMapping(value = "login2", name = "登录")
    LoginUser login2(@RequestParam String username, @RequestParam String password) throws Exception {
        R r = new R();
        r.validate = this.userService.validate(username, password);

        AssertUtils.isTrue(r.validate, new CustomException(r.message));

        return new LoginUser()
                .setName(username)
                .setToken(UserHolder.login(username));
    }
}

package com.gomcarter.developer.publics;

import com.gomcarter.developer.entity.SettingOfUser;
import com.gomcarter.developer.holder.LoginUser;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.service.SettingOfUserService;
import com.gomcarter.developer.service.UserService;
import com.gomcarter.developer.utils.DefaultResponseHandler;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.json.JsonData;
import com.gomcarter.frameworks.config.mapper.JsonMapper;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URI;
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
        RequestConfig defaultRequestConfig = RequestConfig
                .custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
        CloseableHttpClient httpClientLocal = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();


        URIBuilder uriBuilder = new URIBuilder(URI.create(url));
        HttpPost post = new HttpPost(uriBuilder.build());
        String res = httpClientLocal.execute(post, new DefaultResponseHandler());
        JsonData data = JsonMapper.buildNonNullMapper().fromJson(res, JsonData.class);

        // TODO
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("username", username);
        params.put("password", password);
        r.validate = data.getCode() == 0;
        r.message = data.getMessage();

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

    @PostMapping(value = "login2", name = "登录2")
    LoginUser login2(@RequestParam String username, @RequestParam String password) throws Exception {
        R r = new R();
        r.validate = this.userService.validate(username, password);

        AssertUtils.isTrue(r.validate, new CustomException(r.message));

        return new LoginUser()
                .setName(username)
                .setToken(UserHolder.login(username));
    }
}

package com.gomcarter.developer.holder;

import com.gomcarter.developer.dto.JUser;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.BlowfishUtils;
import com.gomcarter.frameworks.base.common.CookieUtils;
import com.gomcarter.frameworks.base.exception.NoLoginException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class UserHolder {

    private static ThreadLocal<JUser> userThreadLocal = new ThreadLocal<>();

    public static void set(JUser user) {
        userThreadLocal.set(user);
    }

    public static JUser get() {
        JUser user = userThreadLocal.get();
        AssertUtils.notNull(user, new NoLoginException());

        return user;
    }

    public static String name() {
        return get().getName();
    }

    public static void reset() {
        userThreadLocal.remove();
    }


    private static final String VERSION = "V1.0.1";
    private static final String BLOW_FISH_KEY = "jsj31@^&;l'1!";
    private static final String SPLIT = "|";
    private static final String TOKEN_NAME = "token";

    /**
     * 对外接口
     * <p>
     * 如果返回结果为null，那么获取userId或者userName失败； 可能需要重新发起用户去登录；
     *
     * @param request request
     * @return JUser
     */
    public static JUser auth(HttpServletRequest request) {
        String token = CookieUtils.getByHeaderOrCookies(request, TOKEN_NAME);

        // 解密
        JUser user = decrypt(token);

        // 判断是否为 null
        AssertUtils.notNull(user, new NoLoginException());

        // 设置到 threadLocal
        set(user);

        return user;
    }

    /**
     * 用户登录之后调用这个方法，得到加密串返给前端，前端将这个串存在cookie中，cookie的key为token；
     *
     * @param user user
     * @return token
     * @throws Exception Exception
     */
    public static String login(String user) throws Exception {
        return BlowfishUtils.encrypt(VERSION + SPLIT + user, BLOW_FISH_KEY);
    }

    private static String d(String in) throws Exception {
        String o = BlowfishUtils.decrypt(in, BLOW_FISH_KEY);
        if (o.startsWith(VERSION)) {
            return o.substring(VERSION.length());
        } else {
            return null;
        }
    }

    private static JUser decrypt(String userCypher) {
        try {
            String user = d(userCypher);
            String[] splits = StringUtils.split(user, SPLIT);
            assert splits != null;
            return new JUser().setName(splits[0]);
        } catch (Exception e) {
            return null;
        }
    }
}

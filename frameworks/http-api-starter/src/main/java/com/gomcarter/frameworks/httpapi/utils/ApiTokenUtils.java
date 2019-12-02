package com.gomcarter.frameworks.httpapi.utils;

import com.alibaba.nacos.client.config.utils.MD5;
import com.gomcarter.frameworks.base.common.CookieUtils;
import com.gomcarter.frameworks.base.common.CustomDateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

/**
 * @author gomcarter on 2019-11-13 10:27:42
 */
public class ApiTokenUtils {

    // TODO: some key you mast redefine
    private static final String publicKey = "some key you mast redefine";

    public static final String TOKEN_NAME = "backToken";

    public static final String SEC_TIME = "secTime";

    public static final String DOMAIN = "domain";

    private static String getKey(int offset) {
        String time = CustomDateUtils.toString(getEnCodeTime(offset));
        return getKey(time);
    }

    private static String getKey(String time) {
        return publicKey + time + publicKey;
    }

    public static String getToken() {
        return MD5.getInstance().getMD5String(getKey(0));
    }

    private static String getToken(String time) {
        return MD5.getInstance().getMD5String(getKey(time));
    }

    private static String getOffSetToken(int offset) {
        return MD5.getInstance().getMD5String(getKey(offset));
    }

    private static Date getEnCodeTime(int offset) {
        Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + offset);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static boolean validate(HttpServletRequest request) {
        return validate(CookieUtils.getByHeaderOrCookies(request, TOKEN_NAME));
    }

    public static boolean validate(String token) {
        return StringUtils.equals(token, getToken()) ||
                StringUtils.equals(token, getOffSetToken(1)) ||
                StringUtils.equals(token, getOffSetToken(-1));
    }

    public static void main(String[] args) {
        System.out.println(ApiTokenUtils.getToken());
    }
}

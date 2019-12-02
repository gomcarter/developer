package it.gomcarter.frameworks.base.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: gomcarter
 * @date: 2017年12月3日 18:04:14
 */
public class CookieUtils {

    public static Map<String, String> getCookieMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        if (request == null || request.getCookies() == null) {
            return map;
        }

        for (Cookie cookie : request.getCookies()) {
            map.put(cookie.getName(), cookie.getValue());
        }

        return map;
    }

    public static Cookie getCookie(HttpServletRequest request, String key) {
        Cookie cookies[] = request.getCookies();
        Cookie c = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(key, cookie.getName())) {
                    c = cookie;
                    break;
                }
            }
        }
        return c;
    }

    public static String getCookieValue(Cookie[] cookies, String key) {
        if (cookies == null) {
            return null;
        }
        String result = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                result = cookie.getValue();
                break;
            }
        }
        return result;
    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        return getCookieValue(cookies, key);
    }

    public static void addCookies(HttpServletResponse response, String key, String value, Integer time) {
        addCookies(response, key, value, null, time);
    }

    public static void addCookies(HttpServletResponse response, String key, String value, String domain, Integer time) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(time);
        cookie.setPath("/");
        if (domain != null) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    public static void removeCookies(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getByHeaderOrCookies(HttpServletRequest request, String key) {
        // 优先从http头中获取取得
        String value = request.getHeader(key);
        // 参数没有时从cookie取得
        if (StringUtils.isBlank(value)) {
            Cookie[] cookies = request.getCookies();
            if (!ArrayUtils.isEmpty(cookies)) {
                value = getCookieValue(cookies, key);
            }
        }
        return value;
    }
}

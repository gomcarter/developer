package com.gomcarter.frameworks.base.common;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


/**
 * @author gomcarter 2017年12月2日 08:10:35
 */
public final class CustomStringUtils extends StringUtils {
    private static final char UNDERLINE = '_';

    /**
     * 下划线 转 驼峰
     *
     * @param param 参数
     * @return 结果
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(param.charAt(i));
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名转为下划线命名
     *
     * @param param 参数
     * @return 结果
     */
    public static String camelToUnderline(String param) {
        StringBuilder sb = new StringBuilder(param);
        //定位
        int temp = 0;
        if (!param.contains("_")) {
            for (int i = 0; i < param.length(); i++) {
                if (Character.isUpperCase(param.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 骆驼命名法转下划线命名法
     *
     * @param name ： abcDefG
     * @return abc_def_g
     */
    public static String underscore(String name) {
        String[] _a = "QWERTYUIOPASDFGHJKLZXCVBNM".split("");
        String[] _b = toLowerCase("_" + join(_a, ",_")).split(",");
        return replaceEach(name, _a, _b);
    }

    /**
     * append tail to head, blank will be ignored
     *
     * @param head one of string
     * @param tail another one
     * @return head + tail
     */
    public static String append(String head, String tail) {
        StringBuilder sb = new StringBuilder();
        if (isNotBlank(head)) {
            sb.append(head);
        }

        if (isNotBlank(tail)) {
            sb.append(tail);
        }
        return sb.toString();
    }

    /**
     * same function as append(String head, String tail)
     *
     * @param head one of int
     * @param tail another one
     * @return head + tail
     */
    public static String append(Integer head, String tail) {
        return append(toString(head), tail);
    }


    /**
     * object to string, "" for null
     *
     * @param o object
     * @return string
     */
    public static String toString(Object o) {
        return null == o ? EMPTY : o.toString();
    }

    /**
     * 获取文件拓展名
     *
     * @param filename file name
     * @return the suffix
     */
    public static String getFileSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    /**
     * 每隔delt个字符插入 inert
     *
     * @param str    str to be inserted
     * @param delt   how many index to inserted
     * @param insert insert string
     * @return string
     */
    public static String insert(String str, final int delt, final String insert) {
        if (insert == null || delt <= 0) {
            return str;
        }

        int length = length(str);
        int t = length / delt;
        if (t == 0) {
            return str;
        }

        String r = "";
        for (int i = 0; i < t; ++i) {
            String k = substring(str, i * delt, i * delt + delt);
            r += k + insert;
        }

        String c = substring(str, t * delt);
        r += c + insert;

        return trim(r);
    }


    private static final String from[] = {
            "\\", "/", ":", "*", "?", "\"", "'", "<", ">", "|"
    };

    public static final String to[] = {
            "_", "_", "_", "_", "_", "_", "_", "_", "_", "_"
    };

    /**
     * 验证文件命名是否正确， 将不正确的命名转换成 _
     *
     * @param fileName file  name
     * @return the correct file name
     */
    public static String validateFileName(String fileName) {
        return replaceEach(fileName, from, to);
    }

    /**
     * 每隔pos个字符， 拆分成一段
     *
     * @param src src
     * @param pos pos
     * @return split result
     */
    public static String[] split(String src, int pos) {
        if (pos <= 0) {
            String[] r = {src};
            return r;
        }
        src = trim(src);

        int length = length(src);
        int t = length / pos;
        if (t == 0) {
            String[] r = {src};
            return r;
        }

        String[] r = new String[t];
        for (int i = 0; i < t; ++i) {
            String k = substring(src, i * pos, i * pos + pos);
            r[i] = k;
        }
        return r;
    }

    /**
     * to toLowerCase safely
     *
     * @param target target
     * @return lowercase of target
     */
    public static String toLowerCase(String target) {
        if (null == target) {
            return null;
        }
        return target.toLowerCase();
    }

    /**
     * to toUpperCase safely
     *
     * @param target target
     * @return uppercase of target
     */
    public static String toUpperCase(String target) {
        if (null == target) {
            return null;
        }
        return target.toUpperCase();
    }

    /**
     * target is number or not
     *
     * @param target target
     * @return true for the target is certainly a number
     */
    public static boolean isNumber(String target) {
        return isNumeric(target) || parseDecimal(target) != null;
    }

    /**
     * parse source to Long safely
     *
     * @param source target
     * @return Long
     */
    public static Long parseLong(String source) {
        try {
            return new Long(source);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * parse source to Integer safely
     *
     * @param source target
     * @return Integer
     */
    public static Integer parseInt(String source) {
        try {
            return Integer.valueOf(source);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * parse source to Integer
     *
     * @param source       target
     * @param defaultValue defaultValue
     * @return Integer, if null then  defaultValue
     */
    public static Integer parseInt(Object source, Integer defaultValue) {
        try {
            return Integer.valueOf(source.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * parse source to BigDecimal
     *
     * @param source BigDecimal
     * @return Integer
     */
    public static BigDecimal parseDecimal(String source) {
        try {
            return new BigDecimal(source);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * get browser url params base on the key name
     *
     * @param url  browser url
     * @param name the param key
     * @return value
     */
    public static String getRequestParameters(String url, String name) {
        try {
            return url.split(name + "=")[1].split("&")[0].split("#")[0];
        } catch (Exception e) {
            return null;
        }
    }
}

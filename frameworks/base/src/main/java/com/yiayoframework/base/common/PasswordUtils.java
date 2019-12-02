package com.yiayoframework.base.common;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.Set;

public class PasswordUtils {

    public static final int pos = -1;

    public enum Level {
        low,
        normal,
        high
    }

    private static final int low = 0;
    public static final int normal = 1;
    private static final int high = 2;

    private static final Set<String> lowPwdSet = Sets.newHashSet(
            "1q2w3e4r", "1qaz2wsx",/*"01234567890",*/
            "asdfghjkl", "qwertyuiop", "zxcvbnm");

    public static Level resloveLevel(String password) {
        return Level.values()[_reslove(password)];
    }

    private static int _reslove(String password) {
        int length = YiayoStringUtils.length(password);

        if (length < 6) {
            return low;
        }

        int result = pos;

        boolean hasNumber = password.matches(".*[0-9].*"); //含数字
        boolean hasWord = password.matches(".*[A-Za-z].*");//含字母
        boolean hasOther = password.matches(".*[^\\w\\s].*");//含其他特殊字符

        if (hasNumber) {
            result++;
        }

        if (hasWord) {
            result++;
        }

        if (hasOther) {
            result++;
        }

        if (!hasOther) {//弱密码没有特殊符号,减少遍历
            for (String key : lowPwdSet) {
                if (StringUtils.contains(key, password.toLowerCase())) {
                    return low;
                }
            }
        }
        return Math.min(high, Math.max(low, result));
    }

    private static String CUSTOM_KEY = "cps2017";

    /**
     * 加密
     */
    public static String encrypt(String password, String random) {
        return new Md5Hash(CUSTOM_KEY + "_" + random + "_" + password).toHex();
    }

    /**
     * 验证密码
     */
    public static Boolean validate(String tobeValidated, String random, String encrypted) {
        try {
            return StringUtils.equals(encrypted, encrypt(tobeValidated, random));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String f =Md5Hash.fromHexString(CUSTOM_KEY + "_" + "68771289" + "_" + "e43e738542763c9cf1b690fa7e9aa73c").toString();
        System.out.println(f);
        String d = encrypt("a123456", "68771289");
        System.out.println(d);


//
//
//        String f = decrypt("tVZ1FDr8xJHXshsBlpXfhDsYsRJ_4maw");

//        List<String> result = Lists.newArrayList();
//        result.add("1!@s");
//        result.add("lzm821029");
//        result.add("1621932173169");
//        result.add("1234567890");
//        result.add("0123456");
//        result.add("abcd!aL");
//        result.add("1qaz2ws");
//        result.add("fdabu123!");
//
//        for (String r : result) {
//            System.out.println(r + ":" + PasswordUtils.resloveLevel(r));
//        }
    }
}

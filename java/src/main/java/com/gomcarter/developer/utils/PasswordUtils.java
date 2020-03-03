package com.gomcarter.developer.utils;

import com.gomcarter.frameworks.base.common.CustomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.Random;

public class PasswordUtils {

    public enum Level {
        /**
         * 弱密码
         */
        low(0),
        /**
         * 普通密码
         */
        normal(1),
        /**
         * 强密码
         */
        high(2);

        int value;

        Level(int value) {
            this.value = value;
        }
    }

    private static final String[] LOW_PWD_SET = {
            "1q2w3e4r", "1qaz2wsx", "01234567890",
            "asdfghjkl", "qwertyuiop", "zxcvbnm"
    };

    public static Level reslove(String password) {
        int length = CustomStringUtils.length(password);

        if (length < 6) {
            return Level.low;
        }

        int result = -1;

        //含数字
        boolean hasNumber = password.matches(".*[0-9].*");
        //含字母
        boolean hasWord = password.matches(".*[A-Za-z].*");
        //含其他特殊字符
        boolean hasOther = password.matches(".*[^\\w\\s].*");

        if (hasNumber) {
            result++;
        }

        if (hasWord) {
            result++;
        }

        if (hasOther) {
            result++;
        }

        if (!hasOther) {
            //弱密码没有特殊符号,减少遍历
            for (String key : LOW_PWD_SET) {
                if (StringUtils.contains(key, password.toLowerCase())) {
                    return Level.low;
                }
            }
        }

        int r = Math.min(Level.high.value, Math.max(Level.low.value, result));
        return Level.values()[r];
    }

    /**
     * 加密
     */
    public static String encrypt(String password, String random) {
        String key = "*&&^()^@@)(*(!sasad";
        return new Md5Hash(key + "_" + random + "_" + password).toHex();
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

    public static String random() {
        return String.format("%d", Math.abs(new Random().nextInt()) % 99999999);
    }

    public static void main(String[] args) {
        String random = random();
        System.out.println(random);
        System.out.println(encrypt("3e27f65cdf23407d9c3a72297567c5fa", random));
    }
}

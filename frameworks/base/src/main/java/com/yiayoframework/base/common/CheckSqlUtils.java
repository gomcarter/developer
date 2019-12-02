package com.yiayoframework.base.common;

public class CheckSqlUtils {

    public static void checkSql(String... param) {
        if (param == null || param.length < 1) {
            return;
        }

        for (String p : param) {
            if (hasSql(p)) {
                throw new RuntimeException("参数非法");
            }
        }
    }

    public static boolean hasSql(String param) {
        if (param == null) {
            return false;
        }

        if (param.length() > 40) {
            return true;
        }

        if (param.toLowerCase().contains(" and ")) {
            return true;
        }

        return false;
    }
}

package com.gomcarter.frameworks.base.common;


/**
 * @author gomcarter 2017年12月2日 08:10:35
 */
public class HexCaculateUtils {

    private final static char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',

            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',

            '~', '@', '#', '$', '^', '*', '(', ')', '-', '+'
    };

    private final static int HEX_NUMBER = DIGITS.length;

    /**
     * 将十进制的数字转换为指定进制的字符串。
     *
     * @param i      十进制的数字。
     * @param system 指定的进制，常见的2/8/16。
     * @return 转换后的字符串。
     */
    public static String numericToString(long i, int system) {
        long num = 0;
        if (i < 0) {
            num = (2L * 0x7fffffff) + i + 2L;
        } else {
            num = i;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((num / system) > 0) {
            buf[--charPos] = DIGITS[(int) (num % system)];
            num /= system;
        }
        buf[--charPos] = DIGITS[(int) (num % system)];
        return new String(buf, charPos, (32 - charPos));
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字。
     *
     * @param s      其它进制的数字（字符串形式）
     * @param system 指定的进制，常见的2/8/16。
     * @return 转换后的数字。
     */
    public static long stringToNumeric(String s, int system) {
        char[] buf = new char[s.length()];
        s.getChars(0, s.length(), buf, 0);
        long num = 0;
        for (int i = 0; i < buf.length; i++) {
            for (int j = 0; j < DIGITS.length; j++) {
                if (DIGITS[j] == buf[i]) {
                    num += j * Math.pow(system, buf.length - i - 1);
                    break;
                }
            }
        }
        return num;
    }

    /**
     * 将s的值加一，返回，并保持于s的等长，不足前排插入0
     *
     * @param s target
     * @return result
     */
    public static String CodePlus(String s) {
        return CodePlus(s, HEX_NUMBER);
    }

    /**
     * 将s的值加一，返回，并保持于s的等长，不足前排插入0
     *
     * @param s       target
     * @param decimal decimal
     * @return result
     */
    public static String CodePlus(String s, int decimal) {
        if (decimal > HEX_NUMBER) {
            throw new RuntimeException("超出进制可计算范围");
        }

        long nowValue = stringToNumeric(s, decimal);
        String nowString = numericToString(++nowValue, decimal);
        int lengthGap = s.length() - nowString.length();

        if (lengthGap < 0) {
            throw new RuntimeException("计算溢出");
        } else {
            if (lengthGap == 0) {
                return nowString;
            } else {
                String format = "%0" + lengthGap + "d";
                return String.format(format, 0) + nowString;
            }
        }
    }
}

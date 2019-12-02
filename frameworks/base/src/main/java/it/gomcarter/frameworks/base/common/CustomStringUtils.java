package it.gomcarter.frameworks.base.common;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @authher gomcarter 2017年12月2日 08:10:35
 */
public class CustomStringUtils extends StringUtils{

    public final static String ifnull(String orgin, String replace) {
        return null == orgin ? replace : orgin;
    }
    /**
     * 骆驼命名法转下划线命名法
     * @param name ： abcDefG
     * @return abc_def_g
     */
    public final static String underscore(String name) {
        String[] _a = "QWERTYUIOPASDFGHJKLZXCVBNM".split("");
        String[] _b = toLowerCase("_" + join(_a, ",_")).split(",");
        return replaceEach(name, _a, _b);
    }

    public final static String append(String head, String tail){
        StringBuilder sb = new StringBuilder();
        if(isNotBlank(head)) {
            sb.append(head);
        }

        if(isNotBlank(tail)) {
            sb.append(tail);
        }
        return sb.toString();
    }


    public final static String append(Integer head, String tail){
        StringBuilder sb = new StringBuilder();
        if(head!=null && isNotBlank(head.toString())) {
            sb.append(head);
        }

        if(isNotBlank(tail)) {
            sb.append(tail);
        }
        return sb.toString();
    }

    public final static String toString(Object o){
        if(null == o) {
            return "";
        } else {
            return o.toString();
        }
    }

    /**
     * 获取文件拓展名
     *
     * @param filename
     * @return
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
     * @param street
     * @param delt
     * @param insert
     * @return
     */
    public static String insert(String str, final int delt, final String insert) {
        if(insert == null || delt <= 0) {
            return str;
        }

        int length = length(str);
        int t = length / delt;
        if(t == 0) {
            return str;
        }

        String r = "";
        for(int i = 0; i < t; ++i) {
            String k = substring(str, i * delt, i * delt + delt);
            r += k + insert;
        }

        String c = substring(str, t * delt);
        r += c + insert;

        return trim(r);
    }

    public static final String sourceSet[] = {
        "Á", "À", "Ã", "Â", "á", "à", "ã", "â", "É", "È", "Ê", "é",
        "è", "ê", "ë", "Í", "í", "Ó", "Õ", "Ô", "ó", "õ", "ô", "Ú",
        "ú", "ù", "û", "ü", "Ç", "ç", "œ", "æ", "Ë", "Î", "Ï", "Ö",
        "Ù", "Û", "Ü", "Œ", "Æ", "ò", "ì", "Ä", "ä", "ß", "ñ", "º", "ª"
    };

    public static final String targetSet[] = {
        "A", "A", "A", "A", "a", "a", "a", "a", "E", "E", "E", "e",
        "e", "e", "e", "I", "i", "O", "O", "O", "o", "o", "o", "U",
        "u", "u", "u", "u", "C", "c", "oe", "ae", "E", "I", "I", "O",
        "U", "U", "U", "OE", "AE", "o", "i", "A", "a", "B", "n", "O", "a"
    };

    /**
     * 替换乱码
     * @param city
     * @return
     */
    public static String _replace(String city) {
        return replaceEach(city, sourceSet, targetSet);
    }


    public static final String from[] = {
        "\\", "/", ":", "*", "?", "\"", "'", "<", ">", "|"
    };

    public static final String to[] = {
        "_", "_", "_", "_", "_", "_", "_", "_", "_", "_"
    };
    public static String validateFileName(String fileName) {
        return replaceEach(fileName, from, to);
    }

    /**
     * 两个字符串质检 插入insert字符串
     * @param street
     * @param suburb
     * @param string
     * @return
     */
    public static String join(String head, String tail, String insert) {
        List<String> r = new ArrayList<String> ();
        if(isNotEmpty(head)) {
            r.add(head);
        }
        if(isNotEmpty(tail)) {
            r.add(tail);
        }
        return join(r, insert);
    }

    /**
     * 每隔pos个字符， 拆分成一段
     * @param src
     * @param pos
     */
    public static String[] split(String src, int pos) {
        if(pos <= 0) {
            String[] r = {src};
            return r;
        }
        src = trim(src);

        int length = length(src);
        int t = length / pos;
        if(t == 0) {
            String[] r = {src};
            return r;
        }

        String[] r = new String[t];
        for(int i = 0; i < t; ++i) {
            String k = substring(src, i * pos, i * pos + pos);
            r[i] = k;
        }
        return r;
    }


    public static boolean contains(String[] exCountry, String countryCode) {
        Set<String> main = new HashSet<String> ();
        for(String c : exCountry) {
            main.add(c);
        }
        return main.contains(countryCode);
    }


    public static String toLowerCase(String state) {
        if(null == state) {
            return "";
        }
        return state.toLowerCase();
    }

    public static final String a[] = {
        " ", "/", "_"
    };
    public static final String b[] = {
        "Á", "À", "Ã", "Â", "á", "à", "ã", "â", "É", "È", "Ê", "é",
        "è", "ê", "ë", "Í", "í", "Ó", "Õ", "Ô", "ó", "õ", "ô", "Ú",
        "ú", "ù", "û", "ü", "Ç", "ç", "œ", "æ", "Ë", "Î", "Ï", "Ö",
        "Ù", "Û", "Ü", "Œ", "Æ", "ò", "ì", "Ä", "ä", "ß", "ñ", "º", "ª"
    };

    public static String fedex_removeSpecialCharacter(String target) {
        if(null == target) {
            return "";
        }

        return target.replaceAll("[^\\w]|_", "");
    }

    public static boolean isNumber(String arg) {
        return isNumeric(arg) || parseInt(arg) != null || parseDecimal(arg) != null;
    }

    public static Long parseLong(String source) {
        try {
            return new Long(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer parseInt(String source) {
        try {
            return new Integer(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer parseInt(Object source, Integer defaultValue) {
        try {
            return new Integer(source.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static BigDecimal parseDecimal(String source) {
        try {
            return new BigDecimal(source);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param type
     * @param imageType : 图片类型，如： producer； upload；=> upload/2014/12/15/xxx.jpg
     * @param key : 创建的用户名
     * @return
     */
    public static String gatherFileName(String type, String imageType) {
        Date d = new Date();
//        double rand = Math.random();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = upperCase(UUID.randomUUID().toString()/*CodecUtils.hex256(sdf.format(d) + rand + key)*/);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        if(null == imageType) {
            imageType = "";
        }

        if (isBlank(type)) {
            type = "";
        } else {
            type = "." + type;
        }

        return imageType + "/" + sdf1.format(d) + "/" + sdf2.format(d) + "/" + fileName + type;
    }

    public static String getRequestParameters(String url, String name) {
        try {
            return url.split(name + "=")[1].split("&")[0].split("#")[0];
        } catch(Exception e) {
            return null;
        }
    }

    public static Boolean isCellPhone(String phone) {
        return phone != null && (phone = trim(phone)).length() == 11 && phone.startsWith("1") && isNumeric(phone);
    }
}

package io.github.gomcarter.frameworks.base.common;

import org.apache.commons.lang3.time.DateUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @authher gomcarter 2017年12月2日 08:10:35
 */
public class CustomDateUtils extends DateUtils {

    public static long RequestDelayTime = 10 * MILLIS_PER_MINUTE;

    /**
     * date to XMLGregorianCalendar
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar toXMLCalendar(Date date) {
        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            System.out.println(e.getMessage());
        }

        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(date);

        return df.newXMLGregorianCalendar(cal1);
    }

    public static Date fromXMLCalendar(XMLGregorianCalendar c) {
        if (c == null) {
            return null;

        }

        return c.toGregorianCalendar().getTime();
    }

    public static String toString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return (sdf.format(date));
        }
    }

    public static String toCNString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            return sdf.format(date);
        }
    }

    public static String toString(Date date, String format) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return (sdf.format(date));
        }
    }

    // index: 时间(Date)的 index -1：昨天， 0-今天，1：明天
    public static Date getDay(Date date, int index) {
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return DateUtils.addDays(date, index);
    }

    public static Date today() {
        return getDay(0);
    }

    public static Date tomorrow() {
        return getDay(1);
    }

    public static Date yestoday() {
        return getDay(-1);
    }

    // index: 0：今天；1：明天； -1：昨天
    public static Date getDay(int index) {
        Date date = new Date();
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return DateUtils.addDays(date, index);
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static Date getCurrentMonth() {
        Date date = new Date();
        date = DateUtils.setDays(date, 1);
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return date;
    }

    public static Date fromString(String dateStr) {
        dateStr = dateStr.replaceAll("-", "/");
        return fromString(dateStr, "yyyy/MM/dd");
    }

    /**
     * 格式为： yyyy-MM-dd HH:mm:ss
     * 或者 yyyy-MM-dd
     *
     * @param dateStr
     * @return
     */
    public static Date fromStringWithTime(String dateStr) {
        dateStr = CustomStringUtils.replaceEach(dateStr, new String[]{
                "-", "\n",
        }, new String[]{
                "/", "",
        });
        if (CustomStringUtils.contains(dateStr, ":")) {
            return fromString(dateStr, "yyyy/MM/dd HH:mm:ss");
        } else {
            return fromString(dateStr, "yyyy/MM/dd");
        }
    }

    public static Date fromString(String dateStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("错误的时间：" + dateStr);
        }

        if (null == date) {
            throw new RuntimeException("错误的时间：" + dateStr);
        }
        return date;
    }

    public static Integer dateDiff(Date end, Date begin) {
        long diff = end.getTime() - begin.getTime();
        Long day = diff / (24 * 60 * 60 * 1000);
        return day.intValue();
    }

    public static Date max(Date a, Date b) {
        if (b == null) {
            return a;
        }

        if (a == null) {
            return b;
        }

        if (a.before(b)) {
            return b;
        } else {
            return a;
        }
    }

    public static Date min(Date a, Date b) {
        if (b == null) {
            return a;
        }

        if (a == null) {
            return b;
        }

        if (a.after(b)) {
            return b;
        } else {
            return a;
        }
    }

    public static Boolean isBetween(Date time, Date startTime, Date endTime) {
        return time.after(startTime) && time.before(endTime);
    }

    public static Long getTime(Date date) {
        return date == null ? null : date.getTime();
    }
}

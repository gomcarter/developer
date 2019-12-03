package com.gomcarter.frameworks.base.common;

import org.apache.commons.lang3.time.DateUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author gomcarter 2017年12月2日 08:10:35
 */
public class CustomDateUtils extends DateUtils {

    /**
     * date to XMLGregorianCalendar
     *
     * @param date target
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar toXMLCalendar(Date date) {
        try {
            DatatypeFactory df = DatatypeFactory.newInstance();
            GregorianCalendar cal1 = new GregorianCalendar();
            cal1.setTime(date);
            return df.newXMLGregorianCalendar(cal1);
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    /**
     * XMLGregorianCalendar to date
     *
     * @param c target
     * @return Date
     */
    public static Date fromXMLCalendar(XMLGregorianCalendar c) {
        if (c == null) {
            return null;

        }

        return c.toGregorianCalendar().getTime();
    }

    /**
     * date to string, such as yyyy-MM-dd HH:mm:ss
     *
     * @param date target
     * @return String such as yyyy-MM-dd HH:mm:ss
     */
    public static String toString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return (sdf.format(date));
        }
    }

    /**
     * date to string, such as yyyy年MM月dd日
     *
     * @param date target
     * @return String such as yyyy年MM月dd日
     */
    public static String toCNString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            return sdf.format(date);
        }
    }

    /**
     * date to string, such as format
     *
     * @param date   target
     * @param format such as yyyy年MM月dd日 or yyyy-MM-dd HH:mm:ss or anything else
     * @return String such as format
     */
    public static String toString(Date date, String format) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return (sdf.format(date));
        }
    }

    /**
     * @param date  target
     * @param index 时间(Date)的 index -1：昨天， 0-今天，1：明天
     * @return date
     */
    public static Date getDay(Date date, int index) {
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return DateUtils.addDays(date, index);
    }

    /**
     * get today
     *
     * @return date of today
     */
    public static Date today() {
        return getDay(0);
    }

    /**
     * get tomorrow
     *
     * @return date of tomorrow
     */
    public static Date tomorrow() {
        return getDay(1);
    }

    /**
     * get yestoday
     *
     * @return date of yestoday
     */
    public static Date yestoday() {
        return getDay(-1);
    }

    /**
     * get date
     *
     * @param index index: 0：今天；1：明天； -1：昨天
     * @return date of index to current date
     */
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
     * @return 当月第一天
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

    /**
     * convert dateStr to date
     *
     * @param dateStr date string
     * @return date
     */
    public static Date fromString(String dateStr) {
        dateStr = dateStr.replaceAll("-", "/");
        return fromString(dateStr, "yyyy/MM/dd");
    }

    /**
     * convert dateStr to date time
     * 格式为： yyyy-MM-dd HH:mm:ss
     * 或者 yyyy-MM-dd
     *
     * @param dateStr dateStr
     * @return date
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

    /**
     * convert dateStr to date time
     * 格式为： formatStr
     *
     * @param dateStr   dateStr
     * @param formatStr user define format
     * @return date
     */
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

    /**
     * how many date between begin and end
     *
     * @param end   end
     * @param begin begin
     * @return how many date between begin and end
     */
    public static Integer dateDiff(Date end, Date begin) {
        long diff = end.getTime() - begin.getTime();
        Long day = diff / (24 * 60 * 60 * 1000);
        return day.intValue();
    }

    /**
     * get latter date from a and b
     *
     * @param a one of the date
     * @param b another one
     * @return this latest date
     */
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

    /**
     * get early date from a and b
     *
     * @param a one of the date
     * @param b another one
     * @return this early date
     */
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

    /**
     * some time is between startTime and endTime or not
     *
     * @param time      time to judge
     * @param startTime condition
     * @param endTime   another condition
     * @return this early date
     */
    public static Boolean isBetween(Date time, Date startTime, Date endTime) {
        return time.after(startTime) && time.before(endTime);
    }

    /**
     * timestamp of date
     *
     * @param date time to judge
     * @return timestamp of date
     */
    public static Long getTime(Date date) {
        return date == null ? null : date.getTime();
    }
}

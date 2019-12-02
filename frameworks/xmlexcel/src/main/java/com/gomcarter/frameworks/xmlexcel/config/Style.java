package com.gomcarter.frameworks.xmlexcel.config;

/**
 * @author gomcarter on 2018年5月4日 10:41:52
 */
public class Style {
    /**
     * 样式style的key， 在header指定某一个cell的样式
     */
    private String key;

    /**
     * 字体颜色，默认黑色
     */
    private String color;

    /**
     * 是否加粗
     */
    private boolean bold;

    /**
     * 北京色，默认无
     */
    private String interior;
    /**
     * 显示格式：如数字0.000_ 0_
     */
    private Format format;

    public Style() {
    }

    public static Style of(String key) {
        return of(key, null);
    }

    public static Style of(String key, String color) {
        return of(key, color, null);
    }

    public static Style of(String key, String color, String interior) {
        return of(key, color, false, interior);
    }

    public static Style of(String key, String color, boolean bold, String interior) {
        return new Style(key, color, bold, interior, null);
    }

    public static Style of(String key, String color, boolean bold, String interior, Format format) {
        return new Style(key, color, bold, interior, format);
    }

    private Style(String key, String color, boolean bold, String interior, Format format) {
        this.key = key;
        this.color = color;
        this.interior = interior;
        this.bold = bold;
        this.format = format;
    }

    public String getKey() {
        return key;
    }

    public Style setKey(String key) {
        this.key = key;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Style setColor(String color) {
        this.color = color;
        return this;
    }

    public String getInterior() {
        return interior;
    }

    public Style setInterior(String interior) {
        this.interior = interior;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public Style setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public Format getFormat() {
        return format;
    }

    public Style setFormat(Format format) {
        this.format = format;
        return this;
    }
}

package com.yiayoframework.excel.config;

/**
 * @author 李银 on 2018年5月4日 10:41:52
 */
public class Cell {
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

    public Cell() {
    }

    public Cell(String key) {
        this(key, null);
    }

    public Cell(String key, String color) {
        this(key, null, null);
    }

    public Cell(String key, String color, String interior) {
        this(key, color, false, interior);
    }

    public Cell(String key, String color, boolean bold, String interior) {
        this(key, color, bold, interior, null);
    }

    public Cell(String key, String color, boolean bold, String interior, Format format) {
        this.key = key;
        this.color = color;
        this.interior = interior;
        this.bold = bold;
        this.format = format;
    }

    public String getKey() {
        return key;
    }

    public Cell setKey(String key) {
        this.key = key;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Cell setColor(String color) {
        this.color = color;
        return this;
    }

    public String getInterior() {
        return interior;
    }

    public Cell setInterior(String interior) {
        this.interior = interior;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public Cell setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public Format getFormat() {
        return format;
    }

    public Cell setFormat(Format format) {
        this.format = format;
        return this;
    }
}

package com.yiayoframework.excel.config;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Administrator on 2016/5/12.
 */
public class Header {
    /**
     * 当写入数据是某个对象的时候，这个key相当于这个对象的字段域
     */
    private String key;

    /**
     * 显示的标题
     */
    private String name;

    /**
     * 单元格样式计算
     */
    private Function cellStyle;

    /**
     * 表头格样式计算
     */
    private Function headerStyle;

    /**
     * 数据格式化
     */
    private Function dataFormatter;

    /**
     * 同一列，数据相同自动合并，默认不合并
     */
    private boolean autoMerge;

    /**
     * 宽度
     */
    private Integer width;

    public static List<Header> of(Header... headers) {
        return Arrays.stream(headers)
                .collect(Collectors.toList());
    }

    public static Header of(String name) {
        return of(name, name);
    }

    public static Header of(String key, String name) {
        return of(key, name, false);
    }

    public static Header of(String name, boolean autoMerge) {
        return of(null, name, autoMerge);
    }

    public static Header of(String key, String name, boolean autoMerge) {
        return of(key, name, autoMerge, null, null, null, null);
    }

    public static Header of(String key, String name, boolean autoMerge, Integer width) {
        return of(key, name, autoMerge, width, null, null, null);
    }

    public static Header of(String name, boolean autoMerge, Function dataFormatter) {
        return of(name, name, autoMerge, null, null, null, dataFormatter);
    }

    public static Header of(String name, Function dataFormatter) {
        return of(name, false, dataFormatter);
    }

    public static Header of(String key, String name, boolean autoMerge, Integer width, Function cellStyle, Function headerStyle, Function dataFormatter) {
        return new Header(key, name, autoMerge, width, cellStyle, headerStyle, dataFormatter);
    }

    private Header(String key, String name, boolean autoMerge, Integer width, Function cellStyle, Function headerStyle, Function dataFormatter) {
        this.key = key;
        this.name = name;
        this.width = width;
        this.cellStyle = cellStyle;
        this.headerStyle = headerStyle;
        this.dataFormatter = dataFormatter;
        this.autoMerge = autoMerge;
    }

    public String getKey() {
        return key;
    }

    public Header setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAutoMerge() {
        return autoMerge;
    }

    public void setAutoMerge(Boolean autoMerge) {
        this.autoMerge = autoMerge;
    }

    public Function getCellStyle() {
        return cellStyle;
    }

    public Header setCellStyle(Function cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    public Function getHeaderStyle() {
        return headerStyle;
    }

    public Header setHeaderStyle(Function headerStyle) {
        this.headerStyle = headerStyle;
        return this;
    }

    public Function getDataFormatter() {
        return dataFormatter;
    }

    public Header setDataFormatter(Function dataFormatter) {
        this.dataFormatter = dataFormatter;
        return this;
    }

    public boolean isAutoMerge() {
        return autoMerge;
    }

    public Header setAutoMerge(boolean autoMerge) {
        this.autoMerge = autoMerge;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public Header setWidth(Integer width) {
        this.width = width;
        return this;
    }
}

package com.gomcarter.frameworks.xmlexcel.download;


import com.gomcarter.frameworks.xmlexcel.DataFormatterParser;

import java.util.Map;

/**
 * @authher gomcarter 2017年12月2日 08:10:35
 */
public class DownloaderTitles {

    private String title;
    private String column;
    private Boolean needMerge = false;
    private Boolean isMergeId = false;
    /**
     * DownloadDataType
     */
    private String dataType;
    private String fieldName;

    private DataFormatterParser dataParser;

    //映射--通用
    private Map<String, String> fillMap;
    //日期格式化--通用
    private String format;
    //绝对值，限制用
    private String abs;
    //object[index].fieldName
    //bigdecimal.setScale X
    private String trunCate;
    //+fieldName,-fieldName,*fieldName,/fieldName X
    private String calculate;

    public String getCalculate() {
        return calculate;
    }

    public DownloaderTitles setCalculate(String calculate) {
        this.calculate = calculate;
        return this;
    }

    public String getTrunCate() {
        return trunCate;
    }

    public DownloaderTitles setTrunCate(String trunCate) {
        this.trunCate = trunCate;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public DownloaderTitles setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public String getAbs() {
        return abs;
    }

    public DownloaderTitles setAbs(String abs) {
        this.abs = abs;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public DownloaderTitles setFormat(String format) {
        this.format = format;
        return this;
    }

    public Map<String, String> getFillMap() {
        return fillMap;
    }

    public DownloaderTitles setFillMap(Map<String, String> fillMap) {
        this.fillMap = fillMap;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DownloaderTitles setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public DownloaderTitles setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public DownloaderTitles setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public Boolean getNeedMerge() {
        return needMerge;
    }

    public DownloaderTitles setNeedMerge(Boolean needMerge) {
        this.needMerge = needMerge;
        return this;
    }

    public DataFormatterParser getDataParser() {
        return dataParser;
    }

    public DownloaderTitles setDataParser(DataFormatterParser dataParser) {
        this.dataParser = dataParser;
        return this;
    }

    public Boolean getIsMergeId() {
        return this.isMergeId;
    }

    public DownloaderTitles setIsMergeId(Boolean mergeId) {
        isMergeId = mergeId;
        return this;
    }
}

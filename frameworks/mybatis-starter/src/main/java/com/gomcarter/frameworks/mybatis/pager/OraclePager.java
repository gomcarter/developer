package com.gomcarter.frameworks.mybatis.pager;


/**
 * 提供oracle默认的翻页工具
 *
 * @author gomcarter 2017年12月2日 08:10:35
 */
public class OraclePager implements Pageable {

    protected int startNum = 0;
    protected int endNum = 0;
    protected int pageSize = 20;

    protected String order;
    protected String sort;

    public OraclePager(int startNum, int endNum, int pageSize) {
        this.startNum = startNum;
        this.endNum = endNum;
        this.pageSize = pageSize;
    }

    public OraclePager(int startNum, int endNum, int pageSize, String sort, String order) {
        this(startNum, endNum, pageSize);
        this.sort = sort;
        this.order = order;
    }

    public OraclePager(int pageSize) {
        this.pageSize = pageSize;
    }

    public OraclePager() {

    }

    @Override
    public Pageable turnPage(int pageNo) {
        this.startNum = pageNo * this.pageSize + 1;
        this.endNum = this.startNum + this.pageSize;
        return this;
    }

    @Override
    public int getStartNum() {
        return startNum;
    }

    public OraclePager setStartNum(int startNum) {
        this.startNum = startNum;
        return this;
    }

    public int getEndNum() {
        return endNum;
    }

    public OraclePager setEndNum(int endNum) {
        this.endNum = endNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public OraclePager setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public OraclePager setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public OraclePager setSort(String sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public int getPageCount() {
        return pageSize;
    }

    @Override
    public String getOrderColumn() {
        return this.sort;
    }

    @Override
    public String getOrderType() {
        return this.order;
    }

}

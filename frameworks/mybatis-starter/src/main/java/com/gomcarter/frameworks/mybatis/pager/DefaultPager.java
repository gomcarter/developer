package com.gomcarter.frameworks.mybatis.pager;


/**
 * 提供默认的翻页工具（支持mysql）
 *
 * @author gomcarter 2017年12月2日 08:10:35
 */
public class DefaultPager implements Pageable {
    protected int rows = 20;
    protected int page = 1;
    protected String sort = "id";
    protected String order = "desc";

    public DefaultPager(int rows, int page) {
        this.rows = rows;
        this.page = page;
    }

    public DefaultPager(int rows, int page, String sort, String order) {
        this.rows = rows;
        this.page = page;
        this.sort = sort;
        this.order = order;
    }

    public DefaultPager() {

    }

    @Override
    public Pageable turnPage(int pageNo) {
        this.page = pageNo;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public DefaultPager setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public int getPage() {
        return page;
    }

    public DefaultPager setPage(int page) {
        this.page = page;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public DefaultPager setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public DefaultPager setOrder(String order) {
        this.order = order;
        return this;
    }

    @Override
    public int getStartNum() {
        return this.rows * (this.page - 1);
    }

    @Override
    public int getPageCount() {
        return this.rows;
    }

    @Override
    public String getOrderColumn() {
        return sort;
    }

    @Override
    public String getOrderType() {
        return order;
    }
}

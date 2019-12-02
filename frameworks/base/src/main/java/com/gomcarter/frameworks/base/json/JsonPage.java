package com.gomcarter.frameworks.base.json;


public class JsonPage {
    private int startNum = 0;
    private int pageCount = 10;
    private String orderColumn;
    private String orderType;

    public JsonPage() {
    }

    public JsonPage(int startNum, int pageCount) {
        this.setPageCount(pageCount);
        this.setStartNum(startNum);
    }

    public int getStartNum() {
        return startNum;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(final int pageCount) {
        this.pageCount = Math.max(pageCount, 0);
    }

    /**
     * 设置每页的记录数量, 低于1时自动调整为1.
     */
    public void setStartNum(final int startNum) {
        this.startNum = Math.max(startNum, 0);
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}

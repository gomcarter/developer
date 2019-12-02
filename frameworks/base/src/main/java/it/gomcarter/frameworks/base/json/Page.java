package it.gomcarter.frameworks.base.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Page<T> implements Serializable {
    private Integer pageSize = 20;
    private Integer pageNo = 1;
    private Integer total = 0;
    private List<T> rows = new ArrayList<T>();

    public Page() {

    }

    public Page(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Page setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Page setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public Page setTotal(Integer total) {
        this.total = total;
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    public Page setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }
}

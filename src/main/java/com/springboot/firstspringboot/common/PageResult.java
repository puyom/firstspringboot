package com.springboot.firstspringboot.common;

import java.io.Serializable;
import java.util.List;

//向前端传输的分页类

public class PageResult<T> implements Serializable {

    //总条数
    private long total;
    //集合-所有数据
    private List<T> rows;

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }


    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}

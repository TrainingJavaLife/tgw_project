package com.tgw360.uip.common;

import com.tgw360.uip.query.PageQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * Created by 邹祥 on 2017/11/21 15:24
 */
public class PageBean<T> extends BaseObject implements Serializable {
    private Integer pageNum; //页码
    private Integer pageSize; //页面大小
    private Integer total; //元素总数
    private Integer pages; // 页面总数
    private List<T> data; // 数据

    protected PageBean() {
    }

    public PageBean(PageQuery query) {
        this(query.getPageNum(), query.getPageSize());
    }

    public PageBean(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

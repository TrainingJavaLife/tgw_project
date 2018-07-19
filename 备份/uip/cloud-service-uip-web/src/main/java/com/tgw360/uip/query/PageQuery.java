package com.tgw360.uip.query;

import com.tgw360.uip.common.BaseObject;

/**
 * 分页Query
 * Created by 邹祥 on 2017/12/5 15:55
 */
public class PageQuery extends BaseObject {
    private Integer pageNum = 1; // 页码
    private Integer pageSize = 10; // 页码大小

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

    public Integer getFrom() {
        return (pageNum - 1) * pageSize;
    }

    public Integer getSize() {
        return pageSize;
    }
}

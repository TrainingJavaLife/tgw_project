package com.tgw360.uip.common;

import com.tgw360.uip.query.PageQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格对象
 * Created by 邹祥 on 2017/12/6 10:44
 */
public class TableBean extends PageBean<List<String>> {

    public TableBean(PageQuery query) {
        super(query);
    }

    public TableBean(int pageNum, int pageSize) {
        super(pageNum, pageSize);
        this.setData(new ArrayList<>());
    }

    private List<String> headers = new ArrayList<>(); // 表头

    public List<String> getHeaders() {
        return headers;
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }

    public void appendTo(int row, Object obj) {
        while ((row >= this.getData().size())) {
            this.getData().add(new ArrayList<>());
        }
        this.getData().get(row).add(obj.toString());
    }
}

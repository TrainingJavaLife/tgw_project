package com.tgw360.uip.query;

import com.tgw360.uip.Table.SortOrder;
import com.tgw360.uip.condition.Condition;

import java.util.List;

/**
 * 条件查询Query
 * Created by 邹祥 on 2017/12/5 16:10
 */
public class SearchByConditionQuery extends PageQuery {
    private String token; // 查询字符串
    private String w;
    private Integer sortColumn = 1; // 按第几列排序
    private SortOrder sortOrder = SortOrder.ASC;    // 排序方向
    private List<Condition> conditions;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public Integer getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(Integer sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}

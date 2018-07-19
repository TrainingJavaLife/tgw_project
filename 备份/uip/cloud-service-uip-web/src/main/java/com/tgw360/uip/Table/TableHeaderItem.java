package com.tgw360.uip.Table;

import com.tgw360.uip.common.BaseObject;

/**
 * Created by 邹祥 on 2017/12/20 15:53
 */
public class TableHeaderItem extends BaseObject {
    private Long columnID;
    private String text;
    private SortOrder sortOrder;

    public TableHeaderItem(Long columnID, String text) {
        this.columnID = columnID;
        this.text = text;
    }

    @Override
    public String toString() {
        if (sortOrder != null)
            return String.join("@", text,  sortOrder.toString());
        else
            return String.join("@", text);
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getColumnID() {
        return columnID;
    }

    public String getText() {
        return text;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }
}

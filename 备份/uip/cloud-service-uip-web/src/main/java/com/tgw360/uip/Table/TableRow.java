package com.tgw360.uip.Table;

import com.tgw360.uip.common.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 邹祥 on 2017/12/20 15:59
 */
public class TableRow extends BaseObject {
    private List<Object> cells = new ArrayList<>();

    public void add(Object value) {
        this.cells.add(value);
    }

    public Object get(int columnNum) {
        return cells.get(columnNum);
    }

    public void set(int columnNum, Object value) {
        cells.set(columnNum, value);
    }

    public List<Object> getCells() {
        return cells;
    }
}

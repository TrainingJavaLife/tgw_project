package com.tgw360.uip.Table;

import com.tgw360.uip.common.PageBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * HTML表格
 * Created by 邹祥 on 2017/12/20 15:44
 */
public class Table {
    public static final String LINE_SEPERATOR = "\n";
    private List<TableHeaderItem> header = new ArrayList<>();
    private List<TableRow> body = new ArrayList<>();
    private Long nowColumnID = -1L;

    public void addHeader(String text) {
        nowColumnID++;
        this.header.add(new TableHeaderItem(nowColumnID, text));
    }

    public void addRow() {
        this.body.add(new TableRow());
    }

    public void appendToRow(int rowNum, Object value) {
        this.body.get(rowNum).add(value);
    }

    public Integer size() {
        return body.size();
    }

    public void sort(final int sortColumn, final SortOrder sortOrder) {
        // 清除表头中原有的排序标记
        for (TableHeaderItem item : header) {
            item.setSortOrder(null);
        }
        // 添加上新的排序标记
        header.get(sortColumn).setSortOrder(sortOrder);
        // 排序
        Collections.sort(body, new Comparator<TableRow>() {
            @Override
            public int compare(TableRow row1, TableRow row2) {
                int result;
                Object o1 = row1.get(sortColumn);
                Object o2 = row2.get(sortColumn);
                if (o1 instanceof String && o2 instanceof String) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;
                    result = s1.compareTo(s2);
                } else {
                    try {
                        Double d1 = Double.valueOf(o1.toString());
                        Double d2 = Double.valueOf(o2.toString());
                        result = d1.compareTo(d2);
                    } catch (Exception e) {
                        throw new RuntimeException("无法比较的类型：" + o1.getClass() + ",  " + o2.getClass());
                    }
                }
                if (sortOrder == SortOrder.DESC) {
                    result = -1 * result;
                }
                return result;
            }
        });
        // 修改序号
        for (int i = 0; i < body.size(); i++) {
            body.get(i).set(0, i + 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(header.toString());
        sb.append(LINE_SEPERATOR);
        for (TableRow row : body) {
            sb.append(row.toString()).append(LINE_SEPERATOR);
        }
        return sb.toString();
    }

    public TablePage page(int pageNum, int pageSize) {
        TablePage page = new TablePage(pageNum, pageSize);
        page.setTable(this);
        return page;
    }

    public class TablePage extends PageBean<Object> {
        private Table table;

        public TablePage(int pageNum, int pageSize) {
            super(pageNum, pageSize);
        }

        public void setTable(Table table) {
            this.table = table;
            this.setTotal(table.size());
        }

        public List<TableHeaderItem> getHeader() {
            return table.header;
        }

        private int fromIndex() {
            return (getPageNum() - 1) * getPageSize();
        }

        private int toIndex() {
            return Math.min(getPageNum() * getPageSize(), body.size());
        }

        public List<TableRow> getBody() {
            return table.body.subList(fromIndex(), toIndex());
        }

        public String format(int column, Object s) {
            String text = table.header.get(column).getText();
            int len = text.codePointCount(0, text.length()) + 10;
            return String.format("%" + len + "s", s.toString());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < table.header.size(); i++) {
                TableHeaderItem item = table.header.get(i);
                sb.append(format(i, item));
            }
            sb.append(LINE_SEPERATOR);
            for (int i = fromIndex(); i < toIndex(); i++) {
                for (int j = 0; j < table.body.get(i).getCells().size(); j++) {
                    Object obj = table.body.get(i).getCells().get(j);
                    sb.append(format(j, obj));
                }
                sb.append(LINE_SEPERATOR);
            }
            return sb.toString();
        }
    }
}

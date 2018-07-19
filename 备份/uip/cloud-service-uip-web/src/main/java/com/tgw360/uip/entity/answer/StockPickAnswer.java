package com.tgw360.uip.entity.answer;

import com.tgw360.uip.entity.question.Question;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 选股的答案
 * Created by 邹祥 on 2017/12/26 16:33
 */
public class StockPickAnswer extends AnswerAdaptor {
    private Integer columnNum;  // 列的数目
    private List<Question> questions = new ArrayList<>();   // 选股条件
    private Map<String, Object[]> map = new LinkedHashMap<>();  // 存放结果，key为股票代码，value为结果，value的长度=columnNum

    public StockPickAnswer(int columnNum) {
        this.columnNum = columnNum;
    }

    public void addRow(String code, Object[] row) {
        this.map.put(code, row);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append(key);
            Object[] values = map.get(key);
            for (Object obj : values) {
                sb.append("\t").append(obj);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Integer getColumnNum() {
        return columnNum;
    }

    public Map<String, Object[]> getMap() {
        return map;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

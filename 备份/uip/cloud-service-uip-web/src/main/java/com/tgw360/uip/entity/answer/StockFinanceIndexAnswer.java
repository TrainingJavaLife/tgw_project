package com.tgw360.uip.entity.answer;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Answer—— 股票的某个指标
 * Created by 邹祥 on 2018/1/2 13:55
 */
public class StockFinanceIndexAnswer extends AnswerAdaptor {
    private List<Pair<String, Double>> pairs;

    public List<Pair<String, Double>> getPairs() {
        return pairs;
    }

    public void setPairs(List<Pair<String, Double>> pairs) {
        this.pairs = pairs;
    }
}

package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.entity.answer.StockPickAnswer;
import com.tgw360.uip.repository.StockRepository;

import java.util.List;
import java.util.Map;

/**
 * 这个类用来为结果列表增加一行股票名称的列
 * Created by 邹祥 on 2017/12/26 20:44
 */
public class StockNameStockPickQuestion extends StockPickQuestionItem {

    private StockRepository stockRepository = SpringUtils.getBean(StockRepository.class);

    @Override
    public Answer perform() {
        // TODO: 加入缓存
        Map<String, Stock> all = stockRepository.findAllStockBasicInfo();
        StockPickAnswer answer = new StockPickAnswer(1);
        for (String code : all.keySet()) {
            answer.addRow(code, new Object[]{all.get(code).getName()});
        }
        return answer;
    }

    @Override
    public void doParse(List<Term> terms) {

    }

    @Override
    public boolean check() {
        return true;
    }

    @Override
    public String toString() {
        return "[股票名称]";
    }
}

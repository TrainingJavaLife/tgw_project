package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.CacheUtils;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.entity.answer.StockFinanceIndexAnswer;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.repository.FinanceRepository;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Question——股票的某个指标
 * Created by 邹祥 on 2017/12/25 17:47
 */
public class StockFinanceIndexQuestion extends QuestionAdaptor {

    private static final FinanceRepository financeRepository = SpringUtils.getBean(FinanceRepository.class);

    private Stock stock;   // 股票名称或股票代码
    private FinanceIndex index; //指标

    @Override
    public Answer perform() {
        StockFinanceIndexAnswer answer = new StockFinanceIndexAnswer();
        List<Pair<String, Double>> pairs = financeRepository.findSingleFinanceIndex(stock.getCode(), index);
        answer.setPairs(pairs);
        return answer;
    }

    @Override
    public void doParse(List<Term> terms) {
        for (Term term : terms) {
            if (MyNature.STOCK.eqaulTo(term.nature)) {
                this.stock = CacheUtils.convertToStockCode(term.word);
            }
            if (MyNature.INDEX.eqaulTo(term.nature)) {
                this.index = FinanceIndex.parse(term.word);
            }
        }
    }

    @Override
    public boolean check() {
        if (stock == null)
            return false;
        if (index == null)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("[%s的%s]", stock.getName(), index.firstAlias());
    }

    public Stock getStock() {
        return stock;
    }

    public FinanceIndex getIndex() {
        return index;
    }
}

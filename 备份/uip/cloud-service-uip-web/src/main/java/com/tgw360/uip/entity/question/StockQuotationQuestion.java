package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.entity.answer.StockQuotationAnswer;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.repository.StockRepository;

import java.util.List;

/**
 * Question——股票行情
 * Created by 邹祥 on 2017/12/25 17:45
 */
public class StockQuotationQuestion extends QuestionAdaptor {
    private static final StockRepository stockRepository = SpringUtils.getBean(StockRepository.class);

    private String w; // 股票名称 或 股票代码

    @Override
    public void doParse(List<Term> terms) {
        for (Term term : terms) {
            if (MyNature.STOCK.eqaulTo(term.nature)) {
                this.w = term.word;
                break;
            }
        }
    }

    @Override
    public boolean check() {
        if (this.w == null) {
            return false;
        }
        return true;
    }

    @Override
    public Answer perform() {
        Stock stock = stockRepository.findByCodeOrName(w);
        StockQuotationAnswer answer = new StockQuotationAnswer();
        answer.setStock(stock);
        return answer;
    }

    @Override
    public String toString() {
        return String.format("[%s的行情]", w);
    }

    public String getW() {
        return w;
    }
}

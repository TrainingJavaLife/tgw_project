package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.repository.ConceptRepository;
import com.tgw360.uip.repository.StockRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 选股Question——根据概念选股
 * Created by 邹祥 on 2017/12/25 17:48
 */
public class ConceptStockPickQuestion extends StockPickQuestionItem {

    private List<String> conceptNames = new ArrayList<>();  // 概念名称
    private StockRepository stockRepository = SpringUtils.getBean(StockRepository.class);

    @Override
    public Answer perform() {
        return stockRepository.findByConceptStockPickQuestion(this);
    }

    @Override
    public void doParse(List<Term> terms) {
        for (Term term : terms) {
            if (MyNature.CONCEPT.eqaulTo(term.nature)) {
                this.conceptNames.add(term.word);
            }
        }
        // 解决 "概念是机器人" 这一类 股票与概念同名的情况
        List<String> stockNames = new ArrayList<>();
        boolean hasKEYWORD_GAINIAN = false;
        for (Term term : terms) {
            if (MyNature.KEYWORD_GAINIAN.eqaulTo(term.nature)) {
                hasKEYWORD_GAINIAN = true;
            }
            if (MyNature.STOCK.eqaulTo(term.nature)) {
                stockNames.add(term.word);
            }
        }
        if (hasKEYWORD_GAINIAN) {
            for (String stockName : stockNames)
                if (new ConceptRepository().isValidConceptName(stockName))
                    this.conceptNames.add(stockName);
        }
    }

    @Override
    public boolean check() {
        if (conceptNames.size() == 0)
            return false;
        return true;
    }

    public void addConceptName(String conceptName) {
        this.conceptNames.add(conceptName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[概念包含:");
        for (String conceptName : conceptNames) {
            sb.append(" ").append(conceptName);
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean containsConcept(String conceptName) {
        return this.conceptNames.contains(conceptName);
    }

    public List<String> getConceptNames() {
        return conceptNames;
    }
}

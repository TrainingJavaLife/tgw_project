package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.entity.answer.StockPickAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合选股
 * Created by 邹祥 on 2017/12/26 9:04
 */
public class StockPickCompositeQuestion extends StockPickQuestion {

    private List<StockPickQuestion> childs = new ArrayList<>();

    @Override
    public Answer perform() {
        List<StockPickAnswer> answers = new ArrayList<>();
        int columnCount = 0;    // 总列数
        for (StockPickQuestion question : childs) {
            StockPickAnswer answer = (StockPickAnswer) question.perform();
            answers.add(answer);
            columnCount += answer.getColumnNum();
        }
        // 合并操作
        StockPickAnswer finalAnswer = new StockPickAnswer(columnCount);
        for (String code : answers.get(0).getMap().keySet()) {
            boolean existInAllAnswers = true;
            for (int i = 1; i < answers.size(); i++) {
                if (!answers.get(i).getMap().containsKey(code)) {
                    existInAllAnswers = false;
                    break;
                }
            }
            if (existInAllAnswers) {
                Object[] row = new Object[columnCount];
                int now = 0;
                for (StockPickAnswer answer : answers) {
                    Object[] objects = answer.getMap().get(code);
                    for (Object obj : objects) {
                        row[now++] = obj;
                    }
                }
                finalAnswer.addRow(code, row);
            }
        }
        return finalAnswer;
    }

    @Override
    public void doParse(List<Term> terms) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void add(StockPickQuestion question) {
        this.childs.add(question);
    }

    @Override
    public StockPickQuestion getChild(int i) {
        return this.childs.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (StockPickQuestion question : childs) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(question.toString());
        }
        sb.append("}");
        return sb.toString();
    }
}

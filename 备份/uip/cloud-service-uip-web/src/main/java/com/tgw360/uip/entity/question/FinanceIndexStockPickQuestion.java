package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.condition.Quarter;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.repository.StockRepository;

import java.util.Date;
import java.util.List;

/**
 * 选股Question——财务指标选股
 * Created by 邹祥 on 2017/12/25 17:48
 */
public class FinanceIndexStockPickQuestion extends StockPickQuestionItem {

    @Override
    public Answer perform() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Question parseFrom(List<Term> terms) {
        boolean firstIndex = true;  // 指示当前是第一个指标还是第二个指标
        boolean complex = false;   // 复杂类型还是简单类型
        for (Term term : terms) {
            if (MyNature.INDEX.eqaulTo(term.nature)) {
                firstIndex = false;
                continue;
            }
            if (!firstIndex) {   // 如果第一个指标名称后面还有 指标/年份/季度/日期,则属于复杂类型
                if (MyNature.INDEX.eqaulTo(term.nature)
                        || MyNature.YEAR.eqaulTo(term.nature)
                        || MyNature.QUARTER.eqaulTo(term.nature)
                        || MyNature.DATE.eqaulTo(term.nature)) {
                    complex = true;
                    break;
                }
            }
        }
        FinanceIndexStockPickQuestion question;
        if (complex) {
            question = new ComplexFinanceIndexStockPickQuestion();
        } else {
            question = new SimpleFinanceIndexStockPickQuestion();
        }
        question.doParse(terms);
        if (question.check())
            return question;
        else
            return new WrongQuestion();
    }

    @Override
    public void doParse(List<Term> terms) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean check() {
        throw new UnsupportedOperationException();
    }

    protected int parseStringToYear(String str) {
        switch (str) {
            case "今年":
            case "上年":
                return thisYear();
            case "去年":
                return thisYear() - 1;
            case "前年":
                return thisYear() - 2;
            default:
                return Integer.parseInt(str.substring(0, 4));
        }
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static Integer thisYear() {
        // TODO: 修改为每天更新一次数据
        return new Date().getYear() + 1900;
    }

    /**
     * 获取当前季度
     *
     * @return
     */
    public static Quarter thisQuarter() {
        int month = new Date().getMonth();
        if (month > 9)
            return Quarter.THIRD;
        if (month > 6)
            return Quarter.SECOND;
        if (month > 3)
            return Quarter.FIRST;
        return Quarter.FOURTH;
    }

    public static String generateDate(Integer year, Quarter quarter) {
        int thisYear = thisYear();
        Quarter thisQuarter = thisQuarter();
        if (year == null)
            year = thisYear;
        if (quarter == null) {
            if (year == thisYear) {
                quarter = thisQuarter;
                if (quarter == Quarter.FOURTH) {
                    year = year - 1;
                }
            } else
                quarter = Quarter.FOURTH;
        }
        return year + "-" + quarter.getMonth() + "-" + quarter.getDay();
    }
}

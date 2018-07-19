package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.condition.Operator;
import com.tgw360.uip.condition.Quarter;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.nlp.ChineseNumberParser;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.repository.StockRepository;

import java.util.List;

/**
 * Question——简单类型的财务指标选股
 * Created by 邹祥 on 2017/12/28 11:21
 */
public class SimpleFinanceIndexStockPickQuestion extends FinanceIndexStockPickQuestion {

    private static final StockRepository stockRepository = SpringUtils.getBean(StockRepository.class);

    private Integer year;   // 年份
    private Quarter quarter;    // 季度
    private String date;    // 日期
    private FinanceIndex index; // 指标
    private Operator operator;  // 比较符
    private Double number;  // 数值
    /*
    如果 number2 == null， 则表示区间  index operator number  例如：  eps > 1
    如果 number2 != null， 则表示区间  index in [number, number1]，  例如 eps in [1, 5]
     */
    private Double number2; // 第二个数值

    @Override
    public void doParse(List<Term> terms) {
        for (int i = 0; i < terms.size(); i++) {
            Term term = terms.get(i);
            switch (MyNature.parse(term.nature.name())) {
                case YEAR:
                    this.year = parseStringToYear(term.word);
                    break;
                case QUARTER:
                    this.quarter = Quarter.parse(term.word);
                    break;
                case DATE:
                    this.date = term.word;
                    break;
                case INDEX:
                    this.index = FinanceIndex.parse(term.word);
                    break;
                case OPERATOR:
                    this.operator = Operator.parse(term.word);
                    break;
                case m:
                    double value = ChineseNumberParser.parse(term.word);
                    if (i > 0 && terms.get(i - 1).word.equals("-")) {   // 如果前面是负号
                        value = -1 * value;
                    }
                    if (number == null)
                        number = value;
                    else
                        number2 = value;
                    break;
            }
        }
    }

    @Override
    public boolean check() {
        if (index == null)  // 指标是必须的
            return false;
        if (!isRange() && operator == null)   // 比较符是必须的
            return false;
        if (number == null)
            number = 0d;
        if (date == null)
            date = generateDate(year, quarter);
        if (number2 != null) {  // 如果number2不为空，则为范围区间 [number, number2]
            if (number > number2) {
                double temp = number;
                number = number2;
                number2 = temp;
            }
        }
        return true;
    }

    @Override
    public Answer perform() {
        return stockRepository.findBySimpleFinanceIndexStockPickQuestion(this);
    }

    @Override
    public String toString() {
        if (isRange()) {
            return String.format("[%s %s >%s, <%s]", date, index.firstAlias(), number, number2);
        } else {
            return String.format("[%s %s %s %s]", date, index.firstAlias(), operator.getSymbol(), number);
        }
    }

    /**
     * 是否是[min, max]区间范围形式的查询
     *
     * @return
     */
    public boolean isRange() {
        return number2 != null;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FinanceIndex getIndex() {
        return index;
    }

    public void setIndex(FinanceIndex index) {
        this.index = index;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Double getNumber2() {
        return number2;
    }

    public void setNumber2(Double number2) {
        this.number2 = number2;
    }
}

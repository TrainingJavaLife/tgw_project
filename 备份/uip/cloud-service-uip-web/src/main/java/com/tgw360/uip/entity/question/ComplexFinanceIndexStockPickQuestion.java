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
 * Question——复杂类型的财务指标选股
 * Created by 邹祥 on 2017/12/28 15:59
 */
public class ComplexFinanceIndexStockPickQuestion extends FinanceIndexStockPickQuestion {

    private static final StockRepository stockRepository = SpringUtils.getBean(StockRepository.class);

    private Integer leftYear;   // 左边的年份
    private Quarter leftQuarter;    // 左边的季度
    private String leftDate;    // 左边的日期
    private FinanceIndex leftIndex;     // 左边的指标
    private Integer rightYear;  // 右边的年份
    private Quarter rightQuarter;   // 右边的季度
    private String rightDate;   // 右边的日期
    private FinanceIndex rightIndex;    // 右边的指标
    private Operator operator;  // 比较符
    private Double number;  // 数值
    private Boolean percentage; // 是否是百分比

    @Override
    public void doParse(List<Term> terms) {
        boolean isLeft = true;      // 指示当前是左边的指标还是右边的指标
        for (int i = 0; i < terms.size(); i++) {
            Term term = terms.get(i);
            switch (MyNature.parse(term.nature.name())) {
                case YEAR:
                    int year = parseStringToYear(term.word);
                    if (isLeft)
                        leftYear = year;
                    else
                        rightYear = year;
                    break;
                case QUARTER:
                    Quarter quarter = Quarter.parse(term.word);
                    if (isLeft)
                        leftQuarter = quarter;
                    else
                        rightQuarter = quarter;
                    break;
                case DATE:
                    if (isLeft)
                        leftDate = term.word;
                    else
                        rightDate = term.word;
                    break;
                case INDEX:
                    FinanceIndex index = FinanceIndex.parse(term.word);
                    if (isLeft) {
                        leftIndex = index;
                        isLeft = false;
                    } else {
                        rightIndex = index;
                    }
                    break;
                case OPERATOR:
                    this.operator = Operator.parse(term.word);
                    break;
                case m:
                    double value = ChineseNumberParser.parse(term.word);
                    this.number = value;
                    if (i > 0 && terms.get(i - 1).word.equals("-")) {
                        this.number = -1 * this.number;
                    }
                    break;
                case nx:
                    if (term.word.equals("%"))
                        percentage = true;
                    break;
            }
        }
    }

    @Override
    public boolean check() {
        if (leftIndex == null)  // 左指标是必须的
            return false;
        if (operator == null)   // 比较符是必须的
            return false;
        rightIndex = leftIndex; // 暂时只支持左右指标相等的情况。
        if (number == null)
            number = 0d;
        if (percentage == null)
            percentage = false;
        if (leftDate == null) {
            leftDate = generateDate(leftYear, leftQuarter);
        }
        if (rightDate == null) {
            rightDate = generateDate(rightYear, rightQuarter);
        }
        return true;
    }

    @Override
    public Answer perform() {
        return stockRepository.findByComplexFinanceIndexStockPickQuestion(this);
    }

    @Override
    public String toString() {
        if (percentage) {
            return String.format("[(%s%s - %s%s) / %s%s %s %s%%]", leftDate, leftIndex, rightDate, rightIndex,
                    rightDate, rightIndex, operator, number);
        } else {
            return String.format("[%s%s - %s%s %s %s]", leftDate, leftIndex.firstAlias(),
                    rightDate, rightIndex.firstAlias(), operator.getSymbol(), number);
        }
    }

    public String getLeftDate() {
        return leftDate;
    }

    public FinanceIndex getLeftIndex() {
        return leftIndex;
    }

    public String getRightDate() {
        return rightDate;
    }

    public FinanceIndex getRightIndex() {
        return rightIndex;
    }

    public Operator getOperator() {
        return operator;
    }

    public Double getNumber() {
        return number;
    }

    public Boolean getPercentage() {
        return percentage;
    }

}

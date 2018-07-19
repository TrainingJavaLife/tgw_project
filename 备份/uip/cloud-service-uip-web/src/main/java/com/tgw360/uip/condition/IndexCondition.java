package com.tgw360.uip.condition;

import java.util.Date;

/**
 * 财务指标查询条件
 * Created by 邹祥 on 2017/12/14 15:05
 */
public class IndexCondition extends AbstractCondition {
    public enum Type {
        SIMPLE,     // 简单类型
        COMPLEX,    // 复杂类型
        WRONG       // 错误类型
    }

    private Type type;               // 类型
    private Integer leftYear;           // 左边的年份，
    private Quarter leftQuarter;        // 左边的季度
    private FinanceIndex leftIndex;     // 左边的指标
    private Operator operator;          // 比较符
    private Integer rightYear;          // 右边的年份
    private Quarter rightQuarter;       // 右边的季度
    private FinanceIndex rightIndex;    // 右边的指标
    private Double number = 0d;              // 比较数
    private Boolean percentage = false;         // 比较数是否带百分比符号

    public String toString() {
        if (type == Type.SIMPLE) {
            return String.format("[%s %s %s %s %s]",
                    leftYear, leftQuarter, leftIndex, operator.getSymbol(), number);
        } else {
            if (percentage)
                return String.format("[(%s %s %s - %s %s %s) / %s %s %s %s %s%%]",
                        leftYear, leftQuarter, leftIndex, rightYear, rightQuarter, rightIndex,
                        rightYear, rightQuarter, rightIndex, operator.getSymbol(), number);
            else
                return String.format("[%s %s %s - %s %s %s %s %s]",
                        leftYear, leftQuarter, leftIndex, rightYear, rightQuarter, rightIndex,
                        operator.getSymbol(), number);
        }
    }


    /**
     * 获取当前年份
     *
     * @return
     */
    public static int thisYear() {
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

    /**
     * 补充完善一些缺失的字段
     *
     * @return
     */
    public Type complete() {
        type = Type.SIMPLE;
        int thisYear = thisYear();
        Quarter thisQuarter = thisQuarter();
        if (leftYear == null)
            leftYear = thisYear;    // 左边默认为今年
        if (leftQuarter == null) {
            if (leftYear == thisYear)
                leftQuarter = thisQuarter;    // 如果是今年，则默认为最新的季度
            else
                leftQuarter = Quarter.FOURTH;   // 如果是往年，则默认为第四季度
        }
        if (leftIndex == null)
            return markWrong();
        if (operator == null) {
            return markWrong();
        }
        if (rightYear != null || rightQuarter != null || rightIndex != null) { // 右侧三项中至少有一项，才判定为复杂类型
            type = Type.COMPLEX;
            if (rightYear == null)
                rightYear = leftYear;       // 右边的年份默认与左边的年份相同
            if (rightQuarter == null)
                if (rightYear == thisYear)
                    rightQuarter = thisQuarter;
                else
                    rightQuarter = Quarter.FOURTH;
            if (rightIndex == null)
                rightIndex = leftIndex;     // 右边指标默认与左边指标相同
        }
        return type;
    }

    private Type markWrong() {
        type = Type.WRONG;
        return type;
    }

    public Type getType() {
        if (type == null)
            return complete();
        return type;
    }

    public void setYear(Integer year, boolean left) {
        if (left) {
            leftYear = year;
        } else {
            rightYear = year;
        }
    }

    public void setQuarter(Quarter quarter, boolean left) {
        if (left) {
            leftQuarter = quarter;
        } else {
            rightQuarter = quarter;
        }
    }

    public void setIndex(FinanceIndex index, boolean left) {
        if (left) {
            leftIndex = index;
        } else {
            rightIndex = index;
        }
    }

    public Integer getLeftYear() {
        return leftYear;
    }

    public void setLeftYear(Integer leftYear) {
        this.leftYear = leftYear;
    }

    public Quarter getLeftQuarter() {
        return leftQuarter;
    }

    public void setLeftQuarter(Quarter leftQuarter) {
        this.leftQuarter = leftQuarter;
    }

    public FinanceIndex getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(FinanceIndex leftIndex) {
        this.leftIndex = leftIndex;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Integer getRightYear() {
        return rightYear;
    }

    public void setRightYear(Integer rightYear) {
        this.rightYear = rightYear;
    }

    public Quarter getRightQuarter() {
        return rightQuarter;
    }

    public void setRightQuarter(Quarter rightQuarter) {
        this.rightQuarter = rightQuarter;
    }

    public FinanceIndex getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(FinanceIndex rightIndex) {
        this.rightIndex = rightIndex;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Boolean getPercentage() {
        return percentage;
    }

    public void setPercentage(Boolean percentage) {
        this.percentage = percentage;
    }

    public String getLeftEndDate() {
        return this.leftYear + "-" + this.leftQuarter;
    }

    public String getRightEndDate() {
        return this.rightYear + "-" + this.rightQuarter;
    }

    public String getLeftLable() {
        return this.leftIndex.firstAlias() + "|" + this.getLeftEndDate().replace("-", "");
    }

    public String getRightLable() {
        return this.rightIndex.firstAlias() + "|" + this.getRightEndDate().replace("-", "");
    }
}

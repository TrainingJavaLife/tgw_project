package com.tgw360.uip.entity.question;

import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.condition.Operator;
import com.tgw360.uip.condition.Quarter;
import org.junit.Test;

import static com.tgw360.uip.entity.question.QuestionUtils.parseToSimilarQuestion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 财务指标选股Question模式串识别  测试
 * Created by 邹祥 on 2017/12/29 14:06
 */
public class FinanceIndexStockPickQuestionRecognitionTest {
    /**
     * 运算符识别
     */
    @Test
    public void testOperator1() {
        Question question = parseToSimilarQuestion("eps>1");
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(Operator.gt, q.getOperator());
        assertEquals(FinanceIndex.BasicEPS, q.getIndex());
        assertEquals(new Double(1), q.getNumber());
    }

    @Test
    public void testOperator2() {
        Question question = parseToSimilarQuestion("每股净资产小于等于5");
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(Operator.lte, q.getOperator());
    }

    @Test
    public void testOperator3() {
        Question question = parseToSimilarQuestion("每股净资产在10以上");
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(Operator.gte, q.getOperator());
    }

    @Test
    public void testOperator4() {
        Question question = parseToSimilarQuestion("eps<=10");
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(Operator.lte, q.getOperator());
    }

    @Test
    public void testOperator5() {
        Question question = parseToSimilarQuestion("eps位于5到10之间");
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertTrue(q.isRange());
        assertEquals(new Double(5), q.getNumber());
        assertEquals(new Double(10), q.getNumber2());
    }

    @Test
    public void testOperator6() {
        Question question = parseToSimilarQuestion("基本每股收益小于0.1，大于0.5");
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(FinanceIndex.BasicEPS, q.getIndex());
        assertTrue(q.isRange());
        assertEquals(new Double(0.1), q.getNumber());
        assertEquals(new Double(0.5), q.getNumber2());
    }

    @Test
    public void testOperator7() {
        Question question = parseToSimilarQuestion("概念包含机器人，太阳能");
        assertEquals(ConceptStockPickQuestion.class, question.getClass());
        ConceptStockPickQuestion q = (ConceptStockPickQuestion) question;
        assertTrue(q.containsConcept("机器人"));
        assertTrue(q.containsConcept("太阳能"));
    }

    /**
     * 时点模式
     */
    @Test
    public void testTime1() {
        Question question = parseToSimilarQuestion("2015年报eps>1");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals("2015-12-31", q.getDate());
    }

    @Test
    public void testTime2() {
        Question question = parseToSimilarQuestion("1季报eps>1");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(FinanceIndexStockPickQuestion.thisYear() + "-03-31", q.getDate());
    }

    @Test
    public void testTime2_2() {
        Question question = parseToSimilarQuestion("eps>1");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals("2017-12-31", q.getDate());
    }

    @Test
    public void testTime3() {
        Question question = parseToSimilarQuestion("2015中年报eps>1");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals("2015-06-30", q.getDate());
    }

    @Test
    public void testTime4() {
        Question question = parseToSimilarQuestion("1季报eps比去年大");
        assertEquals(ComplexFinanceIndexStockPickQuestion.class, question.getClass());
        ComplexFinanceIndexStockPickQuestion q = (ComplexFinanceIndexStockPickQuestion) question;
        assertEquals(FinanceIndexStockPickQuestion.generateDate(null, Quarter.FIRST), q.getLeftDate());
        assertEquals(FinanceIndexStockPickQuestion.generateDate(FinanceIndexStockPickQuestion.thisYear() - 1, null), q.getRightDate());
    }

}

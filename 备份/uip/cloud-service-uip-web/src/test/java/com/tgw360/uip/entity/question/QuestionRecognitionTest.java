package com.tgw360.uip.entity.question;

import com.tgw360.uip.common.Constants;
import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.condition.Operator;
import com.tgw360.uip.condition.Quarter;
import com.tgw360.uip.entity.Document;
import org.junit.Test;

import static com.tgw360.uip.entity.question.QuestionUtils.parseToSimilarQuestion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 单个检索串识别测试 ，同时也作为  自定义分词器测试
 * Created by 邹祥 on 2017/12/28 9:45
 */
public class QuestionRecognitionTest {

    /**
     * 个股行情
     */
    @Test
    public void testStockQuotationQuestion1() {
        Question question = parseToSimilarQuestion("万科A行情");
        assertEquals(StockQuotationQuestion.class, question.getClass());
        StockQuotationQuestion q = (StockQuotationQuestion) question;
        assertEquals("万科A", q.getW());
    }

    @Test
    public void testStockQuotationQuestion2() {
        Question question = parseToSimilarQuestion("行情 000001");
        assertEquals(StockQuotationQuestion.class, question.getClass());
        StockQuotationQuestion q = (StockQuotationQuestion) question;
        assertEquals("000001", q.getW());
    }

    /**
     * 个股资讯
     */
    @Test
    public void testStockDocumentQuestion1() {
        Question question = parseToSimilarQuestion("青岛啤酒的新闻");
        assertEquals(StockDocumentQuestion.class, question.getClass());
        StockDocumentQuestion q = (StockDocumentQuestion) question;
        assertEquals("青岛啤酒", q.getW());
        assertEquals(Document.Type.NEWS, q.getType());
    }

    @Test
    public void testStockDocumentQuestion2() {
        Question question = parseToSimilarQuestion("青岛啤酒的资讯");
        assertEquals(StockDocumentQuestion.class, question.getClass());
        StockDocumentQuestion q = (StockDocumentQuestion) question;
        assertEquals("青岛啤酒", q.getW());
        assertEquals(Document.Type.NEWS2, q.getType());
    }

    @Test
    public void testStockDocumentQuestion3() {
        Question question = parseToSimilarQuestion("公告 万达电影");
        assertEquals(StockDocumentQuestion.class, question.getClass());
        StockDocumentQuestion q = (StockDocumentQuestion) question;
        assertEquals("万达电影", q.getW());
        assertEquals(Document.Type.ANNOUNCEMENT, q.getType());
    }

    @Test
    public void testStockDocumentQuestion4() {
        Question question = parseToSimilarQuestion("研报 300370");
        assertEquals(StockDocumentQuestion.class, question.getClass());
        StockDocumentQuestion q = (StockDocumentQuestion) question;
        assertEquals("300370", q.getW());
        assertEquals(Document.Type.RESEARCH_REPORT, q.getType());
    }

    /**
     * 个股指标
     */
    @Test
    public void testStockFinanceIndexQuestion1() {
        Question question = parseToSimilarQuestion("机器人的EPS");
        assertEquals(StockFinanceIndexQuestion.class, question.getClass());
        StockFinanceIndexQuestion q = (StockFinanceIndexQuestion) question;
        assertEquals("机器人", q.getStock().getName());
        assertEquals(FinanceIndex.BasicEPS, q.getIndex());
    }

    @Test
    public void testStockFinanceIndexQuestion2() {
        Question question = parseToSimilarQuestion("每股净资产 二三四五");
        assertEquals(StockFinanceIndexQuestion.class, question.getClass());
        StockFinanceIndexQuestion q = (StockFinanceIndexQuestion) question;
        assertEquals(q.getStock().getName(), "二三四五");
        assertEquals(FinanceIndex.NetAssetPS, q.getIndex());
    }

    /**
     * 个股详情测试
     */
    @Test
    public void testStockDetailQuestion1() {
        Question question = parseToSimilarQuestion("西部资源");
        assertEquals(StockDetailQuestion.class, question.getClass());
        StockDetailQuestion q = (StockDetailQuestion) question;
        assertEquals("西部资源", q.getW());
    }

    @Test
    public void testStockDetailQuestion2() {
        Question question = parseToSimilarQuestion("000421");
        assertEquals(StockDetailQuestion.class, question.getClass());
        StockDetailQuestion q = (StockDetailQuestion) question;
        assertEquals("000421", q.getW());
    }

    /**
     * 概念选股测试
     */
    @Test
    public void testConceptStockPickQuestion1() {
        Question question = parseToSimilarQuestion("概念是机器人");
        assertEquals(ConceptStockPickQuestion.class, question.getClass());
        ConceptStockPickQuestion q = (ConceptStockPickQuestion) question;
        assertTrue(q.containsConcept("机器人"));
    }

    @Test
    public void testConceptStockPickQuestion2() {
        Question question = parseToSimilarQuestion("智能家居");
        assertEquals(ConceptStockPickQuestion.class, question.getClass());
        ConceptStockPickQuestion q = (ConceptStockPickQuestion) question;
        assertTrue(q.containsConcept("智能家居"));
    }

    @Test
    public void testConceptStockPickQuestion3() {
        Question question = parseToSimilarQuestion("概念正好是人工智能 AlphaGO");
        assertEquals(ConceptStockPickQuestion.class, question.getClass());
        ConceptStockPickQuestion q = (ConceptStockPickQuestion) question;
        assertTrue(q.containsConcept("人工智能"));
    }

    /**
     * 财务指标选股
     */
    @Test
    public void testFinanceIndexStockPickQuestion1() {
        Question question = parseToSimilarQuestion("EPS >= 一");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(FinanceIndexStockPickQuestion.generateDate(null, null), q.getDate());
        assertEquals(FinanceIndex.BasicEPS, q.getIndex());
        assertEquals(Operator.gte, q.getOperator());
        assertTrue(Math.abs(q.getNumber() - 1) < Constants.INFINITESIMAL);
    }

    @Test
    public void testFinanceIndexStockPickQuestion2() {
        Question question = parseToSimilarQuestion("每股净资产在10以下");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(FinanceIndexStockPickQuestion.generateDate(null, null), q.getDate());
        assertEquals(FinanceIndex.NetAssetPS, q.getIndex());
        assertEquals(Operator.lte, q.getOperator());
        assertTrue(Math.abs(q.getNumber() - 10) < Constants.INFINITESIMAL);
    }

    @Test
    public void testFinanceIndexStockPickQuestion3() {
        Question question = parseToSimilarQuestion("2015年三季报每股净资产大于10，小于20");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(new Integer(2015), q.getYear());
        assertEquals(Quarter.THIRD, q.getQuarter());
        assertEquals(FinanceIndex.NetAssetPS, q.getIndex());
        assertTrue(Math.abs(q.getNumber() - 10) < Constants.INFINITESIMAL);
        assertTrue(Math.abs(q.getNumber2() - 20) < Constants.INFINITESIMAL);
    }

    @Test
    public void testFinanceIndexStockPickQuestion4() {
        Question question = parseToSimilarQuestion("EPS 在0.5,1.5之间");
        assertEquals(SimpleFinanceIndexStockPickQuestion.class, question.getClass());
        SimpleFinanceIndexStockPickQuestion q = (SimpleFinanceIndexStockPickQuestion) question;
        assertEquals(FinanceIndexStockPickQuestion.generateDate(null, null), q.getDate());
        assertEquals(FinanceIndex.BasicEPS, q.getIndex());
        assertTrue(Math.abs(q.getNumber() - 0.5) < Constants.INFINITESIMAL);
        assertTrue(Math.abs(q.getNumber2() - 1.5) < Constants.INFINITESIMAL);
    }

    /**
     * 复杂类型的财务指标选股Question
     */
    @Test
    public void testComplexFinanceIndexStockPickQuestion1() {
        Question question = parseToSimilarQuestion("2015年中报EPS 小于 2016年");
        assertEquals(ComplexFinanceIndexStockPickQuestion.class, question.getClass());
        ComplexFinanceIndexStockPickQuestion q = (ComplexFinanceIndexStockPickQuestion) question;
        assertEquals("2015-06-30", q.getLeftDate());
        assertEquals(FinanceIndex.BasicEPS, q.getLeftIndex());
        assertEquals("2016-12-31", q.getRightDate());
        assertEquals(Operator.lt, q.getOperator());
        assertTrue(Math.abs(q.getNumber() - 0) < Constants.INFINITESIMAL);
    }

    @Test
    public void testComplexFinanceIndexStockPickQuestion2() {
        Question question = parseToSimilarQuestion("2017-06-30 EPS 比 2016年1季报 大 50%");
        assertEquals(ComplexFinanceIndexStockPickQuestion.class, question.getClass());
        ComplexFinanceIndexStockPickQuestion q = (ComplexFinanceIndexStockPickQuestion) question;
        assertEquals("2017-06-30", q.getLeftDate());
        assertEquals(FinanceIndex.BasicEPS, q.getLeftIndex());
        assertEquals("2016-03-31", q.getRightDate());
        assertEquals(Operator.gt, q.getOperator());
        assertTrue(Math.abs(q.getNumber() - 50) < Constants.INFINITESIMAL);
        assertTrue(q.getPercentage());
    }

}

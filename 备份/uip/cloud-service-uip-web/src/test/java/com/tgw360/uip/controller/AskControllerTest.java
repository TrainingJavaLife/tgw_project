package com.tgw360.uip.controller;

import com.tgw360.uip.entity.Document;
import com.tgw360.uip.entity.answer.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by 邹祥 on 2017/12/26 10:15
 */
public class AskControllerTest {

    private AskController controller = new AskController();

    /**
     * 个股行情
     */
    @Test
    public void testStockQuotation1() {
        Answer answer = controller.ask("万科A 行情");
        assertEquals(answer.getClass(), StockQuotationAnswer.class);
        StockQuotationAnswer a = (StockQuotationAnswer) answer;
        assertEquals(a.getStockCode(), "000002");
    }

    /**
     * 单个股票
     */
    @Test
    public void testStockDetail1() {
        Answer answer = controller.ask("科大讯飞");
        assertEquals(answer.getClass(), StockDetailAnswer.class);
        StockDetailAnswer a = (StockDetailAnswer) answer;
        assertEquals(a.getStock().getName(), "科大讯飞");
    }

    /**
     * 条件选股
     */
    @Test
    public void testStockPick1() {
        Answer answer = controller.ask("概念包含智能家居，机器人");
        assertEquals(answer.getClass(), StockPickAnswer.class);
        StockPickAnswer a = (StockPickAnswer) answer;
        assertTrue(a.getMap().containsKey("000333"));   // 美的集团
    }

    @Test
    public void testStockPick2() {
        Answer answer = controller.ask("去年三季报基本每股收益大于1.5;白酒");
//        System.out.println(answer);
    }

    @Test
    public void testStockPick3() {
        Answer answer = controller.ask("去年三季报基本每股收益比前年高10%;白酒");
//        System.out.println(answer);
    }

    /**
     * 个股资讯
     */
    @Test
    public void testStockDocument1() {
        Answer answer = controller.ask("伊利股份的研报");
        assertEquals(answer.getClass(), StockDocumentAnswer.class);
        StockDocumentAnswer a = (StockDocumentAnswer) answer;
        for (Document document : a.getDocuments()) {
            assertEquals(Document.Type.parse(document.getType()), Document.Type.RESEARCH_REPORT);
        }
    }

    @Test
    public void testStockDocument2() {
        Answer answer = controller.ask("公告西部证券");
        assertEquals(answer.getClass(), StockDocumentAnswer.class);
        StockDocumentAnswer a = (StockDocumentAnswer) answer;
        for (Document document : a.getDocuments()) {
            assertEquals(Document.Type.parse(document.getType()), Document.Type.ANNOUNCEMENT);
        }
    }

    /**
     * 个股指标
     */
    @Test
    public void testStockFinanceIndex1() {
        Answer answer = controller.ask("龙蟒佰利的eps");
        assertEquals(answer.getClass(), StockFinanceIndexAnswer.class);
    }
}

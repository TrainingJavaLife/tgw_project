package com.tgw360.uip.entity.answer;

import com.tgw360.uip.entity.Stock;

/**
 * Answer——股票行情
 * Created by 邹祥 on 2017/12/27 13:23
 */
public class StockQuotationAnswer extends AnswerAdaptor {

    private Stock stock;

    public String getStockName() {
        return stock.getName();
    }

    public String getStockCode() {
        return stock.getCode();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

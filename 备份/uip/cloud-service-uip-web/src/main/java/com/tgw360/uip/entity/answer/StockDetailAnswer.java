package com.tgw360.uip.entity.answer;


import com.tgw360.uip.entity.Stock;

/**
 * Answer——股票详情
 * Created by 邹祥 on 2017/12/25 14:01
 */
public class StockDetailAnswer extends AnswerAdaptor {

    private Stock stock;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

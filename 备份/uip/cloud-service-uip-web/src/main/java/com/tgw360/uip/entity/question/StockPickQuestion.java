package com.tgw360.uip.entity.question;

/**
 * Question——选股
 * Created by 邹祥 on 2017/12/26 9:02
 */
public abstract class StockPickQuestion extends QuestionAdaptor {

    /**
     * 添加一个子问题
     *
     * @param question
     */
    public void add(StockPickQuestion question) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    public void remove(StockPickQuestion question) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    /**
     * 获取第i个子问题
     *
     * @param i
     * @return
     */
    public StockPickQuestion getChild(int i) {
        throw new UnsupportedOperationException("不支持的操作");
    }
}

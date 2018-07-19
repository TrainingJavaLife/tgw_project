package com.tgw360.uip.service;

import com.tgw360.uip.Table.Table;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.common.TableBean;
import com.tgw360.uip.entity.Document;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.query.SearchByConditionQuery;

import java.io.IOException;
import java.util.List;

/**
 * 股票service
 * Created by 邹祥 on 2017/11/22 9:05
 */
public interface StockService {

    /**
     * 根据前缀(股票代码，股票名称，拼音简称)查询股票
     *
     * @param prefix
     * @return
     */
    List<Stock> findByPrefix(String prefix);

    /**
     * 根据股票代码或者名称查询股票
     *
     * @param w
     * @return
     * @throws IOException
     */
    Stock findByCodeOrName(String w);

    /**
     * 股票Id查询相关：1.新闻 2.公告 3.研报
     * 分页展示 pageNum 第几页 pageSize 每页显示多少条
     *
     * @param id
     * @param type
     * @return
     */
//    PageBean<Document> getStockRelevance(long id, Integer type, Integer pageNum, Integer pageSize);

    /**
     * 根据多个条件查询股票
     *
     * @param query
     * @return
     */
    Table findByConditions(SearchByConditionQuery query) throws Exception;
}

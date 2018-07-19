package com.tgw360.uip.service;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.entity.Stock;

import java.io.IOException;
import java.util.List;

/**
 * 新闻service
 * Created by 危宇 on 2017/11/23 10:44
 */
public interface NewsService {

    /**
     * 根据ID查询新闻详情
     *
     * @param Id
     * @return
     * @throws IOException
     */
    News findNewsById(long Id);


    /**
     * 根据输入的脚本查询新闻并且高亮显示
     * @param script
     * @return
     */
    List<News> searchByScript(String script);

    /**
     * 股票Id查询相关：1.新闻 2.公告 3.研报
     * 分页展示 pageNum 第几页 pageSize 每页显示多少条
     * @param Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageBean<News> getRelevanceNewsById(long Id,Integer pageNum, Integer pageSize);

}

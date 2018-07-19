package com.tgw360.uip.service;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.entity.Intellisense;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.query.FullTextQuery;

import java.util.List;


/**
 * 搜索service
 * Created by 危宇 on 2017/12/7 9:57
 */
public interface SearchService {

    /**
     * 输入关键字全文搜索标题或内容
     *
     * @param fullTextQuery
     * @return
     */
    PageBean<News> searchNewsByKeyword(FullTextQuery fullTextQuery);

    /**
     * 输入关键字全文搜索公告标题或内容
     *
     * @param fullTextQuery
     * @return
     */
    PageBean<Announcement> searchAnnouncementByKeyword(FullTextQuery fullTextQuery);

    /**
     * 输入关键字搜索研报标题或内容
     *
     * @param fullTextQuery
     * @return
     */
    PageBean<ResearchReport> searchReportByKeyword(FullTextQuery fullTextQuery);

    /**
     * 搜索时的智能提示
     *
     * @param prefix
     * @return
     */
    List<Intellisense> suggestByPrefix(String prefix);
}

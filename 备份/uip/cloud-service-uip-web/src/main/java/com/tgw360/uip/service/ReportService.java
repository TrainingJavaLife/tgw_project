package com.tgw360.uip.service;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.ResearchReport;

/**
 * 研报service
 * Created by 危宇 on 2017/12/18 10:55
 */
public interface ReportService {

    /**
     * 根据研报Id查找研报详情
     * @param Id
     * @return
     */
    ResearchReport findReportById(long Id);

    /**
     * 股票Id查询相关：1.新闻 2.公告 3.研报
     * 分页展示 pageNum 第几页 pageSize 每页显示多少条
     * @param Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageBean<ResearchReport> getRelevanceReportById(long Id, Integer pageNum, Integer pageSize);

}

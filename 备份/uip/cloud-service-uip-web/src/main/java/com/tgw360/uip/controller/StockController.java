package com.tgw360.uip.controller;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.*;
import com.tgw360.uip.service.AnnouncementService;
import com.tgw360.uip.service.NewsService;
import com.tgw360.uip.service.ReportService;
import com.tgw360.uip.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by 邹祥 on 2017/11/22 9:10
 */
@RestController
public class StockController {
    @Autowired
    private StockService stockService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private ReportService reportService;

    /**
     * 测试Controller抛出异常的情况
     *
     * @return
     */
    @GetMapping("/test/exception")
    public Object testException() {
        throw new RuntimeException("哈哈哈");
    }

    /**
     * 股票ID以及类型查询股票相关--type:1.新闻 2.公告 3.研报
     *
     * @param id   股票ID
     * @param type 请求类型
     * @return
     * @throws Exception
     */
    @GetMapping("/stock/{id}/docs")
    public Object getStockRelevance(@PathVariable("id") long id,@RequestParam(value = "type",required = false,defaultValue = "1") Integer type,
                                    @RequestParam(value="pageNum",required = false,defaultValue = "1") Integer pageNum,
                                    @RequestParam(value="pageSize",required = false,defaultValue = "10") Integer pageSize) throws Exception {
        CommonResult<PageBean<? extends Document>> commonResult = new CommonResult<>();
        if (type == 1) {
            PageBean<News> pageBean = newsService.getRelevanceNewsById(id,pageNum,pageSize);
            commonResult.setData(pageBean);
        } else if (type == 2) {
            PageBean<Announcement> pageBean = announcementService.getRelevanceAnnouncementById(id,pageNum,pageSize);
            commonResult.setData(pageBean);
        } else if (type == 3) {
            PageBean<ResearchReport> pageBean = reportService.getRelevanceReportById(id,pageNum,pageSize);
            commonResult.setData(pageBean);
        }
        return commonResult;
    }

}

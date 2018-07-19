package com.tgw360.uip.controller;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.*;
import com.tgw360.uip.query.FullTextQuery;
import com.tgw360.uip.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 危宇 on 2017/12/7 10:34
 */
@RestController
public class TextSearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 全文搜索
     * @param fullTextQuery
     * @return
     */
    @GetMapping(value = "/search/keyword")
    public Object searchTextByKeyword(FullTextQuery fullTextQuery){
        CommonResult<PageBean<? extends Document>> commonResult = new CommonResult<>();
        if (fullTextQuery.getType() == 1){
            PageBean<News> newsPageBean = searchService.searchNewsByKeyword(fullTextQuery);
            commonResult.setData(newsPageBean);
        }else if (fullTextQuery.getType() == 2){
            PageBean<Announcement> announcementPageBean = searchService.searchAnnouncementByKeyword(fullTextQuery);
            commonResult.setData(announcementPageBean);
        }else if (fullTextQuery.getType() == 3){
            PageBean<ResearchReport> reportPageBean = searchService.searchReportByKeyword(fullTextQuery);
            commonResult.setData(reportPageBean);
        }
        return commonResult;
    }

    /**
     * 智能提示
     * @param prefix
     * @return
     */
    @GetMapping("/search/intellisense")
    public Object suggestByPrefix(String prefix){
        CommonResult<List<Intellisense>> commonResult = new CommonResult<>();
        List<Intellisense> list = searchService.suggestByPrefix(prefix);
        commonResult.setData(list);
        return commonResult;
    }
}

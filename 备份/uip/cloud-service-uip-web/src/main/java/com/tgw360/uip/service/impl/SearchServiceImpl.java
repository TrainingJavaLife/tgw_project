package com.tgw360.uip.service.impl;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.entity.Intellisense;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.query.FullTextQuery;
import com.tgw360.uip.repository.SearchRepository;
import com.tgw360.uip.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by 危宇 on 2017/12/7 14:34
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchRepository searchRepository;

    @Override
    public PageBean<News> searchNewsByKeyword(FullTextQuery fullTextQuery) {
        return searchRepository.searchNewsByKeyword(fullTextQuery);
    }

    @Override
    public PageBean<Announcement> searchAnnouncementByKeyword(FullTextQuery fullTextQuery) {
        return searchRepository.searchAnnouncementByKeyword(fullTextQuery);
    }

    @Override
    public PageBean<ResearchReport> searchReportByKeyword(FullTextQuery fullTextQuery) {
        return searchRepository.searchReportByKeyword(fullTextQuery);
    }

    @Override
    public List<Intellisense> suggestByPrefix(String prefix) {
        return searchRepository.suggestByPrefix(prefix);
    }


}

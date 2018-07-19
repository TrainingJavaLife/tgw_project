package com.tgw360.uip.service.impl;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.repository.NewsRepository;
import com.tgw360.uip.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by 危宇 on 2017/11/23 10:35
 */
@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public News findNewsById(long Id) {
        return newsRepository.findNewsById(Id);
    }

    @Override
    public List<News> searchByScript(String script) {
        return newsRepository.searchByScript(script);
    }

    @Override
    public PageBean<News> getRelevanceNewsById(long Id, Integer pageNum, Integer pageSize) {
        return newsRepository.getRelevanceNewsById(Id,pageNum,pageSize);
    }

}

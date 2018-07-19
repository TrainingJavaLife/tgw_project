package com.tgw360.uip.service;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.Announcement;

import java.io.IOException;

/**
 * 公告service
 * Created by 危宇 on 2017/11/23 14:26
 */
public interface AnnouncementService {

    /**
     * 根据公告Id查询公告详情
     * @param Id
     * @return
     * @throws IOException
     */
    Announcement findAnnouncementById(long Id) throws IOException;

    /**
     * 股票Id查询相关：1.新闻 2.公告 3.研报
     * 分页展示 pageNum 第几页 pageSize 每页显示多少条
     * @param Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageBean<Announcement> getRelevanceAnnouncementById(long Id, Integer pageNum, Integer pageSize);

}

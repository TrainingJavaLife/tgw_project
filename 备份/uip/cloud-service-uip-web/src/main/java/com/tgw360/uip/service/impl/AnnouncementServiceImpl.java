package com.tgw360.uip.service.impl;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.repository.AnnouncementRepository;
import com.tgw360.uip.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by 危宇 on 2017/11/23 14:30
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService{

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public Announcement findAnnouncementById(long Id) throws IOException {
        return announcementRepository.findAnnouncementById(Id);
    }

    @Override
    public PageBean<Announcement> getRelevanceAnnouncementById(long Id, Integer pageNum, Integer pageSize) {
        return announcementRepository.getRelevanceAnnouncementById(Id,pageNum,pageSize);
    }

}

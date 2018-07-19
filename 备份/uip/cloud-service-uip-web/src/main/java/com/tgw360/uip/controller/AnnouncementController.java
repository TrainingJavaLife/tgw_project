package com.tgw360.uip.controller;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by 危宇 on 2017/11/23 14:35
 */
@RestController
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 公告Id查询公告详情
     * /announcement/{id}
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/announcement/{id}")
    public Object findAnnouncementById(@PathVariable("id") long id) throws Exception {
        Announcement announcement = announcementService.findAnnouncementById(id);
        CommonResult<Announcement> commonResult = new CommonResult<>();
        commonResult.setData(announcement);
        return commonResult;

    }
}

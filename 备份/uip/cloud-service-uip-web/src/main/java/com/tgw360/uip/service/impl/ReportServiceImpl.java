package com.tgw360.uip.service.impl;

import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.repository.ReportRepository;
import com.tgw360.uip.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 危宇 on 2017/12/18 10:56
 */
@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public ResearchReport findReportById(long Id) {
        return reportRepository.findReportById(Id);
    }

    @Override
    public PageBean<ResearchReport> getRelevanceReportById(long Id, Integer pageNum, Integer pageSize) {
        return reportRepository.getRelevanceReportById(Id,pageNum,pageSize);
    }
}

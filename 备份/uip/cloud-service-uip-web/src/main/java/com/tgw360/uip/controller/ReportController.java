package com.tgw360.uip.controller;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 危宇 on 2017/12/11 20:22
 */
@RestController
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    /**
     * 研报Id查询研报详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/research-reports/{id}")
    public Object findReportById(@PathVariable("id") Long id){
        assertNotNull(id,"研报id不能为空");
        ResearchReport report = reportService.findReportById(id);
        CommonResult<ResearchReport> commonResult = new CommonResult<>();
        commonResult.setData(report);
        return commonResult;
    }
}

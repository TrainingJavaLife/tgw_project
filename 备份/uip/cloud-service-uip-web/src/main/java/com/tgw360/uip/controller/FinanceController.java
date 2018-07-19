package com.tgw360.uip.controller;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.entity.Finance;
import com.tgw360.uip.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 财务报告Controller
 * Created by 邹祥 on 2017/12/5 14:45
 */
@RestController
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    /**
     * 获取股票最新的几个财务报告
     *
     * @param id  股票ID
     * @param num 需要获取的财务报告数目
     * @return
     */
    @GetMapping("/stock/{id}/finance")
    public Object findFinanceByStockId(@PathVariable long id, @RequestParam(defaultValue = "4") int num) {
        List<Finance> finances = financeService.findLatest(id, num);
        return new CommonResult<>(finances);
    }
}

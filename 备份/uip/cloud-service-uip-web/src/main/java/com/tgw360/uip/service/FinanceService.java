package com.tgw360.uip.service;

import com.tgw360.uip.entity.Finance;

import java.util.List;

/**
 * Created by 邹祥 on 2017/12/5 14:42
 */
public interface FinanceService {
    /**
     * 获取股票最新的几个财务报告
     *
     * @param id  股票ID
     * @param num 财务报告数目
     * @return
     */
    List<Finance> findLatest(long id, int num);
}

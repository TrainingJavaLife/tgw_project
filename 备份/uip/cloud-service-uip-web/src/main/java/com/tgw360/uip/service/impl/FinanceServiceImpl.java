package com.tgw360.uip.service.impl;

import com.tgw360.uip.entity.Finance;
import com.tgw360.uip.repository.FinanceRepository;
import com.tgw360.uip.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 邹祥 on 2017/12/5 14:44
 */
@Service
public class FinanceServiceImpl implements FinanceService {
    @Autowired
    private FinanceRepository financeRepository;

    @Override
    public List<Finance> findLatest(long id, int num) {
        return financeRepository.findLatest(id, num);
    }
}

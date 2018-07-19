package com.tgw360.uip.common;

import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.repository.StockRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 一些常用的量
 * TODO: 动态更新
 * Created by 邹祥 on 2017/12/27 14:27
 */
public class CacheUtils {

    static Map<String, Stock> allStockMap = SpringUtils.getBean(StockRepository.class).findAllStockBasicInfo();

    /**
     * 获取所有的股票代码
     *
     * @return
     */
    public static Set<String> getAllStockCodes() {
        return Collections.unmodifiableSet(allStockMap.keySet());
    }

    /**
     * 将股票简称（或股票代码）转换成股票代码。
     *
     * @param keyword
     * @return 找不到或结果不唯一则返回null
     */
    public static Stock convertToStockCode(String keyword) {
        for (Stock stock : allStockMap.values()) {
            if (stock.getName().equals(keyword) || stock.getCode().equals(keyword))
                return stock;
        }
        return null;    // 找不到对应的股票
    }
}

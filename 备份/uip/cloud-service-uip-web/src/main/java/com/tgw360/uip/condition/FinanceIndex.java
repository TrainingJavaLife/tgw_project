package com.tgw360.uip.condition;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 财务指标
 * Created by 邹祥 on 2017/11/27 9:45
 */
public enum FinanceIndex {
    /**
     * 基本每股收益
     */
    BasicEPS(
            "元/股",
            new String[]{"基本每股收益", "EPS"}),
    /**
     * 每股净资产
     */
    NetAssetPS(
            "元/股",
            new String[]{"每股净资产"}),
    /**
     * 销售毛利率
     */
    GrossIncomeRatio(
            "%",
            new String[]{"销售毛利率"}),
    /**
     * 营业收入同比
     */
    OperatingRevenueGrowRate(
            "%",
            new String[]{"营业收入同比"}),
    /**
     * 净资产收益率
     */
    ROE(
            "%",
            new String[]{"净资产收益率"}),;

    static Map<String, FinanceIndex> mapping; // 从名称到ES字段名的映射

    static { // 用于初始化mapping
        HashMap<String, FinanceIndex> hashMap = new HashMap<>();
        for (FinanceIndex fi : FinanceIndex.values()) {
            for (String s : fi.aliases) {
                hashMap.put(s, fi);
            }
        }
        mapping = Collections.unmodifiableMap(hashMap);
    }

    private String unit;    // 计量单位

    private String[] aliases;   // 名称列表

    FinanceIndex(String unit, String[] aliases) {
        Assert.notEmpty(aliases, "财务指标至少要有一个名称");
        this.unit = unit;
        this.aliases = aliases;
    }

    public String firstAlias() {
        return aliases[0];
    }


    /**
     * 根据名称构造财务指标
     *
     * @param name
     * @return
     */
    public static FinanceIndex parse(String name) {
        return mapping.get(name);
    }

    public static Map<String, FinanceIndex> getMapping() {
        return mapping;
    }
}

package com.tgw360.uip.common;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ES查询语句
 * Created by 邹祥 on 2017/11/28 15:19
 */
public class ESQuery {
    private static Map<String, String> map = new HashMap<>();

    /**
     * 从文件中读取一条ES查询语句
     *
     * @param name
     * @return
     */
    public static String read(String name) {
        String value = map.get(name);
        if (value != null) {
            return value;
        }
        InputStream is = ESQuery.class.getClassLoader().getResourceAsStream(String.format("ESQuery/%s.txt", name));
        try {
            String str = IOUtils.toString(is);
            map.put(name, str);
            return str;
        } catch (IOException e) {
            throw new RuntimeException("找不到文件: " + name, e);
        }
    }

    /**
     * 根据条件查询股票
     */
    public static final String SEARCH_STOCK_BY_CONDITIONS = "SearchStockByConditions";
    /**
     * 简单的财务条件查询语句
     */
    public static final String SIMPLE_FINANCE_CONDITION = "SimpleFinanceCondition";
    /**
     * 复杂的财务条件查询语句
     */
    public static final String COMPLEX_FINANCE_CONDITION = "ComplexFinanceCondition";
    /**
     * 概念查询语句
     */
    public static final String CONCEPT_CONDITION = "ConceptCondition";

    /**
     * 全文搜索条件1查询语句
     */
    public static final String FULLTEXT_CONDITION1 = "wy-FullTextCondition1";
    public static final String FULLTEXT_CONDITION2 = "wy-FullTextCondition2";
}

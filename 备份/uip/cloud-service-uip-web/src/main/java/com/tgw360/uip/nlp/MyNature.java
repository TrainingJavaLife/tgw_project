package com.tgw360.uip.nlp;

import com.hankcs.hanlp.corpus.tag.Nature;

/**
 * 词性。大写的为自定义词性，小写的HanLP自带的词性。
 */
public enum MyNature {
    UNKNOWN,    // 未知的
    STOCK,      // 股票
    YEAR,       // 年份
    QUARTER,    // 季度
    DATE,       // 日期。例如：20101010，2010-11-11， 2010-12-11
    INDEX,      // 财务指标
    OPERATOR,   // 比较符
    CONCEPT,    // 概念
    m,          // 数词
    nx,         // 标点符号
    KEYWORD_HANGQING,     // 关键字："行情"
    KEYWORD_GAINIAN,      // 关键字："概念"
    KEYWORD_DOCUMENT,     // 关键字："新闻", "公告", "研报"
    ;

    public static MyNature parse(String name) {
        try {
            return MyNature.valueOf(name);
        } catch (Exception e) {
            return MyNature.UNKNOWN;
        }
    }

    public boolean eqaulTo(Nature nature) {
        return this.name().equals(nature.name());
    }
}
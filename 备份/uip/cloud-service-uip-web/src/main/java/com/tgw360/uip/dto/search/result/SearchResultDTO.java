package com.tgw360.uip.dto.search.result;

import com.tgw360.uip.common.BaseObject;

/**
 * 搜索结果DTO
 * Created by 邹祥 on 2017/12/4 19:00
 */
public abstract class SearchResultDTO extends BaseObject {
    enum Type {
        SINGLE_STOCK,   //匹配单只股票
        SINGLE_CONCEPT, // 匹配单个概念
        FUZZY_CONCEPT,  // 模糊匹配单个概念
        CONDITION,    // 条件搜索
        NONE,    // 无内容
    }

    private Type type; // 类型
    private String w; // 用户输入的关键字

    public SearchResultDTO(String w) {
        this.w = w;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }
}

package com.tgw360.uip.dto.search.result;

import java.util.List;

/**
 * 搜索结果DTO——按条件搜索
 * Created by 邹祥 on 2017/12/14 17:29
 */
public class ConditionSearchResultDTO extends SearchResultDTO {
    {
        this.setType(Type.CONDITION);
    }

    public ConditionSearchResultDTO(String w) {
        super(w);
    }

    private List<String> conditions; // 条件的字符串形式
    private String token;

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

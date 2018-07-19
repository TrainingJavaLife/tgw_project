package com.tgw360.uip.dto;

/**
 * 搜索股票时的建议
 * Created by 邹祥 on 2017/12/5 11:18
 */
public class SearchSuggestionsDTO {
    private String code;
    private String name;
    private String chiSpelling;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChiSpelling() {
        return chiSpelling;
    }

    public void setChiSpelling(String chiSpelling) {
        this.chiSpelling = chiSpelling;
    }
}

package com.tgw360.uip.dto;

/**
 * Created by 危宇 on 2017/12/29 14:51
 */
public class SearchIntellisenseDTO {
    private String code;
    private String name;
    private String chiSpelling;
    private String pinyin;
    private String conceptName;
    private String conceptPinyin;

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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getConceptPinyin() {
        return conceptPinyin;
    }

    public void setConceptPinyin(String conceptPinyin) {
        this.conceptPinyin = conceptPinyin;
    }
}

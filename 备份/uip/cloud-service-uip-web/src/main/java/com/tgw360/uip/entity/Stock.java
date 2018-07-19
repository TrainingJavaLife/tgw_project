package com.tgw360.uip.entity;

import com.tgw360.uip.common.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 股票实体类
 * Created by 邹祥 on 2017/11/20 15:01
 */
public class Stock extends BaseObject {
    private Long id;
    private String code; // 股票代码
    private String name; // 股票名称
    private String chiSpelling; // 拼音简称
    private String industry; // 所属行业
    private String city;  // 城市
    private String businessMajor; // 主营产品
    private List<Concept> concepts = new ArrayList<>(); // 所属概念
    private List<Finance> finances = new ArrayList<>(); // 财务报告

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChiSpelling() {
        return chiSpelling;
    }

    public void setChiSpelling(String chiSpelling) {
        this.chiSpelling = chiSpelling;
    }

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBusinessMajor() {
        return businessMajor;
    }

    public void setBusinessMajor(String businessMajor) {
        this.businessMajor = businessMajor;
    }

    public List<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public List<Finance> getFinances() {
        return finances;
    }

    public void setFinances(List<Finance> finances) {
        this.finances = finances;
    }
}

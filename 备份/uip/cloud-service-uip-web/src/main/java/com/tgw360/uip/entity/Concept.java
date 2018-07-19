package com.tgw360.uip.entity;

import com.tgw360.uip.common.BaseObject;

/**
 * 概念实体类
 * Created by 邹祥 on 2017/11/20 15:09
 */
public class Concept extends BaseObject {
    private Long id;
    private String name;  // 概念名称
    private String remark;  // 概念含义

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return name;
    }
}

package com.tgw360.uip.service;

import com.tgw360.uip.entity.Concept;

import java.util.List;

/**
 * 概念Service
 * Created by 邹祥 on 2017/12/5 10:03
 */
public interface ConceptService {
    /**
     * 根据概念名称查询概念
     *
     * @param name
     * @return
     */
    public Concept findByName(String name);

}

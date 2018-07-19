package com.tgw360.uip.service.impl;

import com.tgw360.uip.entity.Concept;
import com.tgw360.uip.repository.ConceptRepository;
import com.tgw360.uip.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by 邹祥 on 2017/12/5 10:03
 */
@Service
public class ConceptServiceImpl implements ConceptService {

    @Autowired
    private ConceptRepository conceptRepository;

    private List<Concept> allConcepts;

    /**
     * Bean的初始化
     */
    @PostConstruct
    private void postConstruct() {
        allConcepts = conceptRepository.findAll();
    }

    @Override
    public Concept findByName(String name) {
        return conceptRepository.findByName(name);
    }

}

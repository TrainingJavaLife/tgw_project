package com.tgw360.uip.dto.search.result;

import com.tgw360.uip.entity.Concept;

/**
 * 搜索结果DTO——单个概念条件
 * Created by 邹祥 on 2017/12/5 9:49
 */
public class SingleConceptSearchResultDTO extends ConditionSearchResultDTO {
    {
        this.setType(Type.SINGLE_CONCEPT);
    }

    private Concept concept;

    public SingleConceptSearchResultDTO(String w) {
        super(w);
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}

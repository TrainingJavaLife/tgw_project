package com.tgw360.uip.dto.search.result;

import java.util.List;

/**
 * 搜索结果DTO——模糊匹配概念
 * Created by 邹祥 on 2017/12/14 13:47
 */
public class FuzzyConceptSearchResultDTO extends SearchResultDTO {

    {
        this.setType(Type.FUZZY_CONCEPT);
    }

    public FuzzyConceptSearchResultDTO(String w) {
        super(w);
    }

    private List<String> possibleConcepts;

    public List<String> getPossibleConcepts() {
        return possibleConcepts;
    }

    public void setPossibleConcepts(List<String> possibleConcepts) {
        this.possibleConcepts = possibleConcepts;
    }
}

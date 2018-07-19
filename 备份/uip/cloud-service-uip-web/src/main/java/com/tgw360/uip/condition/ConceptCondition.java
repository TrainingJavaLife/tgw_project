package com.tgw360.uip.condition;

/**
 * 概念查询条件
 * Created by 邹祥 on 2017/11/27 9:37
 */
public class ConceptCondition extends AbstractCondition {
    private String concept;

    @Override
    public String toString() {
        return String.format("[概念：%s]", concept);
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

}

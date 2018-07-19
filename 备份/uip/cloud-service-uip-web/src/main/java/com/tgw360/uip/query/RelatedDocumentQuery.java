package com.tgw360.uip.query;

/**
 * 股票相关文档Query
 * Created by 邹祥 on 2017/12/5 15:58
 */
public class RelatedDocumentQuery extends PageQuery {
    private Long id; // 股票ID
    private Integer type; // 类型，参照 Document.TYPE_NEWS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

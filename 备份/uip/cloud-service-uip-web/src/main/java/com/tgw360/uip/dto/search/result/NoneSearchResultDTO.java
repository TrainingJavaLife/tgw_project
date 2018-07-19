package com.tgw360.uip.dto.search.result;

/**
 * 搜索结果DTO——无内容
 * Created by 邹祥 on 2017/12/14 14:18
 */
public class NoneSearchResultDTO extends SearchResultDTO {
    {
        this.setType(Type.NONE);
    }

    public NoneSearchResultDTO(String w) {
        super(w);
    }


}

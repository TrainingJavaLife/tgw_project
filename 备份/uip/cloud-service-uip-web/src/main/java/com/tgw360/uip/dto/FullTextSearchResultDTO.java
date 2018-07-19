package com.tgw360.uip.dto;

import com.tgw360.uip.dto.search.result.SearchResultDTO;

/**
 * 关键字搜索结果DTO
 * Created by 邹祥 on 2017/12/5 11:08
 */
public class FullTextSearchResultDTO extends SearchResultDTO {
    {
        //this.setType(TYPE_FULL_TEXT);
    }
    public FullTextSearchResultDTO(String w) {
        super(w);
    }
}

package com.tgw360.uip.dto.search.result;

import com.tgw360.uip.dto.search.result.SearchResultDTO;
import com.tgw360.uip.entity.Stock;

/**
 * 搜索结果DTO——单只股票
 * Created by 邹祥 on 2017/12/4 19:00
 */
public class SingleStockSearchResultDTO extends SearchResultDTO {

    {
        this.setType(Type.SINGLE_STOCK);
    }

    private Stock stock;

    public SingleStockSearchResultDTO(String w) {
        super(w);
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

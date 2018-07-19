package com.tgw360.uip.repository;

import com.tgw360.uip.common.*;
import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.entity.Finance;
import com.tgw360.uip.exception.UipDataException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 财务Repository
 * Created by 邹祥 on 2017/12/5 14:32
 */
@Repository
public class FinanceRepository {
    /**
     * 获取股票最新的几个财务报告
     *
     * @param id  股票ID
     * @param num 财务报告数目
     * @return
     */
    public List<Finance> findLatest(long id, int num) {
        try {
            List<Finance> result = new ArrayList<>();
            RestClient lowClient = ElasticSearchUtils.getLowClient();
            String template = ESQuery.read("FindLatestFinance");
            String queryString = String.format(template, id, num);
            Map<String, String> params = Collections.emptyMap();
            HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);
            Response resp = lowClient.performRequest("GET", "/stock/finance/_search", params, entity);
            SearchResponse response = ElasticSearchUtils.parseResponse(resp);
            ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd"), Date.class);
            for (SearchHit hit : response.getHits().getHits()) {
                Finance finance = new Finance();
                BeanUtils.populate(finance, hit.getSource());
                result.add(finance);
            }
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 获取单只股票的某一个指标（最近10条）
     *
     * @param code  股票代码
     * @param index
     * @return
     */
    public List<Pair<String, Double>> findSingleFinanceIndex(String code, FinanceIndex index) {
        try {
            List<Pair<String, Double>> result = new ArrayList<>();
            ExtendRestHighLevelClient highClient = ElasticSearchUtils.getHighClient();
            SearchRequest request = new SearchRequest(Constants.FINANCE_INDEX);
            request.types(index.name());
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.size(9999);
            ssb.query(QueryBuilders.matchQuery("code", code));
            request.source(ssb);
            SearchResponse response = highClient.search(request);
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> map = hit.getSource();
                for (String key : map.keySet()) {
                    if (key.startsWith("T")) {
                        result.add(new ImmutablePair(key, map.get(key)));
                    }
                }
            }
            Collections.sort(result, new Comparator<Pair<String, Double>>() {
                @Override
                public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
                    return o2.getLeft().compareTo(o1.getLeft());
                }
            });
            return result.subList(0, Math.min(10, result.size()));   // 返回前10条
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

}

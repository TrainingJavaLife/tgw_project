package com.tgw360.uip.repository;

import com.tgw360.uip.common.Constants;
import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.Concept;
import com.tgw360.uip.exception.UipDataException;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 概念DAO
 * Created by 邹祥 on 2017/12/5 9:56
 */
@Repository
public class ConceptRepository {
    /**
     * 根据概念名称查询概念
     *
     * @param name
     * @return
     */
    public Concept findByName(String name) {
        try {
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.CONCEPT_INDEX);
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            BoolQueryBuilder builder = boolQuery().should(termQuery("name", name))
                    .should(termQuery("name", name + "概念"));
            ssb.query(builder);
            request.source(ssb);
            SearchResponse response = client.search(request);
            if (response.getHits().getTotalHits() == 0) {
                return null;
            }
            if (response.getHits().getTotalHits() > 1) {
                throw new UipDataException("根据名称\"" + name + "\"查询概念，结果不唯一");
            }
            Concept concept = new Concept();
            BeanUtils.populate(concept, response.getHits().getAt(0).getSourceAsMap());
            return concept;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    public List<Concept> findAll() {
        try {
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.CONCEPT_INDEX);
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.query(matchAllQuery());
            ssb.size(9999);
            request.source(ssb);
            SearchResponse response = client.search(request);
            List<Concept> list = new ArrayList();
            for (SearchHit hit : response.getHits()) {
                Concept concept = new Concept();
                BeanUtils.populate(concept, hit.getSource());
                list.add(concept);
            }
            return list;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    public boolean isValidConceptName(String name) {
        List<Concept> all = findAll();
        for (Concept concept : all) {
            if (concept.getName().replace("概念", "").equals(name))
                return true;
        }
        return false;
    }
}

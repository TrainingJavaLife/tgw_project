package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.common.ExtendRestHighLevelClient;
import com.tgw360.uip.entity.Concept;
import com.tgw360.uip.repository.ConceptRepository;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 邹祥 on 2017/12/22 14:00
 */
public class CandidateImporter {
    public static void main(String[] args) throws Exception {
        CandidateImporter ci = new CandidateImporter();
//        new CandidateImporter().importStocks();
//        System.out.println(ci.getAllStockName().size());
        ci.importConcepts();
    }

    public void importStocks() throws Exception {
        ExtendRestHighLevelClient client = ElasticSearchUtils.getClient();
        IndexRequest request = new IndexRequest("candidate", "stock");
        // 查所有股票
        String sql4Stock = "select SecuCode,SecuAbbr, ChiSpelling from secumain where SecuCategory=1 and SecuMarket in(83,90) and listedState=1 order by secuCode";
        Connection connection = JdbcUtils.getConnection();
        PreparedStatement pstmt4Stock = connection.prepareStatement(sql4Stock);
        ResultSet rs4Stock = pstmt4Stock.executeQuery();
        int count = 0;
        while (rs4Stock.next()) {
            String code = rs4Stock.getString("SecuCode");
            String text = rs4Stock.getString("SecuAbbr");
            String chiSpelling = rs4Stock.getString("ChiSpelling");
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("text", text);
            map.put("chiSpelling", chiSpelling);
            request.source(map);
            client.index(request);
            System.out.println(++count);
        }
    }

    public void importConcepts() throws Exception {
        List<Concept> concepts = new ConceptRepository().findAll();
        for (Concept concept : concepts) {
            String s = concept.getName().replace("概念", "").toLowerCase();
            index("concept", "概念是" + s);
        }
    }

    public void importSentences() throws Exception {
        List<String> names = getAllStockName();
        for (String name : names) {
            index("sentence", name + "的新闻");
            index("sentence", name + "的公告");
            index("sentence", name + "的研报");
            index("sentence", name + "的行情");
        }
    }

    public void index(String type, String str) throws Exception {
        ExtendRestHighLevelClient client = ElasticSearchUtils.getClient();
        IndexRequest request = new IndexRequest("candidate", type);
        request.source("text", str);
        IndexResponse response = client.index(request);
        System.out.println(response);
    }

    public List<String> getAllStockName() throws IOException {
        ExtendRestHighLevelClient client = ElasticSearchUtils.getClient();
        SearchRequest request = new SearchRequest("candidate");
        SearchSourceBuilder ssb = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        ssb.size(9999);
        request.source(ssb);
        SearchResponse response = client.search(request);
        List<String> result = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            result.add((String) hit.getSource().get("text"));
        }
        return result;
    }
}

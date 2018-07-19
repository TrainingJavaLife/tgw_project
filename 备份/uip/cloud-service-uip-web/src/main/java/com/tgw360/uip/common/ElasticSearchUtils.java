package com.tgw360.uip.common;

import com.tgw360.uip.entity.*;
import com.tgw360.uip.exception.UipDataException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES工具类
 */
public abstract class ElasticSearchUtils {

    private static RestClient lowClient;
    private static ExtendRestHighLevelClient highClient;
    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    static {
        // TODO: 改为自动嗅探集群机器
        lowClient = RestClient.builder(
                new HttpHost("172.18.44.120", 9200, "http")).build();
        highClient = new ExtendRestHighLevelClient(lowClient);
    }

    public static RestClient getLowClient() {
        return lowClient;
    }

    public static ExtendRestHighLevelClient getHighClient() {
        return highClient;
    }

    public static ExtendRestHighLevelClient getClient() {
        return highClient;
    }

    public static SearchResponse parseResponse(Response response) {
        try {
            return highClient.parseEntity(response.getEntity(), SearchResponse::fromXContent);
        } catch (IOException e) {
            throw new UipDataException("ES response解析异常", e);
        }
    }

    /*
        以下用于导入测试数据到ES中
     */

    public static void index(IndexRequest request) throws IOException {
        highClient.index(request);
    }

    public static void index(Stock stock) throws IOException {
        index(toIndexRequest(stock));
    }

    public static void index(Finance finance, Stock parent) throws IOException {
        index(toIndexRequest(finance, parent));
    }

    public static void index(News news) throws IOException {
        index(toIndexRequest(news));
    }

    public static void index(ResearchReport report) throws IOException {
        index(toIndexRequest(report));
    }

    public static void index(Announcement anno) throws IOException {
        index(toIndexRequest(anno));
    }

    public static void index(Concept concept) throws IOException {
        index(toIndexRequest(concept));
    }

    public static void index(Intellisense intellisense) throws IOException {
        index(toIndexRequest(intellisense));
    }

    private static IndexRequest toIndexRequest(Intellisense intellisense){
        IndexRequest request = new IndexRequest("intellisense", "intellisense", intellisense.getId().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("pinyin",intellisense.getPinyin());
        map.put("chiSpelling",intellisense.getChiSpelling());
        map.put("name", intellisense.getName());
        map.put("code", intellisense.getCode());
        request.source(map);
        return request;
    }

    private static IndexRequest toIndexRequest(Concept concept) {
        IndexRequest request = new IndexRequest("concept", "concept", concept.getId().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("name", concept.getName());
        map.put("remark", concept.getRemark());
        request.source(map);
        return request;
    }

    private static IndexRequest toIndexRequest(Announcement anno) {
        IndexRequest request = new IndexRequest("announcement", "announcement",
                anno.getId().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("date", sdf.format(anno.getDate()));
        map.put("title", anno.getTitle());
        map.put("organization", anno.getOrganization());
        map.put("content", anno.getContent());
        map.put("shortContent", anno.getShortContent());
        List<Map<String, Object>> list = new ArrayList<>();
        for (Stock s : anno.getInvolvedStocks()) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("name", s.getName());
            m.put("code", s.getCode());
            list.add(m);
        }
        map.put("involvedStocks", list);
        request.source(map);
        return request;
    }

    private static IndexRequest toIndexRequest(Stock stock) {
        IndexRequest request = new IndexRequest("stock", "stock",
                stock.getId().toString());
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", stock.getCode());
        jsonMap.put("name", stock.getName());
        jsonMap.put("chiSpelling", stock.getChiSpelling());
        jsonMap.put("industry", stock.getIndustry());
        jsonMap.put("city", stock.getCity());
        jsonMap.put("businessMajor", stock.getBusinessMajor());
        String[] s = new String[stock.getConcepts().size()];
        for (int i = 0; i < stock.getConcepts().size(); i++) {
            s[i] = stock.getConcepts().get(i).getName();
        }
        jsonMap.put("concepts", s);
        request.source(jsonMap);
        return request;
    }

    private static IndexRequest toIndexRequest(Finance finance, Stock parent) {
        IndexRequest request = new IndexRequest("stock", "finance",
                finance.getId().toString());
        request.parent(parent.getId().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("endDate", finance.getEndDate());
        map.put("npParentCompanyYOY", finance.getNpParentCompanyYOY());
        map.put("operatingRevenueGrowRate", finance.getOperatingRevenueGrowRate());
        map.put("basicEPS", finance.getBasicEPS());
        map.put("netAssetPS", finance.getNetAssetPS());
        map.put("cashFlowPS", finance.getCashFlowPS());
        map.put("roe", finance.getRoe());
        map.put("grossIncomeRatio", finance.getGrossIncomeRatio());
        request.source(map);
        return request;
    }

    private static IndexRequest toIndexRequest(News news) {
        IndexRequest request = new IndexRequest("news", "news",
                news.getId().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("date", sdf.format(news.getDate()));
        map.put("title", news.getTitle());
        map.put("organization", news.getOrganization());
        map.put("content", news.getContent());
        map.put("shortContent", news.getShortContent());
        List<Map<String, Object>> list = new ArrayList<>();
        for (Stock s : news.getInvolvedStocks()) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("name", s.getName());
            m.put("code", s.getCode());
            list.add(m);
        }
        map.put("involvedStocks", list);
        request.source(map);
        return request;
    }

    private static IndexRequest toIndexRequest(ResearchReport report) {
        IndexRequest request = new IndexRequest("report", "report",
                report.getId().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("date", sdf.format(report.getDate()));
        map.put("title", report.getTitle());
        map.put("organization", report.getOrganization());
        map.put("content", report.getContent());
        map.put("shortContent", report.getShortContent());
        map.put("author",report.getAuthor());
        map.put("rating",report.getRating());
        List<Map<String, Object>> list = new ArrayList<>();
        for (Stock s : report.getInvolvedStocks()) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("name", s.getName());
            m.put("code", s.getCode());
            list.add(m);
        }
        map.put("involvedStocks", list);
        request.source(map);
        return request;
    }
}

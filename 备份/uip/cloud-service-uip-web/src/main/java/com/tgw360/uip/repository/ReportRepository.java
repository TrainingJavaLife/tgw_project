package com.tgw360.uip.repository;

import com.tgw360.uip.common.BeanUtilDateConverter;
import com.tgw360.uip.common.Constants;
import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.exception.UipDataException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 研报Repository
 * Created by 危宇 on 2017/12/11 20:23
 */
@Repository
public class ReportRepository {

    /**
     * 根据研报Id查看研报详情
     * @param Id
     * @return
     * @throws IOException
     */
    public ResearchReport findReportById(long Id){
        try {
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            GetRequest request = new GetRequest(Constants.REPORT_INDEX,Constants.REPORT_TYPE,Long.toString(Id));
            GetResponse response = null;
            response = client.get(request);
            ResearchReport report = new ResearchReport();
            Map<String,Object> map = response.getSource();
            if (map!=null){
                long id = Long.parseLong(response.getId());
                report.setId(id);
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(report, map);
            }
            return report;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 股票Id,Code查询相关新闻、公告、研报
     * @param Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<ResearchReport> getRelevanceReportById(long Id, Integer pageNum, Integer pageSize) {
        try {
            PageBean<ResearchReport> documentPageBean = new PageBean<>(pageNum, pageSize);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();

            request.indices(Constants.REPORT_INDEX);
            request.types(Constants.REPORT_TYPE);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(null,new String[]{
                    "content","involvedStocks"});
            NestedQueryBuilder builder = QueryBuilders.nestedQuery(
                    Constants.NESTED_STOCK_PATH, QueryBuilders.termQuery(Constants.NESTED_SEARCH_ID, Id), ScoreMode.Max);
            searchSourceBuilder.query(builder).from((pageNum - 1) * pageSize).size(pageSize).sort("date", SortOrder.DESC);
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);
            SearchHits hits = response.getHits();
            documentPageBean.setTotal((int) hits.getTotalHits());

            List<ResearchReport> docList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                ResearchReport report = new ResearchReport();

                report.setId(Long.parseLong(docHit.getId()));
                Map<String, Object> docMap = docHit.getSource();
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(report, docMap);
                docList.add(report);
            }
            documentPageBean.setData(docList);
            return documentPageBean;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 股票Id,Code查询相关新闻、公告、研报
     * @param condition
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<ResearchReport> getRelevanceReportByCode(String condition, Integer pageNum, Integer pageSize) {
        try {
            PageBean<ResearchReport> documentPageBean = new PageBean<>(pageNum, pageSize);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();

            request.indices(Constants.REPORT_INDEX);
            request.types(Constants.REPORT_TYPE);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(null,new String[]{
                    "content","involvedStocks"});
            NestedQueryBuilder builder = QueryBuilders.nestedQuery(
                    Constants.NESTED_STOCK_PATH, QueryBuilders.boolQuery()
                            .should(QueryBuilders.termQuery(Constants.NESTED_SEARCH_CODE, condition))
                            .should(QueryBuilders.termQuery(Constants.NESTED_SEARCH_NAME,condition)), ScoreMode.Max);
            searchSourceBuilder.query(builder).from((pageNum - 1) * pageSize).size(pageSize).sort("date", SortOrder.DESC);
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);
            SearchHits hits = response.getHits();
            documentPageBean.setTotal((int) hits.getTotalHits());

            List<ResearchReport> docList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                ResearchReport report = new ResearchReport();
                Long Id = Long.parseLong(docHit.getId());
                report.setId(Id);
                Map<String, Object> docMap = docHit.getSource();
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(report, docMap);
                docList.add(report);
            }
            documentPageBean.setData(docList);
            return documentPageBean;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }
}

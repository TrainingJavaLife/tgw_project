package com.tgw360.uip.repository;

import com.tgw360.uip.common.BeanUtilDateConverter;
import com.tgw360.uip.common.Constants;
import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.entity.Document;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.exception.UipDataException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * 新闻访问Repository
 * Created by 危宇 on 2017/11/23 9:44
 */
@Repository
public class NewsRepository {

    /**
     * 根据新闻Id查看新闻详情
     * @param Id
     * @return
     */
    public News findNewsById(long Id){
        RestHighLevelClient client = ElasticSearchUtils.getClient();
        GetRequest request = new GetRequest(Constants.NEWS_INDEX,Constants.NEWS_TYPE,Long.toString(Id));
        GetResponse response = null;
        try {
            response = client.get(request);
        } catch (IOException e) {
            throw new UipDataException("ES访问异常",e);
        }
        News news = new News();
        Map<String,Object> map = response.getSource();
        if (map!=null){
            long ID = Long.parseLong(response.getId());
            news.setId(ID);
            try {
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(news, map);
            } catch (Exception e) {
                throw new UipDataException("map转object异常", e);
            }
            return news;
        }
        return null;
    }

    /**
     * 页面中输入脚本执行查询新闻
     * @param script
     * @return
     */
    public List<News> searchByScript(String script){
        RestClient lowClient = ElasticSearchUtils.getLowClient();
        Map<String,String> params = Collections.emptyMap();
        HttpEntity entity = new NStringEntity(script, ContentType.APPLICATION_JSON);
        List<News> newsList = new ArrayList<>();
        try {
            Response resp = lowClient.performRequest("POST","/news/news/_search",params,entity);
            SearchResponse response = ElasticSearchUtils.parseResponse(resp);
            SearchHits hits = response.getHits();
            if (hits.getTotalHits()==0){
                return null;
            }else{

                SearchHit[] docHits = hits.getHits();
                for (SearchHit docHit : docHits) {
                    Map<String,Object> docMap = docHit.getSource();
                    String newsTitle = "";
                    String content = "";
                    News news = new News();
                    news.setId(Long.parseLong(docHit.getId()));
                    try {
                        ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                        BeanUtils.populate(news, docMap);
                    } catch (Exception e) {
                        throw new UipDataException("map转object异常", e);
                    }
                    if (docHit.getHighlightFields().get(Constants.NEWS_TITLE)!=null) {
                        Text[] text = docHit.getHighlightFields().get("title").getFragments();
                        for (Text str : text) {
                            newsTitle += str.toString();
                        }
                        news.setTitle(newsTitle);
                    }
                    if (docHit.getHighlightFields().get(Constants.NEWS_CONTENT)!=null) {
                        Text[] text = docHit.getHighlightFields().get("content").getFragments();
                        for (Text str : text) {
                            content +=str.toString();
                        }
                        news.setContent(content);
                    }
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    /**
     * 股票Id查询相关新闻、公告、研报
     *
     * @param Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<News> getRelevanceNewsById(long Id,Integer pageNum, Integer pageSize) {
        try {
            PageBean<News> documentPageBean = new PageBean<>(pageNum, pageSize);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();

            request.indices(Constants.NEWS_INDEX);
            request.types(Constants.NEWS_TYPE);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            NestedQueryBuilder builder = QueryBuilders.nestedQuery(
                    Constants.NESTED_STOCK_PATH, QueryBuilders.termQuery(Constants.NESTED_SEARCH_ID, Id), ScoreMode.Max);

            searchSourceBuilder.query(builder).from((pageNum - 1) * pageSize).size(pageSize).sort("date", SortOrder.DESC);
            searchSourceBuilder.fetchSource(null,new String[]{
                    "content","involvedStocks"}); //ES中_source_include 以及 _source_exclude
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);
            SearchHits hits = response.getHits();
            documentPageBean.setTotal((int) hits.getTotalHits());

            List<News> docList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                News news = new News();
                news.setId(Long.parseLong(docHit.getId()));
                Map<String, Object> docMap = docHit.getSource();
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(news, docMap);
                docList.add(news);
            }
            documentPageBean.setData(docList);
            return documentPageBean;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据股票Code、简称查询相关新闻、公告、研报
     *
     * @param condition
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<News> getRelevanceNewsByCode(String condition,Integer pageNum, Integer pageSize) {
        try {
            PageBean<News> documentPageBean = new PageBean<>(pageNum, pageSize);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();

            request.indices(Constants.NEWS_INDEX);
            request.types(Constants.NEWS_TYPE);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            NestedQueryBuilder builder = QueryBuilders.nestedQuery(
                    Constants.NESTED_STOCK_PATH, QueryBuilders.boolQuery()
                            .should(QueryBuilders.termQuery(Constants.NESTED_SEARCH_CODE, condition))
                            .should(QueryBuilders.termQuery(Constants.NESTED_SEARCH_NAME,condition)), ScoreMode.Max);

            searchSourceBuilder.query(builder).from((pageNum - 1) * pageSize).size(pageSize).sort("date", SortOrder.DESC);
            searchSourceBuilder.fetchSource(null,new String[]{
                    "content","involvedStocks"}); //ES中_source_include 以及 _source_exclude
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);
            SearchHits hits = response.getHits();
            documentPageBean.setTotal((int) hits.getTotalHits());

            List<News> docList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                News news = new News();
                Long ID = Long.parseLong(docHit.getId());
                news.setId(ID);
                Map<String, Object> docMap = docHit.getSource();
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(news, docMap);
                docList.add(news);
            }
            documentPageBean.setData(docList);
            return documentPageBean;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

}

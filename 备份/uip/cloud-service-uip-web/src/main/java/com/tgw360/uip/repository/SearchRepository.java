package com.tgw360.uip.repository;

import com.tgw360.uip.common.*;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.entity.Intellisense;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.exception.UipDataException;
import com.tgw360.uip.query.FullTextQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 搜索+智能提示Repository
 * Created by 危宇 on 2017/12/7 9:46
 */
@Repository
public class SearchRepository {


    /**
     * 新闻全文搜索：关键词查询标题或全文高亮显示
     * @param textQuery
     * @return
     */
    public PageBean<News> searchNewsByKeyword(FullTextQuery textQuery){
        try {
            PageBean<News> result = new PageBean<>(textQuery);
            String mainQueryString = null;
            String timeBegin = null;
            String timeEnd = "now";
            String sortType = null;

            if (textQuery.getTime() == 0){
                textQuery.setTimeBegin("now-20y");
            } else if (textQuery.getTime() == 1){
                timeEnd = textQuery.getTimeEnd();
            }else if (textQuery.getTime() == 2){
                textQuery.setTimeBegin("now-3d");
            } else if (textQuery.getTime() == 3){
                textQuery.setTimeBegin("now-7d");
            } else if (textQuery.getTime() == 4){
                textQuery.setTimeBegin("now-3M");
            }

            if (textQuery.getSort() == 0){
                sortType = "_score";
            } else if (textQuery.getSort() == 1){
                sortType = "date";
            }

            RestClient lowclient = ElasticSearchUtils.getLowClient();
            String template = ESQuery.read(ESQuery.FULLTEXT_CONDITION1);
            String mainTemplate = template.replace("@W",textQuery.getW());
            mainQueryString = String.format(mainTemplate,
                    textQuery.getTimeBegin(),
                    timeEnd,
                    textQuery.getFrom(),
                    textQuery.getPageSize(),
                    sortType);
            Map<String,String> params = Collections.emptyMap();
            HttpEntity entity = new NStringEntity(mainQueryString, ContentType.APPLICATION_JSON);
            Response resp = lowclient.performRequest("GET","/news/news/_search",params,entity);
            SearchResponse response = ElasticSearchUtils.parseResponse(resp);

            SearchHits hits = response.getHits();
            result.setTotal((int)hits.getTotalHits());
            List<News> newsList = new ArrayList<>();

            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                Map<String,Object> docMap = docHit.getSource();
                String title = "";
                String content = "";
                News news = new News();
                news.setId(Long.parseLong(docHit.getId()));
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"),Date.class);
                BeanUtils.populate(news,docMap);
                if (docHit.getHighlightFields().get(Constants.NEWS_TITLE) != null) {
                    Text[] text = docHit.getHighlightFields().get(Constants.NEWS_TITLE).getFragments();
                    for (Text str : text) {
                        title += str.toString();
                    }
                    news.setTitle(title);
                }
                if (docHit.getHighlightFields().get(Constants.NEWS_CONTENT) != null) {
                    Text[] text = docHit.getHighlightFields().get(Constants.NEWS_CONTENT).getFragments();
                    for (Text str : text) {
                        content += str.toString();
                    }
                    news.setContent(content);
                }
                newsList.add(news);
            }
            result.setData(newsList);
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }


    /**
     * 公告全文搜索：关键词查询标题或全文高亮显示
     * @param textQuery
     * @return
     */
    public PageBean<Announcement> searchAnnouncementByKeyword(FullTextQuery textQuery){
        try {
            PageBean<Announcement> result = new PageBean<>(textQuery);
            String mainQueryString = null;
            String timeBegin = null;
            String timeEnd = "now";
            String sortType = null;

            if (textQuery.getTime() == 0){
                timeBegin = "now-20y";
            } else if(textQuery.getTime() == 1) {
                timeBegin = textQuery.getTimeBegin();
                timeEnd = textQuery.getTimeEnd();
            }else if (textQuery.getTime() == 2){
                timeBegin = "now-3d";
            } else if (textQuery.getTime() == 3){
                timeBegin = "now-7d";
            } else if (textQuery.getTime() == 4){
                timeBegin = "now-3M";
            }

            if (textQuery.getSort() == 0){
                sortType = "_score";
            } else if (textQuery.getSort() == 1){
                sortType = "date";
            }


            RestClient lowclient = ElasticSearchUtils.getLowClient();
            String template = ESQuery.read(ESQuery.FULLTEXT_CONDITION1);
            String mainTemplate = template.replace("@W",textQuery.getW());
            mainQueryString = String.format(mainTemplate,
                    timeBegin,
                    timeEnd,
                    textQuery.getFrom(),
                    textQuery.getPageSize(),
                    sortType);
            Map<String,String> params = Collections.emptyMap();
            HttpEntity entity = new NStringEntity(mainQueryString, ContentType.APPLICATION_JSON);
            Response resp = lowclient.performRequest("GET","/announcement/announcement/_search",params,entity);
            SearchResponse response = ElasticSearchUtils.parseResponse(resp);
            SearchHits hits = response.getHits();
            result.setTotal((int)hits.getTotalHits());
            List<Announcement> announcementList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                Map<String,Object> docMap = docHit.getSource();
                String title = "";
                String content = "";
                Announcement announcement = new Announcement();
                announcement.setId(Long.parseLong(docHit.getId()));
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"),Date.class);
                BeanUtils.populate(announcement,docMap);
                if (docHit.getHighlightFields().get(Constants.ANNOUNCEMENT_TITLE) != null) {
                    Text[] text = docHit.getHighlightFields().get(Constants.ANNOUNCEMENT_TITLE).getFragments();
                    for (Text str : text) {
                        title += str.toString();
                    }
                    announcement.setTitle(title);
                }
                if (docHit.getHighlightFields().get(Constants.ANNOUNCEMENT_CONTENT) != null) {
                    Text[] text = docHit.getHighlightFields().get(Constants.ANNOUNCEMENT_CONTENT).getFragments();
                    for (Text str : text) {
                        content += str.toString();
                    }
                    announcement.setContent(content);
                }
                announcementList.add(announcement);
            }
            result.setData(announcementList);
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 研报全文搜索
     * @param textQuery
     * @return
     */
    public PageBean<ResearchReport> searchReportByKeyword(FullTextQuery textQuery){
        try {
            PageBean<ResearchReport> result = new PageBean<>(textQuery);
            String mainQueryString = null;
            String timeBegin = null;
            String timeEnd = "now";
            String sortType = null;
            switch(textQuery.getTime()){
                case 0:
                    timeBegin = "now-20y";
                    break;
                case 1:
                    timeBegin = textQuery.getTimeBegin();
                    timeEnd = textQuery.getTimeEnd();
                    break;
                case 2:
                    timeBegin = "now-3d";
                    break;
                case 3:
                    timeBegin = "now-7d";
                    break;
                case 4:
                    timeBegin = "now-3M";
                    break;
                default:
                    timeBegin =  "now-20y";
                    break;
            }
            if (textQuery.getSort() == 0){
                sortType = "_score";
            } else if (textQuery.getSort() == 1){
                sortType = "date";
            }

            RestClient lowclient = ElasticSearchUtils.getLowClient();
            String template = ESQuery.read(ESQuery.FULLTEXT_CONDITION1);
            String mainTemplate = template.replace("@W",textQuery.getW());
            mainQueryString = String.format(mainTemplate,
                    timeBegin,
                    timeEnd,
                    textQuery.getFrom(),
                    textQuery.getPageSize(),
                    sortType);
            Map<String,String> params = Collections.emptyMap();
            HttpEntity entity = new NStringEntity(mainQueryString, ContentType.APPLICATION_JSON);
            Response resp = lowclient.performRequest("GET","/report/report/_search",params,entity);
            SearchResponse response = ElasticSearchUtils.parseResponse(resp);
            SearchHits hits = response.getHits();
            result.setTotal((int)hits.getTotalHits());
            List<ResearchReport> reportList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                Map<String,Object> docMap = docHit.getSource();
                String title = "";
                String content = "";
                ResearchReport researchReport = new ResearchReport();
                researchReport.setId(Long.parseLong(docHit.getId()));
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"),Date.class);
                BeanUtils.populate(researchReport,docMap);
                if (docHit.getHighlightFields().get("title") != null) {
                    Text[] text = docHit.getHighlightFields().get("title").getFragments();
                    for (Text str : text) {
                        title += str.toString();
                    }
                    researchReport.setTitle(title);
                }
                if (docHit.getHighlightFields().get("content") != null) {
                    Text[] text = docHit.getHighlightFields().get("content").getFragments();
                    for (Text str : text) {
                        content += str.toString();
                    }
                    researchReport.setContent(content);
                }
                reportList.add(researchReport);
            }
            result.setData(reportList);
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 搜索智能提示
     * @param prefix
     * @return
     */
    public List<Intellisense> suggestByPrefix(String prefix){
        try {
            List<Intellisense> list = new ArrayList<>();
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();
            request.indices(Constants.INTELLISENSE_INDEX);
            request.types(Constants.INTELLISENSE_TYPE);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder builder = QueryBuilders.boolQuery().should(QueryBuilders.prefixQuery("code",prefix))
                    .should(QueryBuilders.prefixQuery("name",prefix))
                    .should(QueryBuilders.prefixQuery("chiSpelling",prefix))
                    .should(QueryBuilders.prefixQuery("pinyin",prefix));
            searchSourceBuilder.query(builder).from(0).size(3).sort("code", SortOrder.DESC).sort("pinyin",SortOrder.ASC);
            searchSourceBuilder.fetchSource(new String[]{"name","code","chiSpelling"},null);
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);

            SearchHits hits = response.getHits();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit: docHits){
                Intellisense intellisense = new Intellisense();
                intellisense.setId(Long.parseLong(docHit.getId()));
                Map<String,Object> map = docHit.getSource();
                BeanUtils.populate(intellisense,map);
                list.add(intellisense);
            }
            return list;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

}

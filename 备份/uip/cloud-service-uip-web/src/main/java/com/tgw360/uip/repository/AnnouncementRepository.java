package com.tgw360.uip.repository;

import com.tgw360.uip.common.BeanUtilDateConverter;
import com.tgw360.uip.common.Constants;
import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.entity.Announcement;
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
import java.util.*;

/**
 * 公告Repository
 * Created by 危宇 on 2017/11/23 13:52
 */
@Repository
public class AnnouncementRepository {

    /**
     * 根据公告ID查看公告详情
     * @param Id
     * @return
     */
    public Announcement findAnnouncementById(long Id){
        RestHighLevelClient client = ElasticSearchUtils.getClient();
        GetRequest request = new GetRequest(Constants.ANNOUNCEMENT_INDEX,Constants.ANNOUNCEMENT_TYPE,Long.toString(Id));
        GetResponse response = null;
        try {
            response = client.get(request);
        } catch (IOException e) {
            throw new UipDataException("ES访问异常",e);
        }
        Announcement announcement = new Announcement();
        Map<String,Object> map = response.getSource();
        if (map!=null){
            long ID = Long.parseLong(response.getId());
            announcement.setId(ID);
            try {
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(announcement, map);
            } catch (Exception e) {
                e.printStackTrace();
                throw new UipDataException("map转object异常",e);
            }
            return announcement;
        }
        return null;
    }

    /**
     * 股票Id查询相关新闻、公告、研报
     * @param Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<Announcement> getRelevanceAnnouncementById(long Id, Integer pageNum, Integer pageSize) {
        try {
            PageBean<Announcement> documentPageBean = new PageBean<>(pageNum, pageSize);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();

            request.indices(Constants.ANNOUNCEMENT_INDEX); // 查询索引
            request.types(Constants.ANNOUNCEMENT_TYPE); // 查询类型

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(null,new String[]{
                    "content","involvedStocks"}); // fetchSource(包括,不包括)
            NestedQueryBuilder builder = QueryBuilders.nestedQuery(
                    Constants.NESTED_STOCK_PATH, QueryBuilders.termQuery(Constants.NESTED_SEARCH_ID, Id), ScoreMode.Max);
            searchSourceBuilder.query(builder).from((pageNum - 1) * pageSize).size(pageSize).sort("date", SortOrder.DESC);
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);
            SearchHits hits = response.getHits();
            documentPageBean.setTotal((int) hits.getTotalHits());

            List<Announcement> docList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                Announcement announcement = new Announcement();
                announcement.setId(Long.parseLong(docHit.getId()));
                Map<String, Object> docMap = docHit.getSource();
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(announcement, docMap);
                docList.add(announcement);
            }
            documentPageBean.setData(docList);
            return documentPageBean;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 股票Code、简称查询相关新闻、公告、研报
     * @param condition
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<Announcement> getRelevanceAnnouncementByCode(String condition, Integer pageNum, Integer pageSize) {
        try {
            PageBean<Announcement> documentPageBean = new PageBean<>(pageNum, pageSize);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest();

            request.indices(Constants.ANNOUNCEMENT_INDEX); // 查询索引
            request.types(Constants.ANNOUNCEMENT_TYPE); // 查询类型

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(null,new String[]{
                    "content","involvedStocks"}); // fetchSource(包括,不包括)
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

            List<Announcement> docList = new ArrayList<>();
            SearchHit[] docHits = hits.getHits();
            for (SearchHit docHit : docHits) {
                Announcement announcement = new Announcement();
                Long Id = Long.parseLong(docHit.getId());
                announcement.setId(Id);
                Map<String, Object> docMap = docHit.getSource();
                ConvertUtils.register(new BeanUtilDateConverter("yyyy-MM-dd HH:mm:ss"), Date.class);
                BeanUtils.populate(announcement, docMap);
                docList.add(announcement);
            }
            documentPageBean.setData(docList);
            return documentPageBean;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

}

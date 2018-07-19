package com.tgw360.uip.repository;

import com.tgw360.uip.common.Constants;
import com.tgw360.uip.common.ESQuery;
import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.common.ExtendRestHighLevelClient;
import com.tgw360.uip.condition.ConceptCondition;
import com.tgw360.uip.condition.Condition;
import com.tgw360.uip.condition.IndexCondition;
import com.tgw360.uip.condition.Operator;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.entity.answer.StockPickAnswer;
import com.tgw360.uip.entity.question.ComplexFinanceIndexStockPickQuestion;
import com.tgw360.uip.entity.question.ConceptStockPickQuestion;
import com.tgw360.uip.entity.question.SimpleFinanceIndexStockPickQuestion;
import com.tgw360.uip.exception.UipDataException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by 邹祥 on 2017/12/15 15:33
 */
@Repository
public class StockRepository {
    /**
     * 根据前缀(股票代码，股票名称，拼音简称)查询股票
     *
     * @param prefix
     * @return
     */
    public List<Stock> findByPrefix(String prefix) {
        try {
            List<Stock> list = new ArrayList<>();
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.STOCK_INDEX);
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            BoolQueryBuilder builder = boolQuery().should(prefixQuery("name", prefix))
                    .should(prefixQuery("code", prefix))
                    .should(prefixQuery("chiSpelling", prefix));
            ssb.query(builder);
            ssb.fetchSource(new String[]{"code", "name", "chiSpelling"}, null);
            request.source(ssb);
            SearchResponse response = client.search(request);
            for (SearchHit hit : response.getHits()) {
                Stock stock = new Stock();
                BeanUtils.populate(stock, hit.getSourceAsMap());
                list.add(stock);
            }
            return list;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据股票代码或者名称查询相关股票
     *
     * @param w
     * @return
     */
    public Stock findByCodeOrName(String w) {
        try {
            List<Stock> result = new ArrayList<>();
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.STOCK_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(100);
            ConstantScoreQueryBuilder builder = constantScoreQuery(
                    boolQuery().should(termQuery("name", w)).
                            should(termQuery("code", w)));
            searchSourceBuilder.query(builder);
            request.source(searchSourceBuilder);
            SearchResponse response = null;
            response = client.search(request);
            SearchHits hits = response.getHits();
            if (hits.getTotalHits() == 0) {
                return null;
            }
            if (hits.getTotalHits() > 1) {
                throw new UipDataException("根据条件\"" + w + "\"查询股票，结果不唯一");
            }
            SearchHit hit = hits.getAt(0);
            Stock stock = new Stock();
            stock.setId(Long.parseLong(hit.getId()));
            BeanUtils.populate(stock, hit.getSource());
            return stock;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 获取所有股票的基本信息
     *
     * @return
     */
    public Map<String, Stock> findAllStockBasicInfo() {
        try {
            ExtendRestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.STOCK_INDEX);
            request.types(Constants.STOCK_TYPE);
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.fetchSource(new String[]{"code", "name", "concepts"}, null);
            ssb.query(matchAllQuery());
            ssb.size(9999);
            request.source(ssb);
            SearchResponse response = client.search(request);
            Map<String, Stock> result = new HashMap<>();
            for (SearchHit hit : response.getHits()) {
                Stock stock = new Stock();
                BeanUtils.populate(stock, hit.getSource());
                result.put(stock.getCode(), stock);
            }
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据概念选股问题来选取股票
     *
     * @param q
     * @return
     */
    public StockPickAnswer findByConceptStockPickQuestion(ConceptStockPickQuestion q) {
        try {
            StockPickAnswer answer = new StockPickAnswer(1);
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.STOCK_INDEX);
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            BoolQueryBuilder builder = boolQuery();
            for (String concept : q.getConceptNames()) {
                builder.must(matchQuery("concepts", concept));
            }
            ssb.query(builder);
            ssb.size(10000);
            ssb.fetchSource(new String[]{"code"}, null);
            request.source(ssb);
            SearchResponse response = client.search(request);
            String value = String.join(",", q.getConceptNames());
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String code = (String) source.get("code");
                Object[] row = new Object[]{value};
                answer.addRow(code, row);
            }
            return answer;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据概念条件查询股票
     *
     * @param cc
     * @return Map<股票代码, 概念>
     * @deprecated
     */
    @Deprecated
    public Map<String, String> findByConceptCondition(ConceptCondition cc) {
        try {
            Map<String, String> result = new HashMap<>();
            RestHighLevelClient client = ElasticSearchUtils.getClient();
            SearchRequest request = new SearchRequest(Constants.STOCK_INDEX);
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            BoolQueryBuilder builder = boolQuery().must(matchQuery("concepts", cc.getConcept()));
            ssb.query(builder);
            ssb.size(10000);
            ssb.fetchSource(new String[]{"code"}, null);
            request.source(ssb);
            SearchResponse response = client.search(request);
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String code = (String) source.get("code");
                result.put(code, cc.getConcept());
            }
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据简单的财务指标选股问题来选取股票
     *
     * @param q
     * @return
     */
    public StockPickAnswer findBySimpleFinanceIndexStockPickQuestion(SimpleFinanceIndexStockPickQuestion q) {
        try {
            String fieldName = "T" + q.getDate().replace("-", "");
            ExtendRestHighLevelClient highClient = ElasticSearchUtils.getHighClient();
            SearchRequest request = new SearchRequest("finance");
            request.types(q.getIndex().name());
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.fetchSource(new String[]{"code", fieldName}, null);
            if (q.isRange()) {
                ssb.query(rangeQuery(fieldName).lte(q.getNumber()).gte(q.getNumber2()));
            } else {
                // 反射调用 rangeQuery(fieldName).xxx(number) 方法
                QueryBuilder builder = (QueryBuilder) MethodUtils.invokeExactMethod(
                        rangeQuery(fieldName), q.getOperator().name(),
                        new Object[]{q.getNumber()}, new Class[]{Object.class});
                ssb.query(builder);
            }
            if (q.getOperator() == Operator.gt)
                ssb.query(rangeQuery(fieldName).gt(q.getNumber()));
            else
                ssb.query((rangeQuery(fieldName).lt(q.getNumber())));
            ssb.size(9999);
            request.source(ssb);
            SearchResponse response = highClient.search(request);
            StockPickAnswer answer = new StockPickAnswer(1);
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String code = (String) source.get("code");
                Double value = (Double) source.get(fieldName);
                answer.addRow(code, new Object[]{value});
            }
            return answer;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据复杂的财务指标选股问题来选取股票
     *
     * @param q
     * @return
     */
    public StockPickAnswer findByComplexFinanceIndexStockPickQuestion(ComplexFinanceIndexStockPickQuestion q) {
        try {
            double number = q.getNumber();
            if (q.getOperator() == Operator.lt || q.getOperator() == Operator.lte)    // 小于要变号!!
                number = -1 * number;
            if (q.getPercentage()) {    // 如果带百分号，数值除100
                number = 0.01 * number;
            }
            String leftFieldName = "T" + q.getLeftDate().replace("-", "");
            String rightFieldName = "T" + q.getRightDate().replace("-", "");
            ExtendRestHighLevelClient highClient = ElasticSearchUtils.getHighClient();
            SearchRequest request = new SearchRequest("finance");
            request.types(q.getLeftIndex().name());
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.size(9999);
            ssb.fetchSource(new String[]{"code", leftFieldName, rightFieldName}, null);
            Map<String, Object> params = Collections.EMPTY_MAP;
            String scriptStr;
            if (q.getPercentage()) {
                String template = "(doc['%s'].value-doc['%s'].value)/Math.abs(doc['%s'].value) %s %s";
                scriptStr = String.format(template, leftFieldName, rightFieldName, rightFieldName,
                        q.getOperator().getSymbol(), number);
            } else {
                String template = "doc['%s'].value-doc['%s'].value %s %s";
                scriptStr = String.format(template, leftFieldName, rightFieldName, q.getOperator().getSymbol(), number);
            }
            Script script = new Script(ScriptType.INLINE, "painless", scriptStr, params);
            ssb.query(boolQuery().must(scriptQuery(script)));
            request.source(ssb);
            SearchResponse response = highClient.search(request);
            StockPickAnswer answer = new StockPickAnswer(3);
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String code = (String) source.get("code");
                Double leftValue = (Double) source.get(leftFieldName);
                Double rightValue = (Double) source.get(rightFieldName);
                answer.addRow(code, new Object[]{leftValue, rightValue, null});
            }
            return answer;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    /**
     * 根据简单条件查询股票
     *
     * @param ic
     * @return Map<股票代码, 查询字段的值>
     */
    @Deprecated
    public Map<String, Double> findBySimpleIndexCondition(IndexCondition ic) {
        try {
            double number = ic.getPercentage() ? ic.getNumber() * 0.01 : ic.getNumber();
            String fieldName = "T" + ic.getLeftYear() + ic.getLeftQuarter().getMonth() + ic.getLeftQuarter().getDay();
            ExtendRestHighLevelClient highClient = ElasticSearchUtils.getHighClient();
            SearchRequest request = new SearchRequest("finance");
            request.types(ic.getLeftIndex().name());
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.fetchSource(new String[]{"code", fieldName}, null);
            if (ic.getOperator() == Operator.gt)
                ssb.query(rangeQuery(fieldName).gt(number));
            else
                ssb.query((rangeQuery(fieldName).lt(number)));
            ssb.size(9999);
            request.source(ssb);
            SearchResponse response = highClient.search(request);
            Map<String, Double> result = new HashMap<>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String code = (String) source.get("code");
                Double value = (Double) source.get(fieldName);
                result.put(code, value);
            }
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    @Deprecated
    public Map<String, Tuple<Double, Double>> findByComplexIndexCondition(IndexCondition ic) {
        try {
            double number = ic.getNumber();
            if (ic.getOperator() == Operator.lt)    // 小于要变号!!
                number = -1 * number;
            if (ic.getPercentage()) {    // 如果带百分号，数值除100
                number = 0.01 * number;
            }
            String leftFieldName = "T" + ic.getLeftYear() + ic.getLeftQuarter().getMonth() + ic.getLeftQuarter().getDay();
            String rightFieldname = "T" + ic.getRightYear() + ic.getRightQuarter().getMonth() + ic.getRightQuarter().getDay();
            ExtendRestHighLevelClient highClient = ElasticSearchUtils.getHighClient();
            SearchRequest request = new SearchRequest("finance");
            request.types(ic.getLeftIndex().name());
            SearchSourceBuilder ssb = new SearchSourceBuilder();
            ssb.size(9999);
            ssb.fetchSource(new String[]{"code", leftFieldName, rightFieldname}, null);
            Map<String, Object> params = Collections.EMPTY_MAP;
            String scriptStr;
            if (ic.getPercentage()) {
                String template = "(doc['%s'].value-doc['%s'].value)/doc['%s'].value %s %s";
                scriptStr = String.format(template, leftFieldName, rightFieldname, rightFieldname, ic.getOperator().getSymbol(), number);
            } else {
                String template = "doc['%s'].value-doc['%s'].value %s %s";
                scriptStr = String.format(template, leftFieldName, rightFieldname, ic.getOperator().getSymbol(), number);
            }
            Script script = new Script(ScriptType.INLINE, "painless", scriptStr, params);
            ssb.query(boolQuery().must(scriptQuery(script)));
            request.source(ssb);
            SearchResponse response = highClient.search(request);
            Map<String, Tuple<Double, Double>> result = new HashMap<>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String code = (String) source.get("code");
                Double leftValue = (Double) source.get(leftFieldName);
                Double rightValue = (Double) source.get(rightFieldname);
                result.put(code, new Tuple<>(leftValue, rightValue));
            }
            return result;
        } catch (Exception e) {
            throw new UipDataException(e);
        }
    }

    private String convertToString(Condition condition) {
        if (condition instanceof IndexCondition) {
            IndexCondition c = (IndexCondition) condition;
            if (c.getType() == IndexCondition.Type.SIMPLE) {
                String template = ESQuery.read(ESQuery.SIMPLE_FINANCE_CONDITION);
                return String.format(template, c.getLeftEndDate(), c.getLeftIndex(), c.getOperator(),
                        c.getPercentage() ? c.getNumber() * 0.01 : c.getNumber());
            } else {
                String template = ESQuery.read(ESQuery.COMPLEX_FINANCE_CONDITION);
                double minScore = 0d;
                String date1 = null;
                String finance1 = null;
                String date2 = null;
                String finance2 = null;
                double factor = 1.0;
                if (c.getPercentage()) { // 如果是百分比
                    factor = 1 + c.getNumber() / 100.0;
                } else { // 不是百分比
                    minScore = c.getNumber();
                }
                if (c.getOperator().equals(Operator.gt)) {
                    date1 = c.getLeftEndDate();
                    finance1 = c.getLeftIndex().name();
                    date2 = c.getRightEndDate();
                    finance2 = c.getRightIndex().name();
                } else if (c.getOperator().equals(Operator.lt)) {
                    date1 = c.getRightEndDate();
                    finance1 = c.getRightIndex().name();
                    date2 = c.getLeftEndDate();
                    finance2 = c.getLeftIndex().name();
                }
                return String.format(template, minScore, c.getLeftEndDate(), c.getRightEndDate(),
                        date1, finance1, date2, finance2, factor);
            }
        } else {
            ConceptCondition c = (ConceptCondition) condition;
            String template = ESQuery.read(ESQuery.CONCEPT_CONDITION);
            return String.format(template, c.getConcept(), c.getConcept());
        }
    }

}



package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.common.ExtendRestHighLevelClient;
import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.condition.Quarter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 导入财务数据
 * Created by 邹祥 on 2017/11/22 17:27
 */
public class FinanceImporter {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static void work() throws Exception {
        String sql4Stock = "select * from secumain where SecuCategory=1 and SecuMarket in(83,90) and listedState=1 order by secuCode";
        String sql4Finance = "select * from lc_mainindexnew where companyCode=? and year(endDate) between 1900 and 2014";

        Connection conn = JdbcUtils.getConnection();
        PreparedStatement pstmt4Stock = conn.prepareStatement(sql4Stock);
        PreparedStatement pstmt4Finance = conn.prepareStatement(sql4Finance);
        ResultSet rs4Stock = pstmt4Stock.executeQuery();
        int count = 0;
        while (rs4Stock.next()) {
            long id = rs4Stock.getLong("ID");
            String code = rs4Stock.getString("SecuCode");
            long companyCode = rs4Stock.getLong("CompanyCode");
            pstmt4Finance.setLong(1, companyCode);
            ResultSet rs4Finance = pstmt4Finance.executeQuery();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (FinanceIndex index : FinanceIndex.values()) {
                map.put(index.name(), new HashMap<>());
                map.get(index.name()).put("code", code);
            }
            while (rs4Finance.next()) {
                Date endDate = rs4Finance.getDate("EndDate");
                String endDateStr = sdf.format(endDate);
                for (FinanceIndex index : FinanceIndex.values()) {
                    double value = rs4Finance.getDouble(index.name());
                    Map<String, Object> subMap = map.get(index.name());
                    subMap.put("T" + endDateStr, value);
                }
            }
            for (FinanceIndex index : FinanceIndex.values()) {
                Map<String, Object> subMap = map.get(index.name());
                upsert(index.name(), id, subMap);
            }
            System.out.println(count++);
        }
    }

    public static void main(String[] args) throws Exception {
        work();
//        createIndex();
    }

    /**
     * Update Or Insert
     *
     * @param type
     * @param id
     * @param map
     * @throws IOException
     */
    public static void upsert(String type, Long id, Map<String, Object> map) throws IOException {
        ExtendRestHighLevelClient highClient = ElasticSearchUtils.getHighClient();
        UpdateRequest request = new UpdateRequest("finance", type, id.toString());
        request.doc(map);
        request.upsert(map);
        highClient.update(request);
    }

    /**
     * 创建索引
     *
     * @throws Exception
     */
    public static void createIndex() throws Exception {
        RestClient lowClient = ElasticSearchUtils.getLowClient();
        Map<String, String> params = Collections.emptyMap();
        String mainTemplate = "{\"mappings\": {\"*\":{\"properties\": {\"code\":{\"type\": \"keyword\"}%s}}}}";
        String fieldTemplate = "\"T%s\":{\"type\":\"double\"}";
        StringBuilder sb = new StringBuilder();
        for (FinanceIndex index : FinanceIndex.values()) {
            for (int year = 1990; year <= 2030; year++) {
                for (Quarter quarter : Quarter.values()) {
                    String str = String.format(fieldTemplate, year + quarter.getMonth() + quarter.getDay());
                    sb.append(",").append(str);
                }
            }
        }
        String jsonStr = String.format(mainTemplate, sb);
        System.out.println(jsonStr);
        HttpEntity entity = new NStringEntity(jsonStr, ContentType.APPLICATION_JSON);
        Response response = lowClient.performRequest("PUT", "/finance", params, entity);
        System.out.println(response);
    }
}

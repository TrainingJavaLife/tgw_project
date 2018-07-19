package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.entity.Stock;

import java.io.IOException;
import java.sql.*;

/**
 * 导入新闻数据
 *
 * @author 危宇
 * @date 2017年11月16日 下午3:08:31
 */
public class NewsImporter {
    public static void main(String[] args) throws SQLException, IOException {
//		String sql4News = "SELECT id,InfoPublDate, media, InfoTitle, Content from ni_newsf10 where InfoPublDate BETWEEN '2017-11-1' AND '2017-11-29'";
        String sql4News = "SELECT id,InfoPublDate, media, InfoTitle, Content from ni_newsf10 where InfoPublDate BETWEEN '2017-11-1' AND '2017-11-30'";
        String sql4Stock = "SELECT secu.id, secu.SecuCode, secu.SecuAbbr FROM ni_newsf10_se se, secumain secu WHERE se.ID = ? AND se. CODE = secu.InnerCode  AND se.TypeCode = 1";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement pstmt4News = conn.prepareStatement(sql4News);
        PreparedStatement pstmt4Stock = conn.prepareStatement(sql4Stock);
        // 查新闻
        ResultSet rs4News = pstmt4News.executeQuery();
        while (rs4News.next()) {
            News n = new News();
            long id = rs4News.getLong("id");
            Date date = rs4News.getDate("InfoPublDate");
            String organization = rs4News.getString("media");
            String title = rs4News.getString("InfoTitle");
            String content = rs4News.getString("Content");
            n.setId(id);
            n.setDate(date);
            n.setOrganization(organization);
            n.setTitle(title);
            n.setContent(content);
            n.setShortContent(content.length() >= 70 ? content.substring(0, 70) : content);
            // 查相关股票
            pstmt4Stock.setLong(1, id);
            ResultSet rs4Stock = pstmt4Stock.executeQuery();
            while (rs4Stock.next()) {
                long stockID = rs4Stock.getLong("id");
                String stockCode = rs4Stock.getString("SecuCode");
                String stockName = rs4Stock.getString("SecuAbbr");
                Stock stock = new Stock();
                stock.setId(stockID);
                stock.setCode(stockCode);
                stock.setName(stockName);
                n.getInvolvedStocks().add(stock);
            }
            ElasticSearchUtils.index(n);
        }
    }
}

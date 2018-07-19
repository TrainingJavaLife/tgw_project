package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.Announcement;
import com.tgw360.uip.entity.Stock;

import java.io.IOException;
import java.sql.*;


/**
 * 导入公告数据
 *
 * @author 邹祥
 * @date 2017年11月16日 下午5:05:39
 */
public class AnnouncementImporter {
    public static void main(String[] args) throws SQLException, IOException {
        int successTotal = 0;
        int failureTotal = 0;
        String sql4ID = "select id, companyCode from lc_announcement order by id desc limit 0,20000";
        String sql4Anno = "select InfoPublDate, InfoTitle, media, content  from lc_announcement where id = ?";
        String sql4Stock = "select * from secumain where companyCode = ? and SecuCategory=1 and SecuMarket in(83,90) and listedState=1";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement pstmt4ID = conn.prepareStatement(sql4ID);
        PreparedStatement pstmt4Anno = conn.prepareStatement(sql4Anno);
        PreparedStatement pstmt4Stock = conn.prepareStatement(sql4Stock);
        ResultSet rs4ID = pstmt4ID.executeQuery();
        while (rs4ID.next()) {
            Announcement anno = new Announcement();
            long id = rs4ID.getLong("id");
            long companyCode = rs4ID.getLong("companyCode");
            anno.setId(id);
            // 查询公告详情
            pstmt4Anno.setLong(1, id);
            ResultSet rs4Anno = pstmt4Anno.executeQuery();
            if (rs4Anno.next()) {
                Date date = rs4Anno.getDate("infoPublDate");
                String title = rs4Anno.getString("InfoTitle");
                String organization = rs4Anno.getString("media");
                String content = rs4Anno.getString("content");
                anno.setDate(date);
                anno.setTitle(title);
                anno.setOrganization(organization);
                anno.setContent(content);
                anno.setShortContent(content.length() >= 70 ? content.substring(0, 70) : content);
            }
            // 查询相关股票
            pstmt4Stock.setLong(1, companyCode);
            ResultSet rs4Stock = pstmt4Stock.executeQuery();
            if (rs4Stock.next()) {
                Stock s = new Stock();
                s.setId(rs4Stock.getLong("id"));
                s.setCode(rs4Stock.getString("SecuCode"));
                s.setName(rs4Stock.getString("SecuAbbr"));
                anno.getInvolvedStocks().add(s);
            }
            if (anno.getInvolvedStocks().size() > 0) {
                successTotal++;
            } else {
                failureTotal++;
            }
            ElasticSearchUtils.index(anno);
            System.out.printf("成功:%s \t 失败：%s \t 总共：%s\n", successTotal,
                    failureTotal, successTotal + failureTotal);
        }
    }
}

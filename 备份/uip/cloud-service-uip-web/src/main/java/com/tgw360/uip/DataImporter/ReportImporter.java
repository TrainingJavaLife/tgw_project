package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.ResearchReport;
import com.tgw360.uip.entity.Stock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by 危宇 on 2017/12/19 17:24
 */
public class ReportImporter {
    public static void main(String[] args) throws SQLException, IOException {
        String sql4Report = "SELECT b.id,InfoPublDate, OrgNameDisc,Author,cts.MS,Title, Conclusion FROM c_rr_researchreport b,ct_systemconst cts WHERE  cts.LB=1585 AND cts.DM = b.researchDepth AND InfoPublDate BETWEEN '2017-11-1' AND '2017-11-30'";
        String sql4Stock = "SELECT secu.id, secu.SecuCode, secu.SecuAbbr FROM c_rr_researchreport b,c_rr_researchreport_se c, secumain secu WHERE c.ID = ? AND secu.innercode=c.code AND c.ID = b.ID AND c.TypeCode = 2";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement pstmt4Report = conn.prepareStatement(sql4Report);
        PreparedStatement pstmt4Stock = conn.prepareStatement(sql4Stock);
        ResultSet rst4Report = pstmt4Report.executeQuery();
        while(rst4Report.next()){
            ResearchReport report = new ResearchReport();
            long id = rst4Report.getLong("id");
            Date date = rst4Report.getDate("InfoPublDate");
            String organization = rst4Report.getString("OrgNameDisc");
            String author = rst4Report.getString("Author");
            String reportType = rst4Report.getString("MS");
            String title = rst4Report.getString("title");
            String content = rst4Report.getString("Conclusion");
            report.setId(id);
            report.setDate(date);
            report.setOrganization(organization);
            report.setRating(reportType);
            report.setTitle(title);
            report.setAuthor(author);
            report.setContent(content);
            report.setShortContent(content.length() >= 70 ? content.substring(0, 70) : content);

            pstmt4Stock.setLong(1,id);
            ResultSet rst4Stock = pstmt4Stock.executeQuery();
            while(rst4Stock.next()){
                String stockCode = rst4Stock.getString("SecuCode");
                String stockName = rst4Stock.getString("SecuAbbr");
                Stock stock = new Stock();
                stock.setId(rst4Stock.getLong("id"));
                stock.setCode(stockCode);
                stock.setName(stockName);
                report.getInvolvedStocks().add(stock);
            }
            ElasticSearchUtils.index(report);
        }
    }
}

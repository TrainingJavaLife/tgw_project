package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.Concept;
import com.tgw360.uip.entity.Finance;
import com.tgw360.uip.entity.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 * 导入股票数据
 *
 * @author 邹祥
 * @date 2017年11月16日 下午1:25:58
 */
public class StockImporter {
    public static void main(String[] args) throws Exception {
        // 查所有股票
        String sql4Stock = "select * from secumain where SecuCategory=1 and SecuMarket in(83,90) and listedState=1 order by secuCode";
        // 查行业
        String sql4Industry = "SELECT * FROM lc_exgindustry WHERE companyCode=? and standard=24";
        // 查城市
        String sql4City = "SELECT const.ms FROM ct_systemconst const, lc_stockarchives company WHERE company.CompanyCode=? AND company.CityCode = const.DM AND const.LB = 1145";
        // 查主营业务
        String sql4Major = "SELECT BusinessMajor FROM LC_StockArchives WHERE CompanyCode=?";
        // 查概念
        String sql4Concept = "SELECT coco.ID, conceptName FROM lc_coconcept coco, lc_conceptlist concept where coco.innerCode=? AND coco.conceptCode = concept.conceptCode";
        // 查财务报告
        String sql4Finance = "SELECT ID,enddate,NPParentCompanyYOY,OperatingRevenueGrowRate,BasicEPS,netAssetPS,CashFlowPS,ROE,grossIncomeRatio FROM lc_mainindexnew WHERE YEAR (EndDate) >= 2015 and CompanyCode=?";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement pstmt4Stock = conn.prepareStatement(sql4Stock);
        PreparedStatement pstmt4Industry = conn.prepareStatement(sql4Industry);
        PreparedStatement pstmt4City = conn.prepareStatement(sql4City);
        PreparedStatement pstmt4Major = conn.prepareStatement(sql4Major);
        PreparedStatement pstmt4Concept = conn.prepareStatement(sql4Concept);
        PreparedStatement pstmt4Finance = conn.prepareStatement(sql4Finance);
        ResultSet rs4Stock = pstmt4Stock.executeQuery();
        int count = 0;
        while (rs4Stock.next()) {
            Stock stock = new Stock();
            Long id = rs4Stock.getLong("id");
            String code = rs4Stock.getString("SecuCode");
            String name = rs4Stock.getString("secuAbbr");
            name = ToDBC(name).replace(" ", "");    // 全角转半角，去除空格。 (万  科A)
            Long companyCode = rs4Stock.getLong("companyCode");
            Long innerCode = rs4Stock.getLong("innerCode");
            String chiSpelling = rs4Stock.getString("chiSpelling");
            stock.setId(id);
            stock.setCode(code);
            stock.setName(name);
            stock.setChiSpelling(chiSpelling);
            // 查行业
            pstmt4Industry.setLong(1, companyCode);
            ResultSet rs4Industry = pstmt4Industry.executeQuery();
            if (rs4Industry.next()) {
                String industry = rs4Industry.getString("FirstIndustryName");
                stock.setIndustry(industry);
            }
            // 查城市
            pstmt4City.setLong(1, companyCode);
            ResultSet rs4City = pstmt4City.executeQuery();
            if (rs4City.next()) {
                String city = rs4City.getString("ms");
                stock.setCity(city);
            }
            // 主营业务
            pstmt4Major.setLong(1, companyCode);
            ResultSet rs4Major = pstmt4Major.executeQuery();
            if (rs4Major.next()) {
                String businessMajor = rs4Major.getString("BusinessMajor");
                stock.setBusinessMajor(businessMajor);
            }
            // 查概念
            pstmt4Concept.setLong(1, innerCode);
            ResultSet rs4Concept = pstmt4Concept.executeQuery();
            while (rs4Concept.next()) {
                Concept c = new Concept();
                long cid = rs4Concept.getLong("ID");
                String cname = rs4Concept.getString("conceptName");
                cname = cname.replace("概念", "");      // 去除概念后缀
                c.setId(cid);
                c.setName(cname);
                stock.getConcepts().add(c);
            }
            // 查财务报告
            pstmt4Finance.setLong(1, companyCode);
            ResultSet rs4Finance = pstmt4Finance.executeQuery();
            while (rs4Finance.next()) {
                Finance finance = new Finance();
                long financeID = rs4Finance.getLong("ID");
                Date enddate = rs4Finance.getDate("enddate");
                double npParentCompanyYOY = rs4Finance.getDouble("NPParentCompanyYOY");
                double operatingRevenueGrowRate = rs4Finance.getDouble("OperatingRevenueGrowRate");
                double basicEPS = rs4Finance.getDouble("BasicEPS");
                double netAssetPS = rs4Finance.getDouble("netAssetPS");
                double cashFlowPS = rs4Finance.getDouble("CashFlowPS");
                double roe = rs4Finance.getDouble("roe");
                double grossIncomeRatio = rs4Finance.getDouble("grossIncomeRatio");
                finance.setId(financeID);
                finance.setEndDate(enddate);
                finance.setNpParentCompanyYOY(npParentCompanyYOY);
                finance.setOperatingRevenueGrowRate(operatingRevenueGrowRate);
                finance.setBasicEPS(basicEPS);
                finance.setNetAssetPS(netAssetPS);
                finance.setCashFlowPS(cashFlowPS);
                finance.setRoe(roe);
                finance.setGrossIncomeRatio(grossIncomeRatio);
                stock.getFinances().add(finance);
            }
            ElasticSearchUtils.index(stock);
            for (Finance finance : stock.getFinances()) {
                ElasticSearchUtils.index(finance, stock);
            }
            System.out.println(++count);
        }
    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);
        return returnString;
    }
}

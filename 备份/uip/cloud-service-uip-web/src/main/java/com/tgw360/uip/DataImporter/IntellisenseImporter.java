package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.Intellisense;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 危宇 on 2017/12/29 15:20
 */
public class IntellisenseImporter {

    public static String sql4Stock = "SELECT ID, to_pinyin(SecuAbbr) AS pinyin,SecuAbbr,Secucode,chiSpelling FROM secumain" +
            " WHERE SecuCategory=1 AND SecuMarket IN(83,90) AND listedState=1 ORDER BY secuCode";
    public static String sql4Concept = "SELECT ID,  ConceptName FROM lc_conceptlist WHERE ConceptState = 1";
    public static String sql4Pinyin = "SELECT to_pinyin(?) as pinyin";

    public static void main(String[] args) throws SQLException, IOException {
       Connection conn = JdbcUtils.getConnection();
       IntellisenseImporter.importStock(conn);
       IntellisenseImporter.importConcept(conn);

    }

    public static void importStock(Connection conn) throws SQLException, IOException {
        PreparedStatement pst4Stock = conn.prepareStatement(sql4Stock);
        ResultSet rst4Stock = pst4Stock.executeQuery();
        while(rst4Stock.next()){
            Intellisense intellisense = new Intellisense();
            Long id = rst4Stock.getLong("ID");
            String pinyin = rst4Stock.getString("pinyin");
            String name = rst4Stock.getString("SecuAbbr");
            name = ToDBC(name).replace(" ","");
            String code = rst4Stock.getString("Secucode");
            String chiSpelling = rst4Stock.getString("chiSpelling");
            intellisense.setChiSpelling(chiSpelling);
            intellisense.setCode(code);
            intellisense.setId(id);
            intellisense.setName(name);
            intellisense.setPinyin(pinyin);
            ElasticSearchUtils.index(intellisense);
        }
    }

    public static void importConcept(Connection conn) throws SQLException, IOException {
        PreparedStatement pst4Concept = conn.prepareStatement(sql4Concept);
        PreparedStatement pst4Pinyin = conn.prepareStatement(sql4Pinyin);
        ResultSet rst4Concept = pst4Concept.executeQuery();
        while (rst4Concept.next()){
            Intellisense intellisense = new Intellisense();
            Long Id = rst4Concept.getLong("ID");
            String name = rst4Concept.getString("ConceptName");
            name = name.replace("概念","");
            pst4Pinyin.setString(1,name);
            ResultSet rst4Pinyin = pst4Pinyin.executeQuery();
            intellisense.setId(Id);
            intellisense.setName(name);
            while (rst4Pinyin.next()){
                String pinyin = rst4Pinyin.getString("pinyin");
                intellisense.setPinyin(pinyin);
            }
            ElasticSearchUtils.index(intellisense);
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

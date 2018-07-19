package com.tgw360.uip.DataImporter;

import com.tgw360.uip.common.ElasticSearchUtils;
import com.tgw360.uip.entity.Concept;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 导入概念数据
 * Created by 邹祥 on 2017/11/22 15:18
 */
public class ConceptImporter {
    public static void main(String[] args) throws SQLException, IOException {
        String sql = "SELECT ID,  ConceptName, Remark FROM lc_conceptlist where ConceptState = 1";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery(sql);
        while (rs.next()) {
            long id = rs.getLong("id");
            String conceptName = rs.getString("ConceptName");
            // 去除掉尾部的概念二字
            // 例如：机器人概念-->机器人
            conceptName = conceptName.replace("概念", "");
            String remark = rs.getString("Remark");
            Concept concept = new Concept();
            concept.setId(id);
            concept.setName(conceptName);
            concept.setRemark(remark);
            System.out.println(concept.toString(ToStringStyle.MULTI_LINE_STYLE));
            ElasticSearchUtils.index(concept);
        }
    }
}

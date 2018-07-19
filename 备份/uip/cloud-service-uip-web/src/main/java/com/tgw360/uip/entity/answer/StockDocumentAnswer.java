package com.tgw360.uip.entity.answer;

import com.tgw360.uip.entity.Document;

import java.util.List;

/**
 * Answer——股票相关的文档
 * Created by 邹祥 on 2017/12/27 13:26
 */
public class StockDocumentAnswer extends AnswerAdaptor {
    private List<Document> documents;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Document document : documents) {
            sb.append(document.getTitle()).append("\n");
        }
        return sb.toString();
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}

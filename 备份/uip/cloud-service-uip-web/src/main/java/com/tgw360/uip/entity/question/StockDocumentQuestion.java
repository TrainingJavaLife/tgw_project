package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.PageBean;
import com.tgw360.uip.common.SpringUtils;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.entity.Document;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.entity.answer.StockDocumentAnswer;
import com.tgw360.uip.repository.AnnouncementRepository;
import com.tgw360.uip.repository.NewsRepository;
import com.tgw360.uip.repository.ReportRepository;
import com.tgw360.uip.repository.StockRepository;

import java.util.List;

/**
 * Question——股票的相关文档（新闻/公告/研报)
 * Created by 邹祥 on 2017/12/25 17:46
 */
public class StockDocumentQuestion extends QuestionAdaptor {

    private String w;   // 股票名称或代码
    private Document.Type type; // 文档类型

    private static final StockRepository stockRepository = SpringUtils.getBean(StockRepository.class);
    private static final NewsRepository newsRepository = SpringUtils.getBean(NewsRepository.class);
    private static final AnnouncementRepository announcementRepository = SpringUtils.getBean(AnnouncementRepository.class);
    private static final ReportRepository reportRepository = SpringUtils.getBean(ReportRepository.class);

    @Override
    public Answer perform() {
        // TODO: 修改为一次查询
        Long id = stockRepository.findByCodeOrName(w).getId();
        PageBean page = null;
        switch (type) {
            case NEWS:
            case NEWS2:
                page = newsRepository.getRelevanceNewsById(id, 1, 5);
                break;
            case ANNOUNCEMENT:
                page = announcementRepository.getRelevanceAnnouncementById(id, 1, 5);
                break;
            case RESEARCH_REPORT:
                page = reportRepository.getRelevanceReportById(id, 1, 5);
                break;
        }
        StockDocumentAnswer answer = new StockDocumentAnswer();
        answer.setDocuments(page.getData());
        return answer;
    }

    @Override
    public void doParse(List<Term> terms) {
        for (Term term : terms) {
            if (MyNature.STOCK.eqaulTo(term.nature)) {
                this.w = term.word;
            }
            if (MyNature.KEYWORD_DOCUMENT.eqaulTo(term.nature)) {
                this.type = Document.Type.parse(term.word);
            }
        }
    }

    @Override
    public boolean check() {
        if (w == null)
            return false;
        if (type == null)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("[%s的%s]", w, type.chineseName());
    }

    public String getW() {
        return w;
    }

    public Document.Type getType() {
        return type;
    }
}

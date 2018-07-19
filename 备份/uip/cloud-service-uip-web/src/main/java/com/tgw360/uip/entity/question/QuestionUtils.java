package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.algorithm.similarity.CosineDistanceCalculator;
import com.tgw360.uip.algorithm.similarity.NatureVector;
import com.tgw360.uip.algorithm.similarity.NatureVectorDistanceCalculator;
import com.tgw360.uip.common.Constants;
import com.tgw360.uip.nlp.HanLPHelper;
import com.tgw360.uip.nlp.MyTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Question类型与其对应的模式的映射
 * Created by 邹祥 on 2017/12/25 17:18
 */
public class QuestionUtils {
    private static final Logger log = LoggerFactory.getLogger(QuestionUtils.class);
    private static Map<Class, String[]> templateMapping = new HashMap<>();
    private static Map<Class, List<NatureVector>> vectorMapping = new HashMap<>();

    static {
        HanLPHelper.init();
        log.info("初始化模板的词性向量");
        templateMapping.put(StockDetailQuestion.class, new String[]{"平安银行"});
        templateMapping.put(StockQuotationQuestion.class, new String[]{"平安银行的行情"});
        templateMapping.put(StockDocumentQuestion.class, new String[]{"平安银行的新闻"});
        templateMapping.put(StockFinanceIndexQuestion.class, new String[]{"平安银行的EPS"});
        templateMapping.put(ConceptStockPickQuestion.class, new String[]{"智能家居", "概念是机器人"});
        templateMapping.put(FinanceIndexStockPickQuestion.class, new String[]{"今年基本每股收益大于1"});

        for (Class clazz : templateMapping.keySet()) {
            String[] templates = templateMapping.get(clazz);
            List<NatureVector> vectors = new ArrayList<>();
            for (String template : templates) {
                NatureVector vector = new NatureVector(template);
                vectors.add(vector);
            }
            vectorMapping.put(clazz, vectors);
        }
    }

    static NatureVectorDistanceCalculator calculator = new CosineDistanceCalculator();

    /**
     * 查询与字符串最相似的Question类型
     *
     * @param terms
     * @return
     */
    public static Class<? extends Question> findMostSimilarQuestionClass(List<Term> terms) {
        NatureVector v1 = new NatureVector(terms);
        double nowScore = 0d;
        Class<? extends Question> nowClazz = null;
        for (Class clazz : vectorMapping.keySet()) {
            List<NatureVector> vectors = vectorMapping.get(clazz);
            for (NatureVector v2 : vectors) {
                double score = Math.abs(calculator.calculate(v1, v2));
                if (score - nowScore > Constants.INFINITESIMAL) {
                    nowScore = score;
                    nowClazz = clazz;
                }
            }
        }
        return nowClazz;
    }

    public static Question parseToSimilarQuestion(List<Term> terms) {
        Class<? extends Question> clazz = findMostSimilarQuestionClass(terms);
//        System.out.println(clazz);
        if (clazz != null) {
            try {
                Question question = clazz.newInstance().parseFrom(terms);
                return question;
            } catch (Exception e) {
                e.printStackTrace();
                return new WrongQuestion();
            }
        }
        return new WrongQuestion();
    }

    /**
     * 将 字符串 转换成 最相似 的Question 形式
     *
     * @param text
     * @return
     */
    public static Question parseToSimilarQuestion(String text) {
        return parseToSimilarQuestion(MyTokenizer.segment(text));
    }
}

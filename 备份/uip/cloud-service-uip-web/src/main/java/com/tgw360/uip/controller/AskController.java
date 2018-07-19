package com.tgw360.uip.controller;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.entity.answer.Answer;
import com.tgw360.uip.entity.answer.StockPickAnswer;
import com.tgw360.uip.entity.question.*;
import com.tgw360.uip.nlp.MyTokenizer;
import com.tgw360.uip.repository.ConceptRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 邹祥 on 2017/12/25 16:53
 */
@RestController
public class AskController extends BaseController {

    /**
     * 条件分隔符，注意正则表达式中的特殊字符需要转义
     */
    private static final String[] SEPERATOR = new String[]{";", "；",  "。", "\\?", "？"};
    private static final String SPLIT_REGEX = String.join("|", SEPERATOR);

    @GetMapping("/ask")
    public Answer ask(String w) {
        assertNotBlank(w, "w不能为空");
        Question question = convertToQuestion(w);
        System.out.println(question);
        Answer answer = question.perform();
        // TODO: 为选股Answer 注入 选股Question
//        if (answer instanceof StockPickAnswer){
//            StockPickAnswer a = (StockPickAnswer) answer;
//            StockPickCompositeQuestion q = (StockPickCompositeQuestion) question;
//        }
        return answer;
    }

    private Question convertToQuestion(String w) {
        List<String> sentences = Arrays.stream(w.split(SPLIT_REGEX)).collect(Collectors.toList());
        List<Question> questions = parseAll(sentences);
        Question firstValidQuestion = null; // 第一个合法的条件
        Question firstWrongQuestion = null; // 第一个错误的条件
        boolean hasStockPickQuestion = false;   // 是否包含选股条件
        for (Question question : questions) {
            if (firstWrongQuestion == null && (question instanceof WrongQuestion)) {
                firstWrongQuestion = question;
            }
            if (firstValidQuestion == null && !(question instanceof WrongQuestion)) {
                firstValidQuestion = question;
            }
            if (question instanceof StockPickQuestion) {
                hasStockPickQuestion = true;
                break;
            }
        }
        Question result = null;
        if (hasStockPickQuestion) { // 如果包含选股条件，则按组合选股条件处理
            StockPickCompositeQuestion compositeQuestion = new StockPickCompositeQuestion();
            compositeQuestion.add(new StockNameStockPickQuestion());    // 加一列股票名称
            for (Question question : questions) {
                if (question instanceof StockPickQuestion) {
                    compositeQuestion.add((StockPickQuestion) question);
                }
                if (question instanceof StockDetailQuestion) {
                    String name = ((StockDetailQuestion) question).getW();
                    if (new ConceptRepository().isValidConceptName(name)) {
                        ConceptStockPickQuestion conceptStockPickQuestion = new ConceptStockPickQuestion();
                        conceptStockPickQuestion.addConceptName(name);
                        compositeQuestion.add(conceptStockPickQuestion);
                    }
                }
            }
            result = compositeQuestion;
        } else {    // 否则，按第一个合法的条件处理
            result = firstValidQuestion;
        }
        if (result == null) {
            result = firstWrongQuestion;
        }
        return result;
    }

    private List<Question> parseAll(List<String> sentences) {
        List<Question> result = new ArrayList<>();
        for (String s : sentences) {
            List<Term> terms = MyTokenizer.segment(s);
            Question question = QuestionUtils.parseToSimilarQuestion(terms);
            result.add(question);
        }
        return result;
    }

}

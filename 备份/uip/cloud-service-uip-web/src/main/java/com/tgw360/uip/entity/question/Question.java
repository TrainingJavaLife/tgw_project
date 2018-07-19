package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.entity.answer.Answer;

import java.util.List;

/**
 * 用户输入的问题
 * Created by 邹祥 on 2017/12/25 13:49
 */
public interface Question {

    /**
     * 将分词之后的内容转换成Question对象，返回的Question为this指针
     *
     * @param terms
     * @return
     */
    Question parseFrom(List<Term> terms);

    /**
     * 从ES、数据库中查询，或调用其他接口，获取当前问题的答案
     *
     * @return
     */
    Answer perform();

}

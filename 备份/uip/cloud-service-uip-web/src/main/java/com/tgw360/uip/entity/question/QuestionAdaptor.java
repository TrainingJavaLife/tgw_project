package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.BaseObject;

import java.util.List;

/**
 * Created by 邹祥 on 2017/12/25 13:55
 */
public abstract class QuestionAdaptor extends BaseObject implements Question {
    @Override
    public Question parseFrom(List<Term> terms) {
        doParse(terms);
        if (this.check()) {
            return this;
        } else {
            return new WrongQuestion();
        }
    }

    /**
     * 将分词之后的内容转换成Question对象
     *
     * @param terms
     * @return
     */
    public abstract void doParse(List<Term> terms);

    /**
     * 校验当前Question对象是否合法（是否缺失必须字段？）
     *
     * @return
     */
    public abstract boolean check();

}

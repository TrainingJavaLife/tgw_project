package com.tgw360.uip.entity.question;

import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.entity.answer.Answer;

import java.util.List;

/**
 * 无法正常解析的Question类型
 * Created by 邹祥 on 2017/12/25 17:37
 */
public class WrongQuestion implements Question {
    @Override
    public Question parseFrom(List<Term> terms) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public Answer perform() {
        return null;
    }

    @Override
    public String toString() {
        return "[ERROR]";
    }
}

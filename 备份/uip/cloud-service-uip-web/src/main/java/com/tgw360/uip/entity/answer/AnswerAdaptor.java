package com.tgw360.uip.entity.answer;

import com.tgw360.uip.common.BaseObject;
import com.tgw360.uip.entity.answer.askmore.AskMoreStratage;

import java.util.List;

/**
 * Created by 邹祥 on 2017/12/25 13:58
 */
public abstract class AnswerAdaptor extends BaseObject implements Answer {

    private static String type; // 类型。用来让前端区分各种不同类型的问题。
    private static AskMoreStratage askMoreStratage;

    public String getType() {
        if (type == null) {
            // 获取当前子类名称
            type = this.getClass().getSimpleName();
        }
        return type;
    }

    public List<String> getAskMore() {
        return askMoreStratage.get(3);
    }
}

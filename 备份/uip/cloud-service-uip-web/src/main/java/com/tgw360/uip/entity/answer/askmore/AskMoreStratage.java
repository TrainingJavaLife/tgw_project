package com.tgw360.uip.entity.answer.askmore;

import java.util.List;

/**
 * "你是不是还想问?" 生成策略
 * Created by 邹祥 on 2018/1/2 14:46
 */
public interface AskMoreStratage {

    /**
     * 获取n条“你是不是还想问”的问题
     * @param n
     * @return
     */
    List<String> get(int n);
}

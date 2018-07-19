package com.tgw360.uip.condition;

/**
 * 错误的条件
 * Created by 邹祥 on 2017/12/14 16:01
 */
public class WrongCondition implements Condition {

    private String message;

    public WrongCondition(String message) {
        this.message = message;
    }

}

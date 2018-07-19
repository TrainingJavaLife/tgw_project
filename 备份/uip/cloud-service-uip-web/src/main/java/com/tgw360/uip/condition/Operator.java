package com.tgw360.uip.condition;

import java.util.HashMap;
import java.util.Map;

/**
 * 比较符
 * Created by 邹祥 on 2017/11/27 9:55
 */
public enum Operator {

    /*
        注意：第一个要放符号。
     */
    gt(new String[]{">", "大于", "高于", "多于", "大", "高", "多"}),
    lt(new String[]{"<", "小于", "低于", "少于", "小", "低", "少"}),
    eq(new String[]{"=", "等于"}),
    gte(new String[]{">=", "大于等于", "以上"}),
    lte(new String[]{"<=", "小于等于", "以下"});

    static Map<String, Operator> mapping;

    static { // 用于初始化mapping
        mapping = new HashMap<>();
        for (Operator op : Operator.values()) {
            for (String s : op.aliases) {
                mapping.put(s, op);
            }
        }
    }

    /**
     * 名称列表
     */
    private String[] aliases;

    Operator(String[] aliases) {
        this.aliases = aliases;
    }

    public static Operator parse(String name) {
        return mapping.get(name);
    }

    public String getSymbol() {
        return this.aliases[0];
    }

    public String[] getAliases() {
        return aliases;
    }
}

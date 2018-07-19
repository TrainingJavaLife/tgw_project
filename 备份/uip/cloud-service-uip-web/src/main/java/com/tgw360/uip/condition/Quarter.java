package com.tgw360.uip.condition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 季度
 * Created by 邹祥 on 2017/11/27 9:18
 */
public enum Quarter {
    FIRST("03", "31", new String[]{"1季度", "一季度", "1季报", "一季报"}),
    SECOND("06", "30", new String[]{"2季度", "二季度", "2季报", "二季报", "中报", "中年报", "半年报"}),
    THIRD("09", "30", new String[]{"3季度", "三季度", "3季报", "三季报"}),
    FOURTH("12", "31", new String[]{"4季度", "四季度", "年报"});

    static Map<String, Quarter> mapping;

    static { // 用于初始化mapping
        HashMap<String, Quarter> hashMap = new HashMap<>();
        for (Quarter fi : Quarter.values()) {
            for (String s : fi.aliases) {
                hashMap.put(s, fi);
            }
        }
        mapping = Collections.unmodifiableMap(hashMap);
    }

    final String month;
    final String day;
    final String[] aliases;

    Quarter(String month, String day, String[] aliases) {
        this.month = month;
        this.day = day;
        this.aliases = aliases;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public static Quarter parse(String text) {
        return mapping.get(text);
    }

    @Override
    public String toString() {
        return this.month + "-" + this.day;
    }

    public String[] getAliases() {
        return aliases;
    }
}

package com.tgw360.uip.nlp;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 中文转数字
 * Created by 邹祥 on 2017/12/21 16:01
 */
public class ChineseNumberParser {

    private static final double SHI = 10;   // 十
    private static final double BAI = 100;  // 百
    private static final double QIAN = 1000;    //千
    private static final double WANG = 10000;   //万
    private static final double YI = 100000000; // 亿
    public static final Map<String, Double> map = new HashMap() {
        {
            put("零", 0d);
            put("一", 1d);
            put("二", 2d);
            put("三", 3d);
            put("四", 4d);
            put("五", 5d);
            put("六", 6d);
            put("七", 7d);
            put("八", 8d);
            put("九", 9d);
        }
    };

    private static final boolean DEBUG = false; // 用于debug

    /**
     * 中文转数字
     *
     * @param s
     * @return
     */
    public static double parse(String s) {
        // 去除所有的零
        return parseYI(s.replace("零", ""));
    }

    /**
     * 处理包含“亿”的情况
     *
     * @param s
     * @return
     */
    public static double parseYI(String s) {
//        if (DEBUG) {
//            System.out.println("parseYI: " + s);
//        }
        if (StringUtils.isBlank(s))
            return 0d;
        int posYI = s.lastIndexOf("亿");
        if (posYI == -1)
            return parseWANG(s);
        String s1 = s.substring(0, posYI);
        String s2 = s.substring(posYI + 1);
        return parseYI(s1) * YI + parseWANG(s2);
    }

    /**
     * 处理包含“万”的情况
     *
     * @param s
     * @return
     */
    public static double parseWANG(String s) {
//        if (DEBUG) {
//            System.out.println("parseWANG: " + s);
//        }
        if (StringUtils.isBlank(s))
            return 0d;
        int posWANG = s.lastIndexOf("万");
        if (posWANG == -1) {
            return parseSmall(s);
        }
        String s1 = s.substring(0, posWANG);
        String s2 = s.substring(posWANG + 1);
        return parseWANG(s1) * WANG + parseSmall(s2);
    }

    /**
     * 处理不带“万”和“亿”的
     *
     * @param s
     * @return
     */
    public static double parseSmall(String s) {
//        if (DEBUG) {
//            System.out.println("parseSmall: " + s);
//        }
        if ("十".equals(s))  // 对“十”进行特殊处理，因为十前面的一可以省略
            return 10d;
        if (StringUtils.isBlank(s))
            return 0d;
        int posQIAN = s.lastIndexOf("千");
        int posBAI = s.lastIndexOf("百");
        int posSHI = s.lastIndexOf("十");
        int start = 0;
        String s1 = null;
        String s2 = null;
        String s3 = null;
        String s4 = null;
        if (posQIAN != -1) {
            s1 = s.substring(start, posQIAN);
            start = posQIAN + 1;
        }
        if (posBAI != -1) {
            s2 = s.substring(start, posBAI);
            start = posBAI + 1;
        }
        if (posSHI != -1) {
            s3 = s.substring(start, posSHI);
            start = posSHI + 1;
        }
        s4 = s.substring(start);
        return parseNumber(s1) * QIAN + parseNumber(s2) * BAI + parseNumber(s3) * SHI + parseNumber(s4);
    }

    /**
     * 处理纯数字，不带 十，百，千，万，亿等单位的。
     *
     * @param s
     * @return
     */
    private static double parseNumber(String s) {
//        if (DEBUG) {
//            System.out.println("parseNumber: " + s);
//        }
        if (map.containsKey(s))
            return map.get(s);
        if (StringUtils.isBlank(s))
            return 0d;
        return Double.parseDouble(s);
    }

}

package com.tgw360.uip.algorithm.similarity;

import com.tgw360.uip.nlp.HanLPHelper;
import com.tgw360.uip.nlp.MyNature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 余弦距离
 * Created by 邹祥 on 2017/12/25 9:39
 */
public class CosineDistanceCalculator implements NatureVectorDistanceCalculator {
    @Override
    public double calculate(NatureVector a, NatureVector b) {
        Map<MyNature, Integer> map1 = a.getMap();
        Map<MyNature, Integer> map2 = b.getMap();
        double numerator = 0;   // 分子
        double denominator = a.getDenominator() * b.getDenominator();   // 分母
        for (MyNature nature : MyNature.values()) {
            numerator += map1.get(nature) * map2.get(nature);
        }
        return numerator / denominator;
    }

    public static void main(String[] args) {
        HanLPHelper.init();
        List<String> sentences = new ArrayList<>();
        sentences.add("今年二季度基本每股收益比去年一季度高5%");
        sentences.add("今年二季度基本每股收益大于5");
        sentences.add("概念是虚拟现实");
        String obj = "基本每股收益比去年高";
        for (String s : sentences) {
            System.out.println(s + "\t" + new CosineDistanceCalculator().calculate(new NatureVector(s), new NatureVector(obj)));
        }
    }
}

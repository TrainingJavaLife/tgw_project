package com.tgw360.uip.algorithm.similarity;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.common.BaseObject;
import com.tgw360.uip.nlp.MyNature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 词性向量
 * Created by 邹祥 on 2017/12/25 9:23
 */
public class NatureVector extends BaseObject {
    private Map<MyNature, Integer> map;
    private Double denominator = null;  // 用于计算余弦距离时的分母部分，避免重复计算

    {
        // 初始化map
        map = new HashMap<>();
        for (MyNature nature : MyNature.values()) {
            map.put(nature, 0);
        }
    }

    public NatureVector(String w) {
        this(HanLP.segment(w));
    }

    public NatureVector(List<Term> terms) {
        for (Term term : terms) {
            MyNature nature = MyNature.parse(term.nature.name());
            if (nature != MyNature.UNKNOWN) {
                map.put(nature, map.get(nature) + 1);
            }
        }
    }

    public Map<MyNature, Integer> getMap() {
        return map;
    }

    public double getDenominator() {
        if (denominator == null) {
            denominator = 0d;
            for (Integer value : map.values()) {
                denominator += value * value;
            }
        }
        denominator = Math.sqrt(denominator);
        return denominator;
    }

}

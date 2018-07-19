package com.tgw360.uip.algorithm.similarity;

/**
 * 计算两个词性向量间的距离
 * Created by 邹祥 on 2017/12/25 9:38
 */
public interface NatureVectorDistanceCalculator {
    /**
     * 计算两个词性向量间的距离
     *
     * @param a
     * @param b
     * @return
     */
    double calculate(NatureVector a, NatureVector b);

//    double calculate(String a, String b);
}

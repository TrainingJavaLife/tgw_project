package com.tgw360.uip.entity.answer.askmore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * “你是不是还想问”个股相关的问题生成策略
 * Created by 邹祥 on 2018/1/2 14:53
 */
public class SingleStockAskMoreStratage implements AskMoreStratage {

    private static final Random random = new Random();

    @Override
    public List<String> get(int n) {
        List<String> result = new ArrayList<>();
        while (result.size() < n) {
            result.add(getOne());
        }
        return result;
    }

    public String getOne() {
        int i = random.nextInt(6);  // 随机生成0-5的整数
        switch (i) {
            case 0:
//                System.out.println("指标");
                break;
            case 1:
            case 2:
//                System.out.println("概念");
                break;
            default:
//                System.out.println("资讯");
                break;
        }
        return null;
    }

    public static void main(String[] args) {
        new SingleStockAskMoreStratage().get(3);
    }
}

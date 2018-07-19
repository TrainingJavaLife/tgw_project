package com.tgw360.uip.nlp;

import com.tgw360.uip.common.Constants;
import org.junit.Test;

import java.text.DecimalFormat;

/**
 * 中文转数字 测试类
 * Created by 邹祥 on 2017/12/21 17:42
 */
public class ChineseNumberParserTest {
    static DecimalFormat decimalFormat = new DecimalFormat("#0.00");    // 避免使用科学计数法输出

    private void assertEqual(String s, double value) {    // 测试用
        double result = ChineseNumberParser.parse(s);
        if (Math.abs(result - value) < Constants.INFINITESIMAL) {
//            System.out.println("OK");
        } else {
            throw new RuntimeException(String.format("%s --> %s, 期望： %s", s,
                    decimalFormat.format(result), decimalFormat.format(value)));
        }
    }

    @Test
    public void test1() {
        assertEqual("5000亿200万1234", 500002001234d);
        assertEqual("5000亿1234", 500000001234d);
        assertEqual("三", 3d);
        assertEqual("三十六", 36d);
        assertEqual("200万1234", 2001234d);
        assertEqual("0.15", 0.15d);
        assertEqual("1.5万亿", 1500000000000d);
        assertEqual("1234万亿", 1234000000000000d);
        assertEqual("1.5千亿", 150000000000d);
        assertEqual("5千亿", 500000000000d);
        assertEqual("500万", 5000000d);
        assertEqual("五百万", 5000000d);
        assertEqual("123456", 123456d);
        assertEqual("五千亿四千五百万三千七百五十二", 500045003752d);
        assertEqual("十万", 100000d);
        assertEqual("十亿", 1000000000d);
        assertEqual("十万亿", 10000000000000d);
        assertEqual("零", 0d);
        assertEqual("九千五百万亿四千五百万三千七百五十二", 9500000045003752d);
        assertEqual("一千零一十五", 1015d);
        assertEqual("二十亿五千五百零一万四千零一十", 2055014010d);
        assertEqual("一万", 10000d);
    }
}

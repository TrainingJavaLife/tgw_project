package com.tgw360.uip.nlp;

import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.tgw360.uip.condition.FinanceIndex;
import com.tgw360.uip.condition.Operator;
import com.tgw360.uip.condition.Quarter;
import com.tgw360.uip.entity.Concept;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.repository.ConceptRepository;
import com.tgw360.uip.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HanLP工具类
 * Created by 邹祥 on 2017/12/4 13:16
 */
public class HanLPHelper {

    private static final Logger log = LoggerFactory.getLogger(HanLPHelper.class);

    private static boolean initialized = false;

    /**
     * 初始化自定义词典
     */
    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        log.info("初始化HanLP自定义词典");
        initConcept();  // 概念一定要在股票前面，后面好处理股票和概念同名的情况。 "机器人"
        initStock();
        initYear();
        initQuarter();
        initIndex();
        initOp();
        initAdjective();
        initElse();
    }

    /**
     * 添加自定义词汇到HanLP词典
     *
     * @param word   词汇
     * @param nature 词性
     */
    private static void insert(String word, MyNature nature) {
        CustomDictionary.insert(word, nature.name() + " 1");
    }

    private static void initStock() {
        Map<String, Stock> map = new StockRepository().findAllStockBasicInfo();
        for (Stock stock : map.values()) {
            insert(stock.getName().replace(" ", ""), MyNature.STOCK);
            insert(stock.getCode(), MyNature.STOCK);
        }
    }

    private static void initConcept() {
        // TODO: 动态更新？
        List<Concept> list = new ConceptRepository().findAll();
        for (Concept concept : list) {
            insert(concept.getName().replace("概念", ""), MyNature.CONCEPT);
        }
    }

    private static void initAdjective() {
        String[] arr = {"大", "小", "高", "低", "多", "少"};
        for (String s : arr) {
            insert(s, MyNature.OPERATOR);
        }
    }

    private static void initOp() {
        for (Operator op : Operator.values()) {
            for (String s : op.getAliases()) {
                insert(s, MyNature.OPERATOR);
            }
        }
    }

    private static void initYear() {
        List<String> list = new ArrayList<>();
        list.add("今年");
        list.add("去年");
        list.add("上年");
        list.add("前年");
        for (int i = 1980; i <= 2050; i++) {
            list.add(i + "年");
        }
        for (String s : list) {
            insert(s, MyNature.YEAR);
        }
    }

    private static void initQuarter() {
        for (Quarter quarter : Quarter.values()) {
            for (String s : quarter.getAliases()) {
                insert(s, MyNature.QUARTER);
            }
        }
    }

    private static void initIndex() {
        Map<String, FinanceIndex> mapping = FinanceIndex.getMapping();
        for (String s : mapping.keySet()) {
            insert(s, MyNature.INDEX);
        }
    }

    private static void initElse() {
        insert("概念", MyNature.KEYWORD_GAINIAN);
        insert("行情", MyNature.KEYWORD_HANGQING);
        insert("新闻", MyNature.KEYWORD_DOCUMENT);
        insert("资讯", MyNature.KEYWORD_DOCUMENT);
        insert("公告", MyNature.KEYWORD_DOCUMENT);
        insert("研报", MyNature.KEYWORD_DOCUMENT);
    }

}

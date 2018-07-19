package com.tgw360.uip.Table;

import com.tgw360.uip.condition.ConceptCondition;
import com.tgw360.uip.condition.Condition;
import com.tgw360.uip.nlp.HanLPHelper;
import com.tgw360.uip.condition.IndexCondition;
import com.tgw360.uip.controller.SearchController;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.repository.StockRepository;
import org.elasticsearch.common.collect.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Table测试类
 * Created by 邹祥 on 2017/12/20 16:04
 */
public class TableTest {
    static SearchController controller = new SearchController();
    static StockRepository repository = new StockRepository();

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        HanLPHelper.init();
//        String w = "基本每股收益比去年高，虚拟现实，基本每股收益比前年高100%";
        String w = "虚拟现实";
        List<Condition> conditions = controller.parseAll(w);
        List<Map<String, ?>> maps = getMaps(conditions);
        List<String> validCodes = getValidCodes(maps);
        // 生成表格
        Table table = new Table();
        table.addHeader("序号");
        table.addHeader("股票代码");
        table.addHeader("股票简称");
        table.addHeader("现价");
        table.addHeader("概念个数");
        for (int i = 0; i < validCodes.size(); i++) {
            String code = validCodes.get(i);
            table.addRow();
            table.appendToRow(i, 0);
            table.appendToRow(i, code);    // 股票代码
            Stock stock = repository.findByCodeOrName(code);    // TODO: 改为一次查询所有股票信息
            table.appendToRow(i, stock.getName());   // 股票简称
            table.appendToRow(i, "1.0");   // 现价
            table.appendToRow(i, stock.getConcepts().size());   // 概念个数
        }
        int suffixNumber = 1;
        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            if (condition instanceof ConceptCondition) {
                table.addHeader("概念");
                for (int j = 0; j < validCodes.size(); j++)
                    table.appendToRow(j, ((ConceptCondition) condition).getConcept());
            } else if (condition instanceof IndexCondition) {
                IndexCondition ic = (IndexCondition) condition;
                if (ic.getType() == IndexCondition.Type.SIMPLE) {
                    table.addHeader(ic.getLeftLable());
                    for (int j = 0; j < validCodes.size(); j++) {
                        table.appendToRow(j, maps.get(i).get(validCodes.get(j)));
                    }
                } else {
                    table.addHeader(String.format("%s[%s]", ic.getLeftLable(), suffixNumber++));
                    table.addHeader(String.format("%s[%s]", ic.getRightLable(), suffixNumber++));
                    if (ic.getPercentage()) {
                        table.addHeader(String.format("([%s]-[%s])/[%s]", suffixNumber - 2, suffixNumber - 1, suffixNumber - 1));
                    } else {
                        table.addHeader(String.format("([%s]-[%s])", suffixNumber - 2, suffixNumber - 1));
                    }
                    for (int j = 0; j < validCodes.size(); j++) {
                        Tuple<Double, Double> tuple = (Tuple<Double, Double>) maps.get(i).get(validCodes.get(j));
                        double v1 = tuple.v1();
                        double v2 = tuple.v2();
                        table.appendToRow(j, v1);
                        table.appendToRow(j, v2);
                        double v3 = (ic.getPercentage() ? (v1 - v2) / v2 : (v1 - v2));
                        v3 = ((long) (v3 * 100)) / 100.0;
                        table.appendToRow(j, v3);
                    }
                }
            }
        }
        // 排序
        int sortColumn = 1;
        SortOrder sortOrder = SortOrder.ASC;
        table.sort(sortColumn, sortOrder);
        System.out.println(table);
    }

    public static List<String> getValidCodes(List<Map<String, ?>> maps) {
        List<String> validCodes = new ArrayList<>();
        for (String code : maps.get(0).keySet()) {
            boolean exitInAllMaps = true;
            for (int i = 1; i < maps.size(); i++) {
                if (!maps.get(i).containsKey(code)) {
                    exitInAllMaps = false;
                    break;
                }
            }
            if (exitInAllMaps) {
                validCodes.add(code);
            }
        }
        return validCodes;
    }

    public static List<Map<String, ?>> getMaps(List<Condition> conditions) {
        List<Map<String, ?>> maps = new ArrayList<>();
        for (Condition condition : conditions) {
            Map<String, ?> map;
            if (condition instanceof ConceptCondition) {
                map = repository.findByConceptCondition((ConceptCondition) condition);
            } else {
                IndexCondition ic = (IndexCondition) condition;
                if (ic.getType() == IndexCondition.Type.SIMPLE) {
                    map = repository.findBySimpleIndexCondition(ic);
                } else {
                    map = repository.findByComplexIndexCondition(ic);
                }
            }
            maps.add(map);
        }
        return maps;
    }
}

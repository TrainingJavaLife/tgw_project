package com.tgw360.uip.service.impl;

import com.tgw360.uip.Table.Table;
import com.tgw360.uip.common.TimeCounter;
import com.tgw360.uip.condition.ConceptCondition;
import com.tgw360.uip.condition.Condition;
import com.tgw360.uip.condition.IndexCondition;
import com.tgw360.uip.entity.Finance;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.query.SearchByConditionQuery;
import com.tgw360.uip.repository.StockRepository;
import com.tgw360.uip.service.StockService;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.common.collect.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 邹祥 on 2017/11/22 9:05
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;
    private Map<String, Stock> allStock;

    @Override
    public List<Stock> findByPrefix(String prefix) {
        return stockRepository.findByPrefix(prefix);
    }

    @Override
    public Stock findByCodeOrName(String w) {
        return stockRepository.findByCodeOrName(w);
    }

    TimeCounter tc;

    @Override
    public Table findByConditions(SearchByConditionQuery query) throws Exception {
        tc = new TimeCounter();
        List<Condition> conditions = query.getConditions();
        List<Map<String, ?>> maps = getMaps(conditions);
        System.out.print("查询所有条件总耗时： ");
        tc.countAll();
        List<String> validCodes = getValidCodes(maps);
        tc.count("合并集合耗时: %s\n");
        Map<String, Stock> stocks = getAllStock();
        tc.count("获取所有股票信息耗时:%s\n");
        // 生成表格
        Table table = new Table();
        table.addHeader("序号");
        table.addHeader("股票代码");
        table.addHeader("股票简称");
        table.addHeader("现价");
        table.addHeader("概念个数");
        for (int i = 0; i < validCodes.size(); i++) {
            String code = validCodes.get(i);
            Stock stock = stocks.get(code);
            if (stock == null)      // 刚上市的股票，两处信息不一致而导致查询不到
                stock = new Stock();
            table.addRow();
            table.appendToRow(i, 0);
            table.appendToRow(i, code);    // 股票代码
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
        tc.count("生成结果耗时：%s\n");
        // 排序
        table.sort(query.getSortColumn(), query.getSortOrder());
        tc.count("排序耗时：%s\n");
        tc.countAll();
        return table;
    }

    private List<Map<String, ?>> getMaps(List<Condition> conditions) {
        List<Map<String, ?>> maps = new ArrayList<>();
        for (Condition condition : conditions) {
            Map<String, ?> map;
            if (condition instanceof ConceptCondition) {
                map = stockRepository.findByConceptCondition((ConceptCondition) condition);
            } else {
                IndexCondition ic = (IndexCondition) condition;
                if (ic.getType() == IndexCondition.Type.SIMPLE) {
                    map = stockRepository.findBySimpleIndexCondition(ic);
                } else {
                    map = stockRepository.findByComplexIndexCondition(ic);
                }
            }
            maps.add(map);
            System.out.println(condition);
            tc.count("查询条件结果耗时：%s\n");
        }
        return maps;
    }

    private List<String> getValidCodes(List<Map<String, ?>> maps) {
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

    private Map<String, Stock> getAllStock() {
        // 这个要动态更新
        return allStock;
    }

    @PostConstruct
    private void postConstruct() {
        this.allStock = stockRepository.findAllStockBasicInfo();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private double getField(Stock stock, String endDate, String field) throws Exception {
        List<Finance> finances = stock.getFinances();
        for (Finance finance : finances) {
            String s1 = sdf.format(finance.getEndDate());
            if (s1.equals(endDate)) {
                return (double) PropertyUtils.getProperty(finance, field);
            }
        }
        return 0;
    }

}

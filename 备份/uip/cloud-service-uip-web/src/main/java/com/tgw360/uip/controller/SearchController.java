package com.tgw360.uip.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.tgw360.uip.Table.Table;
import com.tgw360.uip.nlp.ChineseNumberParser;
import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.common.UUIDUtils;
import com.tgw360.uip.condition.*;
import com.tgw360.uip.dto.SearchSuggestionsDTO;
import com.tgw360.uip.dto.search.result.*;
import com.tgw360.uip.entity.Concept;
import com.tgw360.uip.entity.Stock;
import com.tgw360.uip.exception.UipConditionParseException;
import com.tgw360.uip.nlp.MyNature;
import com.tgw360.uip.query.SearchByConditionQuery;
import com.tgw360.uip.service.ConceptService;
import com.tgw360.uip.service.StockService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tgw360.uip.condition.IndexCondition.thisYear;

/**
 * 搜索相关的Controller
 * Created by 邹祥 on 2017/12/5 14:22
 */
@RestController
public class SearchController extends BaseController {

    private static final String LRU_MAP_KEY = "LRU_MAP_KEY";
    private static final Integer LRU_MAP_SIZE = 3;  // 设置为多少合适？

    @Autowired
    private StockService stockService;
    @Autowired
    private ConceptService conceptService;

    /**
     * 搜股票时的建议
     *
     * @param w
     * @return
     * @throws Exception
     */
    @GetMapping("/search/suggestions")
    public Object searchSuggestions(String w) throws Exception {
        CommonResult<List<SearchSuggestionsDTO>> result = new CommonResult<>();
        List<Stock> stocks = stockService.findByPrefix(w);
        List<SearchSuggestionsDTO> list = new ArrayList<>();
        for (Stock stock : stocks) {
            SearchSuggestionsDTO dto = new SearchSuggestionsDTO();
            BeanUtils.copyProperties(dto, stock);
            list.add(dto);
        }
        result.setData(list);
        return result;
    }

    @GetMapping("/search")
    public Object search(String w, HttpSession session) throws Exception {
        assertNotBlank(w, "w不能为空");
        w = preHandle(w); // 对用户输入进行预处理
        Stock stock = stockService.findByCodeOrName(w);
        if (stock != null) { // 如果匹配到股票名称或代码
            SingleStockSearchResultDTO dto = new SingleStockSearchResultDTO(w);
            dto.setStock(stock);
            return new CommonResult<>(dto);
        }
        Concept concept = conceptService.findByName(w);
        if (concept != null) { // 如果匹配到概念
            SingleConceptSearchResultDTO dto = new SingleConceptSearchResultDTO(w);
            List<Condition> conditions = new ArrayList<>();
            ConceptCondition condition = new ConceptCondition();
            condition.setConcept(concept.getName());
            conditions.add(condition);
            dto.setConcept(concept);
            String token = UUIDUtils.getUUID();
            dto.setToken(token);
            // 将 conditions 存入session中
            LRUMap lruMap = getOrCreateLRUMap(session);
            lruMap.put(token, conditions);
            return new CommonResult<>(dto);
        }
        List<Condition> conditions = parseAll(w);   // 将用户输入转换成查询条件
        if (conditions.stream().filter(s -> s != null).count() > 0) {   // 如果至少有一个转换成功的查询条件
            ConditionSearchResultDTO dto = new ConditionSearchResultDTO(w);
            dto.setConditions(conditions.stream().map(c -> c.toString()).collect(Collectors.toList()));
            String token = UUIDUtils.getUUID();
            dto.setToken(token);
            // 将 conditions 存入session中
            LRUMap lruMap = getOrCreateLRUMap(session);
            lruMap.put(token, conditions);
            return new CommonResult<>(dto);
        }
        return new CommonResult<>(new NoneSearchResultDTO(w));
    }

    public List<Condition> parseAll(String w) {
        String[] sentences = w.split(",|，");    // 按逗号分隔成句
        List<Condition> conditions = new ArrayList<>(sentences.length);
        for (String s : sentences) {
            try {
                Condition condition = parse(s); // 尝试将句子转换成查询条件
                conditions.add(condition);
            } catch (Exception e) {
                // 转换失败
                conditions.add(new WrongCondition(e.getMessage()));
            }
        }
        return conditions;
    }

    @GetMapping("debug/search/conditions")
    public Object searchByConditions_DEBUG(SearchByConditionQuery query, HttpSession session) throws Exception {
        LRUMap lruMap = getOrCreateLRUMap(session);
        List<Condition> conditions = (List<Condition>) lruMap.get(query.getToken());
        if (conditions == null || conditions.size() == 0) {
            return CommonResult.TOKEN_INVALID_RESULT;
        }
        query.setConditions(conditions);
        Table table = stockService.findByConditions(query);
        Table.TablePage page = table.page(query.getPageNum(), query.getPageSize());
        return page.toString();
    }

    @GetMapping("/search/conditions")
    public Object searchByConditions(SearchByConditionQuery query, HttpSession session) throws Exception {
        LRUMap lruMap = getOrCreateLRUMap(session);
        List<Condition> conditions = (List<Condition>) lruMap.get(query.getToken());
        if (conditions == null || conditions.size() == 0) {
            return CommonResult.TOKEN_INVALID_RESULT;
        }
        query.setConditions(conditions);
        Table table = stockService.findByConditions(query);
        Table.TablePage page = table.page(query.getPageNum(), query.getPageSize());
        System.out.println(page);
        CommonResult<Table.TablePage> result = new CommonResult<>(page);
        return result;
    }

    /**
     * 返回session中的LRUMap，如果没有就新建一个并放入session
     *
     * @param session
     * @return
     */
    public LRUMap getOrCreateLRUMap(HttpSession session) {
        LRUMap lruMap = (LRUMap) session.getAttribute(LRU_MAP_KEY);
        if (lruMap == null) {
            lruMap = new LRUMap(LRU_MAP_SIZE);
        }
        session.setAttribute(LRU_MAP_KEY, lruMap);
        return lruMap;
    }

    /**
     * 预处理。EPS大於5 --> eps大于5
     *
     * @param s
     * @return
     */
    private String preHandle(String s) {
        // 繁体转简体
        s = HanLP.convertToSimplifiedChinese(s);
        // 大写字母转小写
        s = s.toLowerCase();
        return s;
    }

    /**
     * 将字符串转换成查询条件
     *
     * @param s
     * @return
     * @throws UipConditionParseException
     */
    public Condition parse(String s) throws UipConditionParseException {
        if (StringUtils.isBlank(s))
            return null;
        List<Term> terms = HanLP.segment(s);
        boolean hasConcept = false; // 是否包含概念
        String concept = null;
        boolean hasIndex = false;   // 是否包含财务指标
        for (Term term : terms) {
            if (term.nature.name().equals(MyNature.CONCEPT.name())) {
                hasConcept = true;
                concept = term.word;
            }
            if (term.nature.name().equals(MyNature.INDEX.name()))
                hasIndex = true;
        }
        if (hasConcept) {
            // 后面再添加字段...
            ConceptCondition condition = new ConceptCondition();
            condition.setConcept(concept);
            return condition;
        }
        if (hasIndex) {
            IndexCondition condition = new IndexCondition();
            boolean isLeft = true;  // 指示当前是比较符的左边还是右边
            for (Term term : terms) {
                switch (MyNature.parse(term.nature.name())) {
                    case YEAR:
                        condition.setYear(parseStringToYear(term.word), isLeft);
                        break;
                    case QUARTER:
                        condition.setQuarter(parseStringToQuarter(term.word), isLeft);
                        break;
                    case INDEX:
                        condition.setIndex(FinanceIndex.parse(term.word), isLeft);
                        break;
                    case OPERATOR:
                        condition.setOperator(Operator.parse(term.word));
                        isLeft = false; // 从左边转换到右边
                        break;
                    case m:
                        condition.setNumber(ChineseNumberParser.parse(term.word));
                        break;
                    case nx:
                        if (term.word.equals("%"))
                            condition.setPercentage(true);
                    default:
                        if (term.word.equals("比"))
                            isLeft = false;
                }
            }
            if (condition.complete() != IndexCondition.Type.WRONG) {
                return condition;
            } else {
                return null;
            }
        }
        return null;
    }

    private int parseStringToYear(String str) {
        switch (str) {
            case "今年":
                return thisYear();
            case "去年":
                return thisYear() - 1;
            case "前年":
                return thisYear() - 2;
            default:
                return Integer.parseInt(str.substring(0, 4));
        }
    }

    private Quarter parseStringToQuarter(String str) throws UipConditionParseException {
        if (str.contains("1") || str.contains("一"))
            return Quarter.FIRST;
        if (str.contains("2") || str.contains("二")) {
            return Quarter.SECOND;
        }
        if (str.contains("3") || str.contains("三")) {
            return Quarter.THIRD;
        }
        if (str.contains("4") || str.contains("四")) {
            return Quarter.FOURTH;
        }
        throw new UipConditionParseException("异常季度数据： " + str);
    }

}

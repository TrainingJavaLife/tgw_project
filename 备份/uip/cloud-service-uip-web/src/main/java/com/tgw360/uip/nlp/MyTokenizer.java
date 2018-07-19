package com.tgw360.uip.nlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 分词器
 * 在HanLP标准分词器的结果之上再进行一些处理。
 * Created by 邹祥 on 2017/12/28 13:31
 */
public class MyTokenizer {

    static {
        HanLPHelper.init();
    }

    public static List<Term> segment(String text) {
        text = preProcess(text);
        List<Term> terms = HanLP.segment(text);
        postProcess(terms);
        return terms;
    }

    /**
     * 预处理
     *
     * @param text
     * @return
     */
    private static String preProcess(String text) {
        // 繁体转简体
        text = HanLP.convertToSimplifiedChinese(text);
        // 小写转大写
        text = text.toUpperCase();
        // HanLP分词时，不能识别 eps>1 的情况 ，因此这里将><=用中文替换
        text = text.replace(">", "大于")
                .replace("<", "小于")
                .replace("=", "等于");
        return text;
    }

    /**
     * 后处理
     *
     * @param terms
     * @return
     */
    private static void postProcess(List<Term> terms) {
        // 添加了日期识别的功能
        for (int i = terms.size() - 1; i >= 0; i--) {   // 倒序遍历，有删除操作
            Term term = terms.get(i);
            if (MyNature.m.eqaulTo(term.nature)) {
                if (term.word.matches("\\d{4}") && i != terms.size() - 1
                        && MyNature.QUARTER.eqaulTo(terms.get(i + 1).nature)) {
                    term.nature = Nature.create(MyNature.YEAR.name());
                }
                if (term.word.matches("\\d{8}")
                        || term.word.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
                    String dateString = convertToStandardDateString(term.word);
                    if (dateString != null) {
                        term.nature = Nature.create(MyNature.DATE.name());
                        term.word = dateString;
                    }
                }
                if (i + 4 < terms.size()
                        && set.contains(terms.get(i + 1).word)
                        && terms.get(i + 2).nature == Nature.m
                        && set.contains(terms.get(i + 3).word)
                        && terms.get(i + 4).nature == Nature.m) {
                    String str = term.word + terms.get(i + 2).word + terms.get(i + 4).word;
                    String dateString = convertToStandardDateString(str);
                    if (dateString != null) {
                        terms.remove(i + 4);
                        terms.remove(i + 3);
                        terms.remove(i + 2);
                        terms.remove(i + 1);
                        term.word = dateString;
                        term.nature = Nature.create(MyNature.DATE.name());
                    }
                }
            }
        }
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat shortSDF = new SimpleDateFormat("yyyyMMdd");

    static Set<String> set = new HashSet<String>() {{
        add("/");
        add("-");
        // 用.分隔是，HanLP会识别成数值形式。
    }};

    /**
     * 尝试将字符串转换成标准的日期字符串。
     *
     * @param s
     * @return 转换之后的字符串。如果失败，则返回null
     */
    private static String convertToStandardDateString(String s) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    sb.append(ch);
                }
            }
            if (sb.length() != 8)
                return null;
            // TODO: 判断8位数字 日期的合法性
            return sdf.format(shortSDF.parse(sb.toString()));
        } catch (Exception e) {
            return null;
        }
    }

}

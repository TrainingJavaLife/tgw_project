package com.tgw360.uip.entity;

import com.tgw360.uip.common.BaseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文档：新闻、公告、研报的公共父类
 * Created by 邹祥 on 2017/11/20 15:06
 */
public abstract class Document extends BaseObject {

    /**
     * 文档类型
     */
    public enum Type {
        ALL(0, "全部"),
        NEWS(1, "新闻"),
        NEWS2(1, "资讯"),
        ANNOUNCEMENT(2, "公告"),
        RESEARCH_REPORT(3, "研报"),;

        private Integer id;
        private String chineseName;

        Type(Integer id, String chineseName) {
            this.id = id;
            this.chineseName = chineseName;
        }

        public static Type parse(Integer id) {
            for (Type type : Type.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }

        public static Type parse(String chineseName) {
            for (Type type : Type.values()) {
                if (type.chineseName.equals(chineseName)) {
                    return type;
                }
            }
            return null;
        }

        public String chineseName() {
            return chineseName;
        }
    }

    /**
     * 新闻
     */
    public static final int TYPE_NEWS = 1;
    /**
     * 公告
     */
    public static final int TYPE_ANNOUNCEMENT = 2;
    /**
     * 研报
     */
    public static final int TYPE_RESEARCH_REPORT = 3;

    private Long id;
    private Integer type; // 类型
    private String title; // 标题
    private Date date; // 日期
    private String organization; // 机构
    private String content; // 内容
    private String shortContent;    // 内容的前70个字符。
    private List<Stock> involvedStocks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Stock> getInvolvedStocks() {
        return involvedStocks;
    }

    public void setInvolvedStocks(List<Stock> involvedStocks) {
        this.involvedStocks = involvedStocks;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }
}

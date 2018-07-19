package com.tgw360.uip.entity;


/**
 * 研报实体类
 * Created by 邹祥 on 2017/11/20 15:08
 */
public class ResearchReport extends Document{
    {
        this.setType(Document.TYPE_RESEARCH_REPORT);
    }

    private String author; // 研报作者
    private String rating; // 研报类型

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

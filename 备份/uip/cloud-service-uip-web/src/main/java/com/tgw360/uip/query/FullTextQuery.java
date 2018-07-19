package com.tgw360.uip.query;

/**
 * 全文搜索相关参数
 * Created by 危宇 on 2017/12/7 17:54
 */
public class FullTextQuery extends PageQuery{
    private String w;          // 输入关键字
    private Integer q = 0;     // 0.默认查询全文 1.查询标题
    private Integer time = 0;  // 0.默认全部时间 1.自定义事件 timebegin timeEnd 2.最近3天 3.最近一周 4.最近三个月
    private Integer sort = 0;  // 0.相关度降序排序 1.时间降序排序
    private Integer type = 1;  // 1.新闻 2.研报 3.公告
    private String timeBegin; // 自定义开始时间
    private String timeEnd;    // 自定义结束时间

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public Integer getQ() {
        return q;
    }

    public void setQ(Integer q) {
        this.q = q;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}

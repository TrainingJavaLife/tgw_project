package com.tgw360.uip.entity;

/**
 * 公告实体类
 * Created by 邹祥 on 2017/11/20 15:07
 */
public class Announcement extends Document {
    {
        this.setType(Document.TYPE_ANNOUNCEMENT);
    }

    private String url;  // 下载连接

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

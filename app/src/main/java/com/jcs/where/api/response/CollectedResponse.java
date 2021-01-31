package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * create by zyf on 2021/1/31 4:39 下午
 */
public class CollectedResponse {

    /**
     * type : 3
     * news : {"id":64,"content_type":2,"title":"Wonderland of long ago: and how she would keep, through all her coaxing.","cover_images":["https://whereoss.oss-accelerate.aliyuncs.com/news/covers/311055f76cd9466e91aab421ad9d11d1.jpg"],"video_time":"06:58","video_link":"https://whereoss.oss-cn-beijing.aliyuncs.com/videos/218679048-1-208.mp4","publisher":{"id":11,"nickname":"Where"},"created_at":"5个月前"}
     */

    @SerializedName("type")
    private Integer type;
    @SerializedName("news")
    private NewsResponse news;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public NewsResponse getNews() {
        return news;
    }

    public void setNews(NewsResponse news) {
        this.news = news;
    }
}

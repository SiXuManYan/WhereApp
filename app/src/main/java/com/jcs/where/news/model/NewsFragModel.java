package com.jcs.where.news.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.PageResponse;

/**
 * create by zyf on 2021/1/18 10:01 下午
 */
public class NewsFragModel extends BaseModel {
    public void getNews(int channelId, String input, BaseObserver<PageResponse<NewsResponse>> observer) {
        dealResponse(mRetrofit.getNews(channelId, input), observer);
    }

    public void getNews(int channelId, BaseObserver<PageResponse<NewsResponse>> observer) {
        getNews(channelId, "", observer);
    }

    public void getNews(String input, BaseObserver<PageResponse<NewsResponse>> observer) {
        getNews(0, input, observer);
    }
}

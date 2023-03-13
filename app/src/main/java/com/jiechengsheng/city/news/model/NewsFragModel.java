package com.jiechengsheng.city.news.model;

import com.jiechengsheng.city.api.BaseModel;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.response.NewsResponse;
import com.jiechengsheng.city.api.response.PageResponse;

/**
 * create by zyf on 2021/1/18 10:01 下午
 */
public class NewsFragModel extends BaseModel {

    public void getNews(Integer channelId, String input, int page, BaseObserver<PageResponse<NewsResponse>> observer) {
        dealResponse(mRetrofit.getNews(channelId, input , page), observer);
    }




}

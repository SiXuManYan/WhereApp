package com.jcs.where.news.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.NewsDetailResponse;

/**
 * create by zyf on 2021/1/27 11:34 下午
 */
public class NewsVideoModel extends BaseModel {
    public void getNewsDetail(String newsId, BaseObserver<NewsDetailResponse> observer) {
        dealResponse(mRetrofit.getNewsDetail(newsId), observer);
    }
}

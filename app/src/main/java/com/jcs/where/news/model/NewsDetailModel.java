package com.jcs.where.news.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.NewsDetailResponse;
import com.jcs.where.api.response.NewsResponse;

/**
 * create by zyf on 2021/1/25 12:27 上午
 */
public class NewsDetailModel extends BaseModel {
    public void getNewsDetail(String newsId, BaseObserver<NewsDetailResponse> observer) {
        dealResponse(mRetrofit.getNewsDetail(newsId), observer);
    }

}

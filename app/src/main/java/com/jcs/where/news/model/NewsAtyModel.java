package com.jcs.where.news.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.NewsTabResponse;

import java.util.List;

/**
 * create by zyf on 2021/1/18 9:26 下午
 */
public class NewsAtyModel extends BaseModel {

    public void getNewsTabs(BaseObserver<List<NewsTabResponse>> observer) {
        dealResponse(mRetrofit.getNewsTabs(), observer);
    }

}

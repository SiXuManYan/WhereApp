package com.jiechengsheng.city.news.model;

import com.jiechengsheng.city.api.BaseModel;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.response.NewsDetailResponse;
import com.jiechengsheng.city.api.response.NewsResponse;
import com.jiechengsheng.city.api.response.SuccessResponse;

import java.util.List;

/**
 * create by zyf on 2021/1/25 12:27 上午
 */
public class NewsDetailModel extends BaseModel {
    public void getNewsDetail(String newsId, BaseObserver<NewsDetailResponse> observer) {
        dealResponse(mRetrofit.getNewsDetail(newsId), observer);
    }

    public void postFollowPublish(int publisherId, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.postFollowNewsPublisher(publisherId), observer);
    }

    public void delFollowPublish(int publisherId, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.delFollowNewsPublisher(publisherId), observer);
    }

    public void getRecommendNews(BaseObserver<List<NewsResponse>> observer) {
        dealResponse(mRetrofit.getRecommendNews(), observer);
    }

    public void postCollectNews(String newsId, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.postCollectNews(newsId), observer);
    }

    public void delCollectNews(String newsId, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.delCollectNews(newsId), observer);
    }
}

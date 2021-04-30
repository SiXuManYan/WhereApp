package com.jcs.where.mine.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.PageResponse;

import java.util.List;

/**
 * create by zyf on 2021/1/31 3:48 下午
 */
public class CollectionListModel extends BaseModel {

    /**
     * 获得收藏页面-收藏的视频列表
     */
    public void getCollectionVideo(int page, BaseObserver<PageResponse<CollectedResponse>> observer) {
        dealResponse(mRetrofit.getCollectionVideo(page), observer);
    }


    /**
     * 获得收藏页面-收藏的文章列表
     */
    public void getCollectionArticle(int page, BaseObserver<PageResponse<CollectedResponse>> observer) {
        dealResponse(mRetrofit.getCollectionArticle(page), observer);
    }

    /**
     * 获得收藏页面-收藏的同城列表
     */
    public void getCollectionSameCity(int page, BaseObserver<PageResponse<CollectedResponse>> observer) {
        dealResponse(mRetrofit.getCollectionSameCity(page), observer);
    }

}

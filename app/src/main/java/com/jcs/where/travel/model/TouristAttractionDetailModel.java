package com.jcs.where.travel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.bean.TouristAttractionDetailResponse;
import com.jcs.where.api.response.CommentResponse;

/**
 * create by zyf on 2021/1/30 12:32 下午
 */
public class TouristAttractionDetailModel extends BaseModel {
    public void getTouristAttractionDetail(int touristAttractionId, BaseObserver<TouristAttractionDetailResponse> observer) {
        dealResponse(mRetrofit.getTouristAttractionDetail(touristAttractionId), observer);
    }

    public void getTouristAttractionCommentList(int touristAttractionId, BaseObserver<PageResponse<CommentResponse>> observer) {
        dealResponse(mRetrofit.getTouristAttractionCommentList(touristAttractionId), observer);
    }
}

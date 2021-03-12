package com.jcs.where.travel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.api.response.PageResponse;

/**
 * create by zyf on 2021/3/8 11:25 下午
 */
public class TravelCommentModel extends BaseModel {

    public void getTouristAttractionCommentList(int touristAttractionId, BaseObserver<PageResponse<CommentResponse>> observer) {
        dealResponse(mRetrofit.getTouristAttractionCommentList(touristAttractionId), observer);
    }
}

package com.jcs.where.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelCommentsResponse;

import java.util.List;

public class HotelCommentModel extends BaseModel {


    public void getHotelCommentNum(int hotelId, BaseObserver<List<Integer>> observer) {
        dealResponse(mRetrofit.getHotelCommentNum(hotelId), observer);
    }

    public void getComments(String hotelId, BaseObserver<HotelCommentsResponse> observer) {
        dealResponse(mRetrofit.getHotelComments(Integer.parseInt(hotelId)), observer);
    }

    /**
     * 获取酒店评价
     *
     * @param hotelId 酒店id
     * @param type    评价类型：1-晒图 2-低分 3-最新
     */
    public void getComments(int hotelId, int type, BaseObserver<HotelCommentsResponse> observer) {
        dealResponse(mRetrofit.getHotelComments(hotelId, type), observer);
    }
}

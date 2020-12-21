package com.jcs.where.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelDetailResponse;
import com.jcs.where.api.response.SuccessResponse;

import retrofit2.Response;

public class HotelDetailModel extends BaseModel {

    public void postCollectHotel(int hotelId, BaseObserver<Response<SuccessResponse>> observer) {
        dealResponse(mRetrofit.postCollectHotel(hotelId), observer);
    }

    public void delCollectHotel(int hotelId, BaseObserver<Response<SuccessResponse>> observer) {
        dealResponse(mRetrofit.delCollectHotel(hotelId), observer);
    }

    public void getHotelDetail(int hotelId, BaseObserver<HotelDetailResponse> observer) {
        dealResponse(mRetrofit.getHotelDetail(hotelId), observer);
    }
}

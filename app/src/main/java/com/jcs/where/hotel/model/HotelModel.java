package com.jcs.where.hotel.model;

import com.jcs.where.api.BaseLocationModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * 酒店预订Model
 * create by zyf on 2020/12/21 9:25 PM
 */
public class HotelModel extends BaseLocationModel {
    public void getYouLike(BaseObserver<List<HotelResponse>> observer) {
        // 猜你喜欢
        dealResponse(mRetrofit.getYouLike(), observer);
    }
}

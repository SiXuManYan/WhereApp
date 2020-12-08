package com.jcs.where.home.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelOrderDetailResponse;

public class HotelPayModel extends BaseModel {
    public void getHotelOrderDetail(int orderId, BaseObserver<HotelOrderDetailResponse> observer) {
        dealResponse(mRetrofit.getHotelOrderDetail(orderId), observer);
    }
}

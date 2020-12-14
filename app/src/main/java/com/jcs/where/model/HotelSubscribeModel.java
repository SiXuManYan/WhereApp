package com.jcs.where.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.response.HotelOrderResponse;

public class HotelSubscribeModel extends BaseModel {
    public void postHotelOrder(HotelOrderRequest request, BaseObserver<HotelOrderResponse> observer) {
        dealResponse(mRetrofit.postHotelOrder(request), observer);
    }
}

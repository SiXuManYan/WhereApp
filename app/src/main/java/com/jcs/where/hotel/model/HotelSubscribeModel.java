package com.jcs.where.hotel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.response.HotelOrderResponse;
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse;

public class HotelSubscribeModel extends BaseModel {
    public void postHotelOrder(HotelOrderRequest request, BaseObserver<HotelOrderResponse> observer) {
        dealResponse(mRetrofit.postHotelOrder(request), observer);
    }

    public void postHotelOrder2(HotelOrderRequest request, BaseObserver<HotelOrderCommitResponse> observer) {
        dealResponse(mRetrofit.commitHotelOrder(request), observer);
    }
}

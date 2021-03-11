package com.jcs.where.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelOrderDetailResponse;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.api.response.OrderNumResponse;
import com.jcs.where.api.response.PageResponse;

/**
 * create by zyf on 2020/12/11 8:21 PM
 */
public class OrderModel extends BaseModel {

    public void getOrderNum(BaseObserver<OrderNumResponse> observer) {
        dealResponse(mRetrofit.getOrderNum(), observer);
    }

    public void getOrderList(int type, String searchInput, int page, BaseObserver<PageResponse<OrderListResponse>> observer) {
        dealResponse(mRetrofit.getOrderList(type, searchInput, page), observer);
    }

    public void getHotelOrderDetail(int orderId, BaseObserver<HotelOrderDetailResponse> observer) {
        dealResponse(mRetrofit.getHotelOrderDetail(orderId), observer);
    }

}

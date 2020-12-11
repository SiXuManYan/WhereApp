package com.jcs.where.home.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.api.response.OrderNumResponse;

/**
 * create by zyf on 2020/12/11 8:21 PM
 */
public class OrderModel extends BaseModel {

    public void getOrderNum(BaseObserver<OrderNumResponse> observer) {
        dealResponse(mRetrofit.getOrderNum(), observer);
    }

    public void getOrderList(int type, BaseObserver<OrderListResponse> observer) {
        getOrderList(type, "", observer);
    }

    public void getOrderList(int type, String keyword, BaseObserver<OrderListResponse> observer) {
        dealResponse(mRetrofit.getOrderList(type, keyword), observer);
    }

}

package com.jcs.where.api;

/**
 * 含有获取Location的方法
 * create by zyf on 2020/12/21 9:22 PM
 */
public class BaseLocationModel extends BaseModel {
    public void getLocation(String lat, String lng, BaseObserver<String> observer) {
        dealResponse(mRetrofit.getLocation(lat + "," + lng), observer);
    }
}

package com.jcs.where.api;

import retrofit2.Retrofit;

public class BaseModel {
    protected RetrofitManager mManager;
    protected RetrofitApi mRetrofit;

    public BaseModel() {
        this.mManager = RetrofitManager.getManager();
        this.mRetrofit = mManager.getRetrofit().create(RetrofitApi.class);
    }
}

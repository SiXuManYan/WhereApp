package com.jcs.where.api;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseModel {

    protected RetrofitApi mRetrofit;

    public BaseModel() {
        RetrofitManager mManager = RetrofitManager.getManager();
        this.mRetrofit = mManager.getRetrofit().create(RetrofitApi.class);
    }



    protected <T> void dealResponse(Observable<T> observable, BaseObserver<T> observer) {
        observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }
}

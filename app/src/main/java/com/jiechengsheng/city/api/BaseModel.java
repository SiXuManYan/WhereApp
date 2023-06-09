package com.jiechengsheng.city.api;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseModel {

    protected RetrofitApi mRetrofit;
    protected BaseObserver<?> mObserver;


    public BaseModel() {
        RetrofitManager mManager = RetrofitManager.getManager();
        this.mRetrofit = mManager.getRetrofit().create(RetrofitApi.class);
    }



    protected <T> void dealResponse(Observable<JcsResponse<T>> observable, BaseObserver<T> observer) {
        mObserver = observer;
        observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }


}

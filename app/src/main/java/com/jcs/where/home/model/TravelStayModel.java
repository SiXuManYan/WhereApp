package com.jcs.where.home.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TravelStayModel extends BaseModel {
    public void getCategories(int level, int[] categories, BaseObserver<List<CategoryResponse>> observer) {
        mRetrofit.getCategories(level, categories).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    public void getYouLike(BaseObserver<List<HotelResponse>> observer) {
        mRetrofit.getYouLike().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }
}

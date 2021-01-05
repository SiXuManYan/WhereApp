package com.jcs.where.model;

import android.util.Log;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;

import java.util.List;

public class TravelStayModel extends BaseModel {
    public void getCategories(int level, List<Integer> categories, BaseObserver<List<CategoryResponse>> observer) {
        Log.e("TravelStayModel", "getCategories: " + categories.toString());
        dealResponse(mRetrofit.getCategories(level, categories.toString()), observer);
    }

    public void getYouLike(BaseObserver<List<HotelResponse>> observer) {
        dealResponse(mRetrofit.getYouLike(), observer);
    }

    public void getBanners(int type, BaseObserver<List<BannerResponse>> observer) {
        dealResponse(mRetrofit.getBanners(type), observer);
    }

}

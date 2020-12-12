package com.jcs.where.home.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;

import java.util.List;

public class HotelListModel extends BaseModel {
    public void getCategories(int level, int[] categories, BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(level, categories), observer);
    }
}
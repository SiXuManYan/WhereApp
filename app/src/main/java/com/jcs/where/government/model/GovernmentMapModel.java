package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;

import java.util.List;

/**
 * create by zyf on 2020/12/28 8:50 PM
 */
public class GovernmentMapModel extends BaseModel {
    /**
     * 获得页面TabLayout展示的二级分类
     */
    public void getCategories(BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(2, new int[]{1}), observer);
    }
}

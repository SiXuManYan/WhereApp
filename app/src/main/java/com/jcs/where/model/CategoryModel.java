package com.jcs.where.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;

import java.util.List;

/**
 * create by zyf on 2020/12/14 8:35 PM
 */
public class CategoryModel extends BaseModel {

    /**
     * 获得分类页面TabLayout展示的一级分类
     */
    public void getCategories(BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(1, new int[]{0}), observer);
    }
}

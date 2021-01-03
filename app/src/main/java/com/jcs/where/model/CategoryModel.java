package com.jcs.where.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.ParentCategoryResponse;

import java.util.List;

/**
 * create by zyf on 2020/12/14 8:35 PM
 */
public class CategoryModel extends BaseModel {

    /**
     * 获得分类页面TabLayout展示的一级分类
     */
    public void getCategories(BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(String.valueOf(0)), observer);
    }

    /**
     * 获得CategoryFragment页面展示的一级二级分类数据
     */
    public void getParentCategory(BaseObserver<List<ParentCategoryResponse>> observer) {
        dealResponse(mRetrofit.getParentCategory(), observer);
    }
}

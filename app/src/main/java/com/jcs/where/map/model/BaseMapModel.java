package com.jcs.where.map.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;

import java.util.List;

/**
 * create by zyf on 2021/1/28 9:14 下午
 */
public class BaseMapModel extends BaseModel {
    /**
     * 获得页面TabLayout展示的二级分类
     */
    public void getCategories(int level,String parentCategoryId,BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(level, parentCategoryId), observer);
    }
}

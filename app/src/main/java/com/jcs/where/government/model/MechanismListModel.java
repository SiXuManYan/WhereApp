package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.utils.Constant;

import java.util.List;

/**
 * create by zyf on 2020/12/28 10:06 PM
 */
public class MechanismListModel extends BaseModel {
    public void getMechanismList(String categoryId, BaseObserver<MechanismPageResponse> observer) {

        dealResponse(mRetrofit.getMechanismListById(categoryId, "", Constant.LAT, Constant.LNG), observer);
    }

    /**
     * 获得页面TabLayout展示的三级分类
     */
    public void getChildCategories(int level, String categoryId, BaseObserver<List<CategoryResponse>> observer) {
        // level 3，表示3级分类
        dealResponse(mRetrofit.getCategories(level, String.valueOf(categoryId)), observer);
    }
}

package com.jcs.where.flash.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * create by zyf on 2021/1/6 10:25 上午
 */
public class FlashModel extends BaseModel {

    public void getYellowPageAllCategories(BaseObserver<List<CategoryResponse>> observer) {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(10);
        categoryIds.add(17);
        categoryIds.add(21);
        categoryIds.add(27);
        categoryIds.add(94);
        categoryIds.add(114);
        categoryIds.add(209);
        categoryIds.add(226);
        // 获取一级分类
        dealResponse(mRetrofit.getAllChildCategories(1, categoryIds.toString()), observer);
    }

}

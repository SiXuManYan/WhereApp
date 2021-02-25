package com.jcs.where.mine.model.merchant_settled;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;

import java.util.List;

/**
 * create by zyf on 2021/2/25 9:03 下午
 */
public class SettledTypeModel extends BaseModel {

    public void getCategories(int level, String categoryId, BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(level, categoryId), observer);
    }

}

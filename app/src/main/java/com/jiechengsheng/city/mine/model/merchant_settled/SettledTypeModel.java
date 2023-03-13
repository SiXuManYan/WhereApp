package com.jiechengsheng.city.mine.model.merchant_settled;

import com.jiechengsheng.city.api.BaseModel;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.response.MerchantTypeResponse;

import java.util.List;

/**
 * create by zyf on 2021/2/25 9:03 下午
 */
public class SettledTypeModel extends BaseModel {

//    public void getCategories(int level, String categoryId, BaseObserver<List<CategoryResponse>> observer) {
//        dealResponse(mRetrofit.getCategories(level, categoryId), observer);
//    }

    public void getMerchantType(String level, int pid, BaseObserver<List<MerchantTypeResponse>> observer) {
        dealResponse(mRetrofit.getMerchantSettledType(level, pid), observer);
    }
}

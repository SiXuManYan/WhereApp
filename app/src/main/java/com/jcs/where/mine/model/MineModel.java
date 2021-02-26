package com.jcs.where.mine.model;

import com.google.gson.JsonObject;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MerchantSettledInfoResponse;
import com.jcs.where.api.response.UserInfoResponse;

/**
 * create by zyf on 2021/1/9 11:32 下午
 */
public class MineModel extends BaseModel {

    public void getUserInfo(BaseObserver<UserInfoResponse> observer) {
        dealResponse(mRetrofit.getUserInfo(), observer);
    }

    public void getUnreadMessageCount(BaseObserver<JsonObject> observer) {
        dealResponse(mRetrofit.getUnreadMessageCount(), observer);
    }

    public void getMerchantSettledInfo(BaseObserver<MerchantSettledInfoResponse> observer) {
        dealResponse(mRetrofit.getMerchantSettledInfo(), observer);
    }
}

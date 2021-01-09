package com.jcs.where.mine.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.UserInfoResponse;

/**
 * create by zyf on 2021/1/9 11:32 下午
 */
public class MineModel extends BaseModel {

    public void getUserInfo(BaseObserver<UserInfoResponse> observer) {
        dealResponse(mRetrofit.getUserInfo(), observer);
    }

}

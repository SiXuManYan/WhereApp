package com.jcs.where.mine.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.request.UpdateUserInfoRequest;
import com.jcs.where.api.response.UserInfoResponse;

/**
 * create by zyf on 2021/1/10 12:11 上午
 */
public class PersonalDataModel extends BaseModel {
    public void getUserInfo(BaseObserver<UserInfoResponse> observer) {
        dealResponse(mRetrofit.getUserInfo(), observer);
    }


    public void updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest, BaseObserver<UserInfoResponse> observer) {
        dealResponse(mRetrofit.patchUpdateUserInfo(updateUserInfoRequest), observer);
    }
}

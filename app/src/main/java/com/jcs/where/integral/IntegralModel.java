package com.jcs.where.integral;

import com.google.gson.JsonObject;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.api.response.UserInfoResponse;

import io.reactivex.annotations.NonNull;

/**
 * Created by Wangsw  2021/1/23 15:10.
 */
public class IntegralModel extends BaseModel {


    private final IntegralView mView;

    public IntegralModel(IntegralView view) {
        this.mView = view;
    }


    /**
     * 获取用户信息(获取积分)
     * @param
     */
    public void getUserInfo() {
        dealResponse(mRetrofit.getUserInfo(), new BaseObserver<UserInfoResponse>(){

            @Override
            protected void onError(ErrorResponse errorResponse) {
                mView.onError(errorResponse);
            }

            @Override
            protected void onSuccess(UserInfoResponse response) {
                mView.bindIntegral(String.valueOf(response.getIntegral()));
            }
        });
    }


    /**
     * 签到列表
     */
    public void getSignInList() {
        dealResponse(mRetrofit.getSignList(), new BaseObserver<SignListResponse>() {

            @Override
            public void onSuccess(@NonNull SignListResponse response) {
                mView.bindDetailData(response);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                mView.onError(errorResponse);
            }
        });
    }


    /**
     * 立即签到
     */
    public void signIn() {

        dealResponse(mRetrofit.signIn(), new BaseObserver<JsonObject>() {

            @Override
            public void onSuccess(@NonNull JsonObject response) {
                mView.signInSuccess();
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                mView.onError(errorResponse);
            }
        });

    }
}

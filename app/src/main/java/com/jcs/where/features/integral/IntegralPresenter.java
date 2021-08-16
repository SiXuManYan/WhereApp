package com.jcs.where.features.integral;

import com.google.gson.JsonElement;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.features.integral.IntegralView;

import io.reactivex.annotations.NonNull;

/**
 * Created by Wangsw  2021/1/23 15:10.
 */
public class IntegralPresenter extends BaseMvpPresenter {

    private final IntegralView mView;

    public IntegralPresenter(IntegralView views) {
        super(views);
        mView = views;
    }

    /**
     * 获取用户信息(获取积分)
     *
     * @param
     */
    public void getUserInfo() {

        requestApi(mRetrofit.getUserInfo(), new BaseMvpObserver<UserInfoResponse>(mView) {
            @Override
            protected void onSuccess(UserInfoResponse response) {
                Integer signStatus = response.getSignStatus();
                mView.bindUserIntegral(String.valueOf(response.getIntegral()), signStatus == 1);
            }
        });
    }


    /**
     * 签到列表
     */
    public void getSignInList() {
        requestApi(mRetrofit.getSignList(), new BaseMvpObserver<SignListResponse>(mView) {

            @Override
            public void onSuccess(@NonNull SignListResponse response) {
                mView.bindSignInList(response);
            }

        });
    }


    /**
     * 立即签到
     */
    public void signIn() {

        requestApi(mRetrofit.signIn(), new BaseMvpObserver<JsonElement>(mView) {

            @Override
            public void onSuccess(@NonNull JsonElement response) {
                mView.signInSuccess();
            }
        });

    }
}

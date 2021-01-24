package com.jcs.where.integral;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.SignListResponse;

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
                mView.onDetailError(errorResponse);
            }
        });
    }


}

package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.SuccessResponse;

import retrofit2.Response;

/**
 * create by zyf on 2021/1/2 11:41 AM
 */
public class MechanismDetailModel extends BaseModel {

    public void getMechanismDetailById(int mechanismId, BaseObserver<MechanismDetailResponse> observer) {
        dealResponse(mRetrofit.getMechanismDetailById(mechanismId), observer);
    }

    public void postCollectMechanism(int mechanismId, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.postCollectMechanism(mechanismId), observer);
    }

    public void delCollectMechanism(int mechanismId, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.delCollectMechanism(mechanismId), observer);
    }
}

package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MechanismDetailResponse;

/**
 * create by zyf on 2021/1/2 11:41 AM
 */
public class MechanismDetailModel extends BaseModel {

    public void getMechanismDetailById(int mechanismId, BaseObserver<MechanismDetailResponse> observer) {
        dealResponse(mRetrofit.getMechanismDetailById(mechanismId), observer);
    }

}

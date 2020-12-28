package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MechanismPageResponse;

/**
 * create by zyf on 2020/12/28 10:06 PM
 */
public class MechanismListModel extends BaseModel {
    public void getMechanismList(int category, BaseObserver<MechanismPageResponse> observer) {
        dealResponse(mRetrofit.getMechanismListById(category, ""), observer);
    }
}

package com.jcs.where.mine.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.FootprintResponse;
import com.jcs.where.api.response.PageResponse;

import io.reactivex.Observable;

/**
 * create by zyf on 2021/2/3 11:09 下午
 */
public class FootprintModel extends BaseModel {
    public void getFootprintList(BaseObserver<PageResponse<FootprintResponse>> observer) {
        dealResponse(mRetrofit.getFootprintList(), observer);
    }
}

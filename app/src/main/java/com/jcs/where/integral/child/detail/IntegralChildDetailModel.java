package com.jcs.where.integral.child.detail;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.IntegralDetailResponse;
import com.jcs.where.api.response.PageResponse;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by Wangsw  2021/1/23 15:10.
 * 积分明细
 */
public class IntegralChildDetailModel extends BaseModel {


    private final IntegralChildDetailView mView;

    public IntegralChildDetailModel(IntegralChildDetailView view) {
        this.mView = view;
    }


    /**
     * 积分明细列表
     */
    public void getIntegralDetailList() {

    }




}

package com.jcs.where.features.integral.child.detail;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.IntegralDetailResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.features.integral.child.detail.IntegralChildDetailView;

import java.util.List;

/**
 * Created by Wangsw  2021/1/23 15:10.
 * 积分明细
 */
public class IntegralChildDetailPresenter extends BaseMvpPresenter {


    private final IntegralChildDetailView mView;

    public IntegralChildDetailPresenter(IntegralChildDetailView views) {
        super(views);
        mView = views;
    }


    /**
     * 积分明细列表
     */
    public void getIntegralDetailList(int page) {

        requestApi(mRetrofit.getIntegralsDetailList(page), new BaseMvpObserver<PageResponse<IntegralDetailResponse>>(mView) {
            @Override
            protected void onSuccess(PageResponse<IntegralDetailResponse> response) {
                boolean isLastPage = response.getLastPage() == page;
                List<IntegralDetailResponse> data = response.getData();
                mView.bindDetailData(data, isLastPage);
            }

        });
    }


}
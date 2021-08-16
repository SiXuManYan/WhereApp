package com.jcs.where.features.integral.child.task;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.features.integral.child.task.IntegralChildTaskView;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import java.util.List;

/**
 * Created by Wangsw  2021/3/2 11:19.
 */
public class IntegralChildTaskPresenter extends BaseMvpPresenter {

    private IntegralChildTaskView mView;

    public IntegralChildTaskPresenter(IntegralChildTaskView view) {
        super(view);
        mView = view;
    }

    public void getRecommendList(int page) {

        String areaId = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
        String lat = Constant.LAT + "";
        String lng = Constant.LNG + "";

        requestApi(mRetrofit.getRecommends(page, lat, lng, areaId), new BaseMvpObserver<PageResponse<HomeRecommendResponse>>(mView) {
            @Override
            protected void onSuccess(PageResponse<HomeRecommendResponse> response) {
                boolean isLastPage = response.getLastPage() == page;
                List<HomeRecommendResponse> data = response.getData();
                mView.bindDetailData(data, isLastPage);
            }
        });
    }
}

package com.jcs.where.home.fragment;

import com.jcs.where.BuildConfig;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.api.response.version.VersionResponse;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import java.util.List;

/**
 * Created by Wangsw  2021/3/4 16:55.
 * @deprecated
 */
public class HomePresenter extends BaseMvpPresenter {

    private HomeView mView;

    public HomePresenter(HomeView baseMvpView) {
        super(baseMvpView);
        this.mView = baseMvpView;
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

    public void checkAppVersion() {
        requestApi(mRetrofit.checkAppVersion(BuildConfig.VERSION_NAME, "Android"), new BaseMvpObserver<VersionResponse>(mView) {
            @Override
            protected void onSuccess(VersionResponse response) {

                if (!response.status) {
                    return;
                }

                mView.checkAppVersion(response);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {

            }
        });



    }
}

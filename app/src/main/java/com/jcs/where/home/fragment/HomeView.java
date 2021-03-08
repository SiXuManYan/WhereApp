package com.jcs.where.home.fragment;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.api.response.version.VersionResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/4 16:56.
 */
public interface HomeView extends BaseMvpView {
    void bindDetailData(List<HomeRecommendResponse> data, boolean isLastPage);

    /**
     * app 版本更新
     */
    void checkAppVersion(VersionResponse response);
}

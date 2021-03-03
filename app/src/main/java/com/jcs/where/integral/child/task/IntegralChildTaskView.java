package com.jcs.where.integral.child.task;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/2 11:18.
 */
public interface IntegralChildTaskView extends BaseMvpView {
    void bindDetailData(List<HomeRecommendResponse> data, boolean isLastPage);
}

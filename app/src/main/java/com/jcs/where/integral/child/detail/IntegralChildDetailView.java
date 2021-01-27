package com.jcs.where.integral.child.detail;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.IntegralDetailResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/1/23 15:09.
 */
public interface IntegralChildDetailView extends BaseMvpView {


    void bindDetailData(List<IntegralDetailResponse> data,boolean isLast);
}

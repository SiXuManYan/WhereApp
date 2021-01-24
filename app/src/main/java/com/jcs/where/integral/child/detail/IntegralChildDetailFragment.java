package com.jcs.where.integral.child.detail;


import android.view.View;

import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.IntegralDetailResponse;
import com.jcs.where.base.BaseFragment;

import java.util.List;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分明细
 */
public class IntegralChildDetailFragment extends BaseFragment implements IntegralChildDetailView{


    public static IntegralChildDetailFragment newInstance() {
        return new IntegralChildDetailFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integral_child;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }


    @Override
    public void onDetailError(ErrorResponse errorResponse) {

    }

    @Override
    public void bindDetailData(List<IntegralDetailResponse> data) {

    }
}

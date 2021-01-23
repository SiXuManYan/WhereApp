package com.jcs.where.integral.child.task;


import android.view.View;

import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.base.BaseFragment;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分任务
 */
public class IntegralChildTaskFragment extends BaseFragment implements IntegralChildTaskView{


    public static IntegralChildTaskFragment newInstance() {
        return new IntegralChildTaskFragment();
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
    protected void loadOnVisible() {

    }

    @Override
    protected void bindListener() {

    }


    @Override
    public void onDetailError(ErrorResponse errorResponse) {

    }
}

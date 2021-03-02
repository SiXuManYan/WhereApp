package com.jcs.where.integral.child.task;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/3/2 11:19.
 */
public class IntegralChildTaskPresenter extends BaseMvpPresenter {

    private IntegralChildTaskView mView;

    public IntegralChildTaskPresenter(IntegralChildTaskView view) {
        super(view);
        mView = view;
    }
}

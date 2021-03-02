package com.jcs.where.integral.child.task;


import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分任务
 */
public class IntegralChildTaskFragment extends BaseMvpFragment<IntegralChildTaskPresenter> implements IntegralChildTaskView {


    private TextView mSignInTv;

    public static IntegralChildTaskFragment newInstance() {
        return new IntegralChildTaskFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integral_child_task;
    }

    @Override
    protected void initView(View view) {
        mSignInTv = view.findViewById(R.id.sign_in_tv);

    }

    @Override
    protected void initData() {
        presenter = new IntegralChildTaskPresenter(this);
    }

    @Override
    protected void loadOnVisible() {

    }

    @Override
    protected void bindListener() {

    }

    public void signInClick(View view) {

        if (mSignInTv.isEnabled()) {
            EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_IN_REQUEST));
        }
    }


    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        super.onEventReceived(baseEvent);
        if (baseEvent.code == EventCode.EVENT_SIGN_IN_CHANGE_STATUS) {
            Boolean data = (Boolean) baseEvent.data;
            changeSignStatus(data);
        }
    }

    private void changeSignStatus(boolean isSigned) {
        if (mSignInTv.getVisibility() != View.VISIBLE) {
            mSignInTv.setVisibility(View.VISIBLE);
        }
        mSignInTv.setEnabled(!isSigned);
        if (isSigned) {
            mSignInTv.setText(R.string.already_sign_in);
        } else {
            mSignInTv.setText(R.string.sign_in_now);
        }
    }


}

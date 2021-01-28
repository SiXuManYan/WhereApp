package com.jcs.where.integral.child.task;


import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.EventCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分任务
 */
public class IntegralChildTaskFragment extends BaseFragment {


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
        EventBus.getDefault().register(this);
        mSignInTv = view.findViewById(R.id.sign_in_tv);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    public void signInClick(View view) {

        if (mSignInTv.isEnabled()) {
            EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_IN_REQUEST));
        }
    }


    /**
     * 更新签到状态
     *
     * @param baseEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<Boolean> baseEvent) {
        if (baseEvent.code == EventCode.EVENT_SIGN_IN_CHANGE_STATUS) {
            changeSignStatus(baseEvent.data);
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

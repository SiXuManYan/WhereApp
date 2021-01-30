package com.jcs.where.features.setting;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/1/28 15:13.
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        findViewById(R.id.sign_out_tv).setOnClickListener(this::onSignOutClick);
    }

    private void onSignOutClick(View view) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.prompt)
                .setMessage(R.string.sign_out_hint)
                .setCancelable(false)
                .setPositiveButton(R.string.ensure, (dialogInterface, i) -> {
                    EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_OUT));
                    CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, "");
                    startActivityClearTop(LoginActivity.class, null);
                    finish();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .create().show();


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

    }


}

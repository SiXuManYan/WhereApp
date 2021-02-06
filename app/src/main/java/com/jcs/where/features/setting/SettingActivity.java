package com.jcs.where.features.setting;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.jcs.where.BaseApplication;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.features.setting.information.ModifyInfoActivity;
import com.jcs.where.features.setting.password.ModifyPasswordActivity;
import com.jcs.where.features.setting.phone.ModifyPhoneActivity;
import com.jcs.where.storage.entity.User;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.SPKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by Wangsw  2021/1/28 15:13.
 * 设置
 */
public class SettingActivity extends BaseActivity {

    private TextView phone_tv, clear_cache_tv;
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        phone_tv = findViewById(R.id.phone_tv);
        clear_cache_tv = findViewById(R.id.clear_cache_tv);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        setUserInfo();
        setCache();
    }

    private void setUserInfo() {
        User user = User.getInstance();
        phone_tv.setText(FeaturesUtil.getFormatPhoneNumber(user.phone));
    }

    private void setCache() {
        File cacheDir = Glide.getPhotoCacheDir(this);
        if (cacheDir != null) {
            File parentFile = cacheDir.getParentFile();
            long size = GlideUtil.getDirSize(parentFile);
            String sizeText = GlideUtil.byteConversionGBMBKB(size);
            clear_cache_tv.setText(getString(R.string.cache_cleaning_format, sizeText));
        }
    }

    @Override
    protected void bindListener() {
        findViewById(R.id.sign_out_tv).setOnClickListener(this::onSignOutClick);
        findViewById(R.id.notification_rl).setOnClickListener(v -> {
            AppUtils.launchAppDetailsSettings();
        });
        clear_cache_tv.setOnClickListener(v -> {
            GlideUtil.clearImageDiskCache(this);
            GlideUtil.clearImageMemoryCache(this);
            ToastUtils.showShort(R.string.clear_success);
            clear_cache_tv.setText(R.string.cache_cleaning);
        });
        findViewById(R.id.information_rl).setOnClickListener(v -> {
            startActivity(ModifyInfoActivity.class);
        });
        findViewById(R.id.password_rl).setOnClickListener(v -> {
            startActivity(ModifyPasswordActivity.class);
        });
        findViewById(R.id.phone_number_rl).setOnClickListener(v -> {
            startActivity(ModifyPhoneActivity.class);
        });
        findViewById(R.id.privacy_policy_rl).setOnClickListener(v -> {
            WebViewActivity.goTo(this, FeaturesUtil.getPrivacyPolicy());
        });

    }

    private void onSignOutClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.prompt)
                .setMessage(R.string.sign_out_hint)
                .setCancelable(false)
                .setPositiveButton(R.string.ensure, (dialogInterface, i) -> {
                    User.clearAllUser();
                    CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, "");
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.PARAM_DISMISS_BACK_ICON, true);

                    EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_OUT));
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

        int code = baseEvent.code;
        switch (code) {
            case EventCode.EVENT_REFRESH_USER_INFO:
                setUserInfo();
                break;
            default:
                break;
        }
    }


}

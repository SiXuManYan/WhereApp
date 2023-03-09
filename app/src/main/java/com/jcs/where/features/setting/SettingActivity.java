package com.jcs.where.features.setting;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.features.refund.method.RefundMethodActivity;
import com.jcs.where.features.setting.information.ModifyInfoActivity;
import com.jcs.where.features.setting.password.ModifyPasswordActivity;
import com.jcs.where.features.setting.phone.ModifyPhoneActivity;
import com.jcs.where.features.web.WebViewActivity;
import com.jcs.where.mine.about.AboutActivity;
import com.jcs.where.storage.entity.User;
import com.jcs.where.utils.BusinessUtils;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Wangsw  2021/1/28 15:13.
 * 设置
 */
public class SettingActivity extends BaseActivity {

    private TextView phone_tv, clear_cache_tv;
    private int sequence;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        EventBus.getDefault().register(this);
        phone_tv = findViewById(R.id.phone_tv);
        clear_cache_tv = findViewById(R.id.clear_cache_tv);
        TextView version_name_tv = findViewById(R.id.version_name_tv);
        version_name_tv.setText(BuildConfig.VERSION_NAME);
        sequence = SPUtils.getInstance().getInt(Constant.SP_PUSH_SEQUENCE, 0);
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
            WebViewActivity.Companion.navigation(this, FeaturesUtil.getPrivacyPolicy(), false);
        });
        findViewById(R.id.about_rl).setOnClickListener(v -> {
            startActivity(AboutActivity.class);
        });
        findViewById(R.id.refund_rl).setOnClickListener(v -> {
            startActivityAfterLogin(RefundMethodActivity.class);
        });

    }

    private void onSignOutClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.prompt)
                .setMessage(R.string.sign_out_hint)
                .setCancelable(false)
                .setPositiveButton(R.string.ensure, (dialogInterface, i) -> {
                    // 登出友盟
//                    MobclickAgent.onProfileSignOff();

                    // 断开融云连接
                    BusinessUtils.INSTANCE.loginOut();

                    // 删除极光推送别名
                    JPushInterface.deleteAlias(this, sequence);

                    startActivity(LoginActivity.class);
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

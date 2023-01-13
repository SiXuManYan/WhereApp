package com.jcs.where.features.upgrade;

import android.Manifest;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.jcs.where.R;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.PermissionUtils;

/**
 * Created by Wangsw  2021/3/5
 * 版本更新
 */
public class UpgradeActivity extends BaseMvpActivity<UpgradePresenter> implements BaseMvpView {


    private String newVersionCode;
    private String downloadUrl;
    private String updateDesc;
    private boolean isForceInstall;

    private boolean launchSetting = false;
    private boolean isDownload = false;


    private AlertDialog permissionDialog;
    private DownUtil downUtil;

    private ImageView ic_cancel;
    private TextView title_tv;
    private TextView message_tv;
    private TextView upgrade_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upgrade;
    }

    @Override
    protected void initView() {
        ic_cancel = findViewById(R.id.ic_cancel);
        title_tv = findViewById(R.id.title_tv);
        message_tv = findViewById(R.id.message_tv);
        upgrade_tv = findViewById(R.id.upgrade_tv);
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        newVersionCode = intent.getStringExtra(Constant.PARAM_NEW_VERSION_CODE);
        downloadUrl = intent.getStringExtra(Constant.PARAM_DOWNLOAD_URL);
        updateDesc = intent.getStringExtra(Constant.PARAM_UPDATE_DESC);
        isForceInstall = intent.getBooleanExtra(Constant.PARAM_IS_FORCE_INSTALL, false);
        downUtil = new DownUtil(this);
        setData();
    }

    private void setData() {
        String title = getString(R.string.new_version_found_format, newVersionCode);
        title_tv.setText(title);
        message_tv.setText(updateDesc);

        if (isForceInstall) {
            ic_cancel.setVisibility(View.GONE);
        } else {
            ic_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void bindListener() {
        upgrade_tv.setOnClickListener(this::onUpgradeClick);
        ic_cancel.setOnClickListener(v -> finish());
    }

    private void onUpgradeClick(View view) {
//        handlePermission();
        FeaturesUtil.gotoGooglePlay(this);
    }

    @Override
    public void onBackPressed() {
        if (!isForceInstall) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (launchSetting) {
            launchSetting = false;
            handlePermission();
        }
    }

    private void handlePermission() {

        PermissionUtils.permissionAny(this, granted -> {
                    if (granted) {
                        downUtil.startDownload(downloadUrl, getString(R.string.app_name), R.mipmap.ic_app_logo);
                        isDownload = true;
                    } else {
                        doUninhibited();
                    }

                }, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void doUninhibited() {
        String hint01 = getString(R.string.new_version_hint_01);
        String hint02 = getString(R.string.new_version_hint_02);
        String hint03 = getString(R.string.new_version_hint_03);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(hint01 + hint02 + hint03);
        ForegroundColorSpan span = new ForegroundColorSpan(ColorUtils.getColor(R.color.red_ACD8FF));
        stringBuilder.setSpan(span, hint01.length(), (hint01 + hint02).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        permissionDialog = new AlertDialog.Builder(this)
                .setMessage(stringBuilder)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    launchSetting = true;
                    AppUtils.launchAppDetailsSettings();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                }).create();

        permissionDialog.show();

    }

    private void showUpgradeDialog() {

/*        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.new_version_found_format, newVersionCode))
                .setCancelable(false)
                .setMessage(updateDesc)
                .setPositiveButton(R.string.upgrade, (dialog, which) -> {

                    if (!isDownload) {
                        handlePermission();
                    }
                });

        if (!isForceInstall) {
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
        }
        upgradeDialog = builder.create();
        upgradeDialog.show();*/

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (permissionDialog != null) {
            permissionDialog.dismiss();
        }

    }
}

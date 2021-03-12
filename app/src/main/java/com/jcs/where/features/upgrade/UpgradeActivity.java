package com.jcs.where.features.upgrade;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.jcs.where.R;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.Constant;
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
    private AlertDialog upgradeDialog;
    private DownUtil downUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upgrade;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        newVersionCode = intent.getStringExtra(Constant.PARAM_NEW_VERSION_CODE);
        downloadUrl = intent.getStringExtra(Constant.PARAM_DOWNLOAD_URL);
        updateDesc = intent.getStringExtra(Constant.PARAM_UPDATE_DESC);
        isForceInstall = intent.getBooleanExtra(Constant.PARAM_IS_FORCE_INSTALL, false);
        downUtil = new DownUtil(this);
    }

    @Override
    protected void bindListener() {

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
                        showUpgradeDialog();
                    } else {
                        if (isForceInstall) {
                            //  强制升级未授权，退出app
                            AppUtils.exitApp();
                        } else {
                            // 非强制升级,引导
                            doUninhibited();
                        }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.new_version_found) + newVersionCode)
                .setCancelable(false)
                .setMessage(updateDesc)
                .setPositiveButton(R.string.upgrade, (dialog, which) -> {
                    downUtil.startDownload(downloadUrl, getString(R.string.app_name), R.mipmap.ic_launcher);
                    isDownload = true;
                });

        if (!isForceInstall) {
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
        }
        upgradeDialog = builder.create();
        upgradeDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (permissionDialog != null) {
            permissionDialog.dismiss();
        }
        if (upgradeDialog != null) {
            upgradeDialog.dismiss();
        }
    }
}

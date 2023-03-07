package com.jcs.where.features.upgrade;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.jcs.where.R;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;

/**
 * Created by Wangsw  2021/3/5
 * 版本更新
 */
public class UpgradeActivity extends BaseMvpActivity<UpgradePresenter> implements BaseMvpView {


    private String newVersionCode;
    private String downloadUrl;
    private String updateDesc;
    private boolean isForceInstall;




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
        FeaturesUtil.gotoGooglePlay(this);
    }

    @Override
    public void onBackPressed() {
        if (!isForceInstall) {
            super.onBackPressed();
        }
    }


}

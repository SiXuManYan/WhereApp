package com.jcs.where.government.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseDialog;

/**
 * 安装 App 提示
 * create by zyf on 2021/1/2 7:57 PM
 */
public class ToInstallAppDialog extends BaseDialog {
    private TextView mInstallPrompt;
    private Button mCancelBtn, mEnsureBtn;

    private String mInstallName = "地图";

    @Override
    protected int getLayout() {
        return R.layout.dialog_to_install_app;
    }

    @Override
    protected int getHeight() {
        return 200;
    }

    @Override
    protected int getWidth() {
        return 200;
    }

    @Override
    protected void initView(View view) {
        mCancelBtn = view.findViewById(R.id.cancelBtn);
        mEnsureBtn = view.findViewById(R.id.ensureBtn);

        mInstallPrompt = view.findViewById(R.id.installPrompt);
        mInstallPrompt.setText("是否去安装 " + mInstallPrompt + " ?");
    }

    @Override
    protected void initData() {

    }

    public void setInstallName(String installName) {
        this.mInstallName = installName;
    }

    @Override
    protected void bindListener() {
        mCancelBtn.setOnClickListener(v -> dismiss());
        mEnsureBtn.setOnClickListener(this::onEnsureClicked);
    }

    public void onEnsureClicked(View view) {
        Context context = getContext();
        if (context != null) {
            Uri uri = Uri
                    .parse("market://details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }
}

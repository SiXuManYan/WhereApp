package com.jcs.where.government.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseBottomDialog;

/**
 * 导航
 * create by zyf on 2021/1/2 7:44 PM
 */
public class ToNavigationDialog extends BaseBottomDialog {

    private Button mCancelBtn;
    private TextView mNavigationTv;
    private ToInstallAppDialog mInstallDialog;

    private final String GOOGLE_MAP_PACKAGE_NAME = "com.google.android.apps.maps";
    private Double mLatitude;
    private Double mLongitude;
    private String mNavigationName = "Google 地图";

    @Override
    protected int getLayout() {
        return R.layout.dialog_to_navigation;
    }

    @Override
    protected int getHeight() {
        return 200;
    }

    @Override
    protected void initView(View view) {

        mCancelBtn = view.findViewById(R.id.cancelBtn);
        mNavigationTv = view.findViewById(R.id.navigationTv);

        mNavigationTv.setText(mNavigationName);

    }

    @Override
    protected void initData() {
        mInstallDialog = new ToInstallAppDialog();
        mInstallDialog.setInstallName("地图");
    }

    @Override
    protected void bindListener() {
        mCancelBtn.setOnClickListener(v -> dismiss());
        mNavigationTv.setOnClickListener(this::onNavigationClicked);
    }

    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public void setNavigationName(String navigationName) {
        this.mNavigationName = navigationName;
    }

    public void onNavigationClicked(View view) {
        Context context = getContext();
        if (context != null) {
            if (hasPackage(GOOGLE_MAP_PACKAGE_NAME)) {
                toMap(context);
            } else {
                showToast(getString(R.string.google_map_not_installed));
                mInstallDialog.show(getFragmentManager());
            }
        }
    }

    private void toMap(Context context) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="
                + mLatitude + "," + mLongitude
                + ", + Sydney +Australia");
        Intent toMap = new Intent(Intent.ACTION_VIEW,
                gmmIntentUri);
        toMap.setPackage("com.google.android.apps.maps");
        context.startActivity(toMap);
        dismiss();
    }

    /*
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    private boolean hasPackage(String packageName) {
        Context context = getContext();
        if (null == packageName) {
            return false;
        }

        boolean has = true;
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
        } catch (PackageManager.NameNotFoundException e) {
            // 抛出找不到的异常，说明该程序已经被卸载
            has = false;
        }
        return has;
    }


}

package com.jcs.where.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcs.where.R;

import java.util.ArrayList;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView icon1, icon2, icon3;
    private TextView text1, text2, text3;
    private LinearLayout tab1, tab2, tab3;
    FragmentManager fm;
    private ArrayList<Fragment> frlist = new ArrayList<Fragment>();
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE};
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        fullScreen(this);
        fm = getSupportFragmentManager();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        initView();
    }

    private void initView() {
        icon1 = V.f(this, R.id.icon1);
        icon2 = V.f(this, R.id.icon2);
        icon3 = V.f(this, R.id.icon3);
        text1 = V.f(this, R.id.text1);
        text2 = V.f(this, R.id.text2);
        text3 = V.f(this, R.id.text3);
        tab1 = V.f(this, R.id.tab1);
        tab1.setOnClickListener(this);
        tab2 = V.f(this, R.id.tab2);
        tab2.setOnClickListener(this);
        tab3 = V.f(this, R.id.tab3);
        tab3.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();

        frlist.add(fm.findFragmentById(R.id.home_fragment));
        frlist.add(fm.findFragmentById(R.id.order_fagment));
        frlist.add(fm.findFragmentById(R.id.mine_fragment));
        setTab(0);
    }

    private void setTab(int index) {
        FragmentTransaction trans = fm.beginTransaction();
        for (int i = 0; i < frlist.size(); i++) {
            if (i == index) {
                trans.show(frlist.get(i));
            } else {
                trans.hide(frlist.get(i));
            }
        }
        trans.commitAllowingStateLoss();
        switch (index) {
            case 0: {
                icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_press));
                icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_order_normal));
                icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mine_normal));
                text1.setSelected(true);
                text2.setSelected(false);
                text3.setSelected(false);
                break;
            }
            case 1: {
                icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_normal));
                icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_order_press));
                icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mine_normal));
                text1.setSelected(false);
                text2.setSelected(true);
                text3.setSelected(false);
                break;
            }
            case 2: {
                icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_normal));
                icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_order_normal));
                icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mine_press));
                text1.setSelected(false);
                text2.setSelected(false);
                text3.setSelected(true);
                break;
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                setTab(0);
                break;
            case R.id.tab2:
                setTab(1);
                break;
            case R.id.tab3:
                setTab(2);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}

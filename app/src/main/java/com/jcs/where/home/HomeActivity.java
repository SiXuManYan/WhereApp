package com.jcs.where.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.category.CategoryFragment;
import com.jcs.where.home.fragment.HomeFragment;
import com.jcs.where.home.fragment.MineFragment;
import com.jcs.where.home.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;

public class HomeActivity extends BaseActivity {
    private ImageView icon1, icon2, icon3;
    private TextView text1, text2, text3;
    private LinearLayout tab1, tab2, tab3;
    private List<HomeTabBean> mTabBeans;
    FragmentManager fm;
    private ArrayList<Fragment> frList = new ArrayList<Fragment>();
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
    private TabLayout mTabLayout;

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
        initData();
        bindListener();
    }

    private void bindListener() {

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = frList.get(position);
                if (!fragment.isAdded()) {
                    transaction.add(R.id.homeFrame, fragment);
                }else {
                    transaction.show(fragment);
                }

                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = frList.get(position);
                transaction.hide(fragment).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initData() {

    }

    private View makeTabView(HomeTabBean homeTabBean) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_home_activity, null);
        ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabIcon.setImageResource(homeTabBean.iconRes);
        tabTitle.setText(homeTabBean.name);
        return tabView;
    }

    private void initView() {
        initFragment();
        mTabLayout = findViewById(R.id.homeTabs);

        initTabLayout();

    }

    private void initFragment() {
        frList.add(new HomeFragment());
        frList.add(new CategoryFragment());
        frList.add(new OrderFragment());
        frList.add(new MineFragment());

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.homeFrame, frList.get(0)).commit();
    }

    private void initTabLayout() {
        mTabBeans = new ArrayList<>();
        mTabBeans.add(new HomeTabBean("首页", R.drawable.selector_tab_home_home));
        mTabBeans.add(new HomeTabBean("分类", R.drawable.selector_tab_home_category));
        mTabBeans.add(new HomeTabBean("订单", R.drawable.selector_tab_home_order));
        mTabBeans.add(new HomeTabBean("我的", R.drawable.selector_tab_home_mine));

        int size = mTabBeans.size();
        for (int i = 0; i < size; i++) {
            HomeTabBean homeTabBean = mTabBeans.get(i);
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setCustomView(makeTabView(homeTabBean));
            mTabLayout.addTab(tab);
        }

        mTabLayout.selectTab(mTabLayout.getTabAt(0));
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

    static class HomeTabBean {
        String name;
        int iconRes;

        public HomeTabBean(String name, int iconRes) {
            this.name = name;
            this.iconRes = iconRes;
        }
    }
}

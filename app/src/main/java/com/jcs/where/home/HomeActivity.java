package com.jcs.where.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.category.CategoryFragment2;
import com.jcs.where.features.home.HomeFragment2;
import com.jcs.where.home.fragment.OrderFragment;
import com.jcs.where.mine.fragment.MineFragment;
import com.jcs.where.utils.PermissionUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseMvpActivity<MainPresenter> implements MainView {

    private static final int REQUEST_PERMISSION_CODE = 1;
    FragmentManager fm;
    private List<HomeTabBean> mTabBeans;
    private final ArrayList<Fragment> frList = new ArrayList<>();
    private TabLayout mTabLayout;
    private Long mTapTime = 0L;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationRequest locationRequest;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        fm = getSupportFragmentManager();
        initFragment();
        mTabLayout = findViewById(R.id.homeTabs);
        initTabLayout();
        initLocation();
    }

    private void initLocation() {
//
//        PermissionUtils.permissionAny(this,);
//
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(1000 * 30);
//        locationRequest.setFastestInterval(1000*10);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void bindListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = frList.get(position);
                if (!fragment.isAdded()) {
                    transaction.add(R.id.homeFrame, fragment);
                } else {
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


    private View makeTabView(HomeTabBean homeTabBean) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_home_activity, null);
        ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabIcon.setImageResource(homeTabBean.iconRes);
        tabTitle.setText(homeTabBean.name);
        return tabView;
    }


    private void initFragment() {
        frList.add(new HomeFragment2());
        frList.add(new CategoryFragment2());
        frList.add(new OrderFragment());
        frList.add(new MineFragment());

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.homeFrame, frList.get(0)).commit();
    }

    private void initTabLayout() {
        mTabBeans = new ArrayList<>();
        mTabBeans.add(new HomeTabBean(getString(R.string.main_tab_title_home), R.drawable.selector_tab_home_home));
        mTabBeans.add(new HomeTabBean(getString(R.string.main_tab_title_category), R.drawable.selector_tab_home_category));
        mTabBeans.add(new HomeTabBean(getString(R.string.main_tab_title_order), R.drawable.selector_tab_home_order));
        mTabBeans.add(new HomeTabBean(getString(R.string.main_tab_title_mine), R.drawable.selector_tab_home_mine));

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


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mTapTime > 2000) {
            ToastUtils.showShort(getString(R.string.main_back_hint));
            mTapTime = System.currentTimeMillis();
        } else {
            // 保留应用状态
            moveTaskToBack(false);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {
        int code = baseEvent.code;
        if (code == EventCode.EVENT_SIGN_OUT) {
            finish();
        }
    }

}

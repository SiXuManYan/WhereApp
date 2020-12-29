package com.jcs.where.government.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.government.model.GovernmentMapModel;
import com.jcs.where.view.popup.PopupConstraintLayout;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.annotations.NonNull;

/**
 * 政府机构地图页
 * 政府机构列表是本页的fragment
 * create by zyf on 2020/12/28 7:44 PM
 */
public class GovernmentMapActivity extends BaseActivity implements OnMapReadyCallback {
    private SupportMapFragment mMapFragment;

    private PopupConstraintLayout mPopupLayout;
    private ImageView mTopArrowIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MechanismAdapter mViewPagerAdapter;
    private List<MechanismListFragment> mMechanismListFragments;

    private GovernmentMapModel mModel;
    private List<CategoryResponse> mTabCategories;
    private final int TYPE_GOVERNMENT = 3;

    @Override
    protected void initView() {
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this::onMapAsync);
        mTopArrowIv = findViewById(R.id.topArrowIv);
        mTabLayout = findViewById(R.id.governmentTabs);
        mViewPager = findViewById(R.id.governmentViewPager);
        mPopupLayout = findViewById(R.id.bottomPopupLayout);
        // 绑定PopupLayout适配器
        bindPopupLayoutAdapter();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("GovernmentMapActivity", "onMapReady: " + "");
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void onMapAsync(GoogleMap googleMap) {
        Log.e("GovernmentMapActivity", "onMapAsync: " + "");
    }

    private void bindPopupLayoutAdapter() {
        mPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {
            @Override
            public void onShowCompleted() {
                if (mMechanismListFragments != null && mMechanismListFragments.size() > 0) {
                    MechanismListFragment fistFragment = mMechanismListFragments.get(0);
                    fistFragment.getNetData();
                }
            }

            @Override
            public long getDuration() {
                return 300;
            }

            @Override
            public boolean isGoneAfterBottom() {
                return false;
            }

            @Override
            public int getMinHeight() {
                return getPxFromDp(55);
            }

            @Override
            public int getMaxHeight() {
                return getPxFromDp(583);
            }
        });
    }

    @Override
    protected void initData() {
        mModel = new GovernmentMapModel();
        mTabCategories = new ArrayList<>();
        mMechanismListFragments = new ArrayList<>();
        mViewPagerAdapter = new MechanismAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mModel.getCategories(new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                mTabCategories.clear();
                mMechanismListFragments.clear();

                // 添加全部对应的Tab
                CategoryResponse allCategory = new CategoryResponse();
                allCategory.setName(getString(R.string.all));
                allCategory.setId(0);
                allCategory.setType(TYPE_GOVERNMENT);
                mTabCategories.add(allCategory);

                // 0 表示要获得全部的信息，添加全部对应的ListFragment
                mMechanismListFragments.add(MechanismListFragment.newInstance(allCategory, true));
                mTabCategories.addAll(categoryResponses);
                int size = mTabCategories.size();
                mTabLayout.removeAllTabs();
                for (int i = 0; i < size; i++) {
                    TabLayout.Tab tab = mTabLayout.newTab();
                    CategoryResponse categoryResponse = mTabCategories.get(i);
                    tab.setCustomView(makeTabView(categoryResponse.getName()));
                    mTabLayout.addTab(tab);

                    // 0 位置对应的是全部，已经在循环外添加过了
                    if (i != 0) {
                        mMechanismListFragments.add(MechanismListFragment.newInstance(categoryResponse));
                    }
                }

                mViewPager.setAdapter(mViewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setOffscreenPageLimit(mTabCategories.size());
            }
        });
    }

    private View makeTabView(String title) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_normal_only_text, null);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabTitle.setText(title);
        return tabView;
    }

    @Override
    protected void bindListener() {
        mTopArrowIv.setOnClickListener(this::onTopArrowClick);
    }

    private void onTopArrowClick(View view) {
        mPopupLayout.showOrHide();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_map;
    }

    class MechanismAdapter extends FragmentStatePagerAdapter {

        public MechanismAdapter(@androidx.annotation.NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabCategories.get(position).getName();
        }

        @androidx.annotation.NonNull
        @Override
        public Fragment getItem(int position) {
            return mMechanismListFragments.get(position);
        }

        @Override
        public int getCount() {
            return mMechanismListFragments.size();
        }
    }
}

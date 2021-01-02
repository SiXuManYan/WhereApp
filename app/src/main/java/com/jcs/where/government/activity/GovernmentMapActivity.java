package com.jcs.where.government.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.fragment.CardViewPagerFragment;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.government.model.GovernmentMapModel;
import com.jcs.where.utils.MapMarkerUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
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
    private CardViewPagerFragment mViewPagerFragment;

    private PopupConstraintLayout mPopupLayout;
    private ImageView mTopArrowIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MechanismAdapter mViewPagerAdapter;
    /**
     * 展示机构列表的Fragment集合
     */
    private List<MechanismListFragment> mMechanismListFragments;
    /**
     * 机构列表分类数据，展示在TabLayout上
     */
    private List<CategoryResponse> mTabCategories;
    private GovernmentMapModel mModel;
    private MapMarkerUtil mMapMarkerUtil;
    private GoogleMap mGoogleMap;

    private final int TYPE_GOVERNMENT = 3;
    private int mAreaId = -1;

    @Override
    protected void initView() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mMapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.map);
        mViewPagerFragment = (CardViewPagerFragment) supportFragmentManager.findFragmentById(R.id.viewpagerFrag);
        mTopArrowIv = findViewById(R.id.topArrowIv);
        mTabLayout = findViewById(R.id.governmentTabs);
        mViewPager = findViewById(R.id.governmentViewPager);
        mPopupLayout = findViewById(R.id.bottomPopupLayout);
        // 绑定PopupLayout适配器
        bindPopupLayoutAdapter();
    }

    @Override
    protected void initData() {
        mModel = new GovernmentMapModel();
        mMapMarkerUtil = new MapMarkerUtil(this);
        mAreaId = getAreaId();
        mTabCategories = new ArrayList<>();
        mMechanismListFragments = new ArrayList<>();
        mViewPagerAdapter = new MechanismAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // 初始化地图，初始化成功后会获取要在地图上展示的数据
        mMapFragment.getMapAsync(this::onMapAsync);
        // 获得标签分类
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


    /**
     * Google 地图加载完成的回调
     * 与地图有关的操作，在此回调中执行
     *
     * @param googleMap 地图对象
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMapMarkerUtil.setMap(mGoogleMap);

        // 获取要展示在地图上的数据
        showLoading();
        // 获得展示在地图上的数据
        mModel.getMechanismListForMap(TYPE_GOVERNMENT, 14.6631685, 120.5887840, new BaseObserver<List<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<MechanismResponse> mechanismResponses) {
                stopLoading();
                mMapMarkerUtil.clear();
                if (mechanismResponses != null && mechanismResponses.size() > 0) {
                    mMapMarkerUtil.addAllMechanismForMap(mechanismResponses);
                    mMapMarkerUtil.addMarkerToMap();

                    mViewPagerFragment.bindAllData(mechanismResponses);
                }
            }
        });

//        这个应该就是移动了
//        googleMap.moveCamera();
        mGoogleMap.setOnMarkerClickListener(this::onMarkerClicked);
    }

    /**
     * marker 的点击回调
     *
     * @return 返回false表示我们尚未使用该事件，并且希望
     * 使默认行为发生，这是为了使摄像机移动以使标记居中，打开标记的信息窗口（如果有）。
     */
    private boolean onMarkerClicked(Marker marker) {

        // 根据marker当前的选择状态更改点击后的icon
        mMapMarkerUtil.changeMarkerStatus(marker);
        return false;
    }

    private int getAreaId() {
        try {
            return Integer.parseInt(SPUtil.getInstance().getString(SPKey.K_AREA_ID));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void onMapAsync(GoogleMap googleMap) {
        onMapReady(googleMap);
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

    /**
     * 获得tab view
     *
     * @param title tab title
     */
    private View makeTabView(String title) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_normal_only_text, null);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabTitle.setText(title);
        return tabView;
    }

    @Override
    protected void bindListener() {
        mTopArrowIv.setOnClickListener(this::onTopArrowClick);
        mViewPagerFragment.bindPageSelectedListener(this::onVpPageSelected);
    }

    /**
     * ViewPager item 滑动选择回调
     * @param position 索引
     */
    private void onVpPageSelected(int position) {
        // 选中 position 对应的 marker
        mMapMarkerUtil.selectMarker(position);
    }

    /**
     * 点击了箭头，展示政府机构列表
     * @param view
     */
    private void onTopArrowClick(View view) {
        //TODO  要更换 箭头 icon
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

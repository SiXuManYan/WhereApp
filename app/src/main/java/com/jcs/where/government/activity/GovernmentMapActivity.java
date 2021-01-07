package com.jcs.where.government.activity;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.SearchResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.adapter.GovernmentSearchAdapter;
import com.jcs.where.government.adapter.MechanismAdapter;
import com.jcs.where.government.fragment.CardViewPagerFragment;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.government.model.GovernmentMapModel;
import com.jcs.where.utils.EmptyWatcher;
import com.jcs.where.utils.MapMarkerUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.view.popup.PopupConstraintLayout;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.annotations.NonNull;

/**
 * 政府机构地图页
 * 政府机构列表是本页的fragment
 * create by zyf on 2020/12/28 7:44 PM
 */
public class GovernmentMapActivity extends BaseActivity implements OnMapReadyCallback {
    private SupportMapFragment mMapFragment;
    private CardViewPagerFragment mCardFragment;

    private PopupConstraintLayout mPopupLayout;
    private ImageView mTopArrowIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MechanismAdapter mViewPagerAdapter;

    // 搜索相关
    private EditText mSearchEt;
    private ImageView mDelInputIv;
    private RecyclerView mSearchRecycler;
    private GovernmentSearchAdapter mSearchAdapter;
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

    private final String TYPE_GOVERNMENT = "3";
    private int mAreaId = -1;

    @Override
    protected void initView() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mMapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.map);
        mCardFragment = (CardViewPagerFragment) supportFragmentManager.findFragmentById(R.id.viewpagerFrag);
        mTopArrowIv = findViewById(R.id.topArrowIv);
        mTabLayout = findViewById(R.id.governmentTabs);
        mViewPager = findViewById(R.id.governmentViewPager);
        mPopupLayout = findViewById(R.id.bottomPopupLayout);

        // 搜索相关
        mDelInputIv = findViewById(R.id.delInputIv);
        mSearchEt = findViewById(R.id.searchEt);
        mSearchRecycler = findViewById(R.id.searchRecycler);
        mSearchAdapter = new GovernmentSearchAdapter();
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));
        mSearchRecycler.setAdapter(mSearchAdapter);
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
        mViewPagerAdapter = new MechanismAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

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
                allCategory.setId(TYPE_GOVERNMENT);
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

                mViewPagerAdapter.setMechanismListFragments(mMechanismListFragments);
                mViewPagerAdapter.setTabCategories(mTabCategories);

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
                if (mechanismResponses.size() > 0) {
                    mMapMarkerUtil.addAllMechanismForMap(mechanismResponses);
                    mMapMarkerUtil.addMarkerToMap();

                    mCardFragment.bindAllData(mechanismResponses);
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
    public boolean onMarkerClicked(Marker marker) {

        // 根据marker当前的选择状态更改点击后的icon
        mMapMarkerUtil.changeMarkerStatus(marker);
        int selectPosition = mMapMarkerUtil.getMarkerPosition(marker);
        mCardFragment.selectPosition(selectPosition);
        return false;
    }

    private int getAreaId() {
        try {
            return Integer.parseInt(SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID));
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
        mCardFragment.bindPageSelectedListener(this::onVpPageSelected);
        mSearchAdapter.setOnItemClickListener(this::onSearchItemClicked);
        mSearchEt.setOnEditorActionListener(this::onSearchActionClicked);
        mSearchEt.addTextChangedListener(new EmptyWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("GovernmentMapActivity", "afterTextChanged: " + s.toString());
                if (s.length() == 0) {
                    showEmptySearchAdapter();
                } else {
                    mDelInputIv.setVisibility(View.VISIBLE);
                }
            }
        });
        mDelInputIv.setOnClickListener(this::onDelInputClicked);

        // 监听软键盘显示隐藏
        KeyboardVisibilityEvent.setEventListener(this, this::onKeyboardStatusChanged);
    }

    private void onDelInputClicked(View view) {
        ((TextView) mSearchEt).setText("");
        mDelInputIv.setVisibility(View.GONE);
        showEmptySearchAdapter();
    }

    private void showEmptySearchAdapter() {
        mSearchAdapter.getData().clear();
        mSearchAdapter.notifyDataSetChanged();
        mSearchAdapter.setEmptyView(R.layout.view_empty_data_brvah);
    }

    /**
     * 软键盘显示隐藏的监听回调
     *
     * @param open true：显示 false：隐藏
     */
    private void onKeyboardStatusChanged(boolean open) {
        if (!open) {
            mSearchRecycler.setVisibility(View.GONE);
        } else {
            if (mSearchAdapter.getItemCount() > 0) {
                mSearchRecycler.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean onSearchActionClicked(TextView textView, int actionId, KeyEvent keyEvent) {
        String input = textView.getText().toString();
        if (input.isEmpty()) {
            mMapMarkerUtil.restoreMap();
            mCardFragment.restore();
            hideInput();
        } else {
            showLoading();
            mModel.getSearchByInput(input, new BaseObserver<List<SearchResponse>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                public void onNext(@NotNull List<SearchResponse> searchResponses) {
                    stopLoading();
                    mSearchAdapter.getData().clear();
                    if (searchResponses.size() > 0) {
                        mSearchAdapter.addData(searchResponses);
                    } else {
                        mSearchAdapter.setEmptyView(R.layout.view_empty_data_brvah);
                    }
                    if (mSearchRecycler.getVisibility() == View.GONE) {
                        mSearchRecycler.setVisibility(View.VISIBLE);
                    }
                    mSearchEt.requestFocus();
                }
            });
        }
        return false;
    }

    private void onSearchItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        SearchResponse searchResponse = mSearchAdapter.getData().get(position);
        Integer id = searchResponse.getId();
        showLoading();
        mModel.getMechanismDetailById(id, new BaseObserver<MechanismDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NotNull MechanismDetailResponse mechanismDetailResponse) {
                stopLoading();
                mMapMarkerUtil.clearMap();
                mSearchRecycler.setVisibility(View.GONE);
                mMapMarkerUtil.addTempMarker(mechanismDetailResponse);
                mCardFragment.bindSingleData(getMechanismResponse(mechanismDetailResponse));
                Log.e("GovernmentMapActivity", "onNext: " + mechanismDetailResponse.getId());
            }
        });

        hideInput();
        Log.e("GovernmentMapActivity", "onSearchItemClicked: " + id);
    }

    private MechanismResponse getMechanismResponse(MechanismDetailResponse mechanismDetailResponse) {
        MechanismResponse mechanismResponse = new MechanismResponse();
        mechanismResponse.setId(mechanismDetailResponse.getId());
        mechanismResponse.setAddress(mechanismDetailResponse.getAddress());
        mechanismResponse.setImages(mechanismDetailResponse.getImages());
        mechanismResponse.setTitle(mechanismDetailResponse.getTitle());
        mechanismResponse.setLat(mechanismDetailResponse.getLat());
        mechanismResponse.setLng(mechanismDetailResponse.getLng());
        return mechanismResponse;
    }

    /**
     * ViewPager item 滑动选择回调
     *
     * @param position 索引
     */
    private void onVpPageSelected(int position) {
        // 选中 position 对应的 marker
        mMapMarkerUtil.selectMarker(position);
    }

    /**
     * 点击了箭头，展示政府机构列表
     *
     * @param view
     */
    private void onTopArrowClick(View view) {
        mPopupLayout.showOrHide();
        changeArrowIcon();
    }

    private void changeArrowIcon() {
        if (mPopupLayout.isShow()) {
            mTopArrowIv.setImageResource(R.mipmap.ic_big_arrow_bottom);
        } else {
            mTopArrowIv.setImageResource(R.mipmap.ic_big_arrow_top);
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_map;
    }
}

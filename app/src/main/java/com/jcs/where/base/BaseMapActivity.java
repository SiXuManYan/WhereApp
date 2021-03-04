package com.jcs.where.base;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.government.adapter.GovernmentSearchAdapter;
import com.jcs.where.government.adapter.MapListFragmentAdapter;
import com.jcs.where.government.fragment.CardViewPagerFragment;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.map.model.BaseMapModel;
import com.jcs.where.utils.EmptyWatcher;
import com.jcs.where.utils.MapMarkerUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.view.popup.PopupConstraintLayout;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.annotations.NonNull;

/**
 * create by zyf on 2021/1/28 12:07 下午
 */
public abstract class BaseMapActivity extends BaseActivity implements OnMapReadyCallback {
    private static final LatLng ADELAIDE = new LatLng(14.6778362, 120.5306459);
    public static final String K_CHILD_CATEGORY_ID = "childCategoryId";
    private String mChildCategoryId = "";
    private int mChildTabIndex = -1;

    protected SupportMapFragment mMapFragment;
    protected CardViewPagerFragment mCardFragment;

    protected PopupConstraintLayout mPopupLayout;
    protected ImageView mTopArrowIv;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected MapListFragmentAdapter mViewPagerAdapter;

    /**
     * 我的位置
     */
    protected ImageView mMyLocationIcon;

    // 搜索相关
    protected EditText mSearchEt;
    protected ImageView mDelInputIv;
    protected RecyclerView mSearchRecycler;
    protected GovernmentSearchAdapter mSearchAdapter;

    /**
     * 机构列表分类数据，展示在TabLayout上
     */
    protected List<CategoryResponse> mTabCategories;
    protected MapMarkerUtil mMapMarkerUtil;
    protected GoogleMap mGoogleMap;

    /**
     * 从 sp 中拿到当前的地区 id
     */
    protected int mAreaId = -1;

    private BaseMapModel mModel;

    @Override
    protected void initView() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mMapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.map);
        mCardFragment = (CardViewPagerFragment) supportFragmentManager.findFragmentById(R.id.viewpagerFrag);
        mTopArrowIv = findViewById(R.id.topArrowIv);
        mTabLayout = findViewById(R.id.governmentTabs);
        mViewPager = findViewById(R.id.governmentViewPager);
        mPopupLayout = findViewById(R.id.bottomPopupLayout);

        // 我的位置
        mMyLocationIcon = findViewById(R.id.myLocationIcon);

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
        mAreaId = getAreaId();

        // 初始化地图，初始化成功后会获取要在地图上展示的数据
        mMapFragment.getMapAsync(this::onMapAsync);
        mModel = new BaseMapModel();
        mMapMarkerUtil = new MapMarkerUtil(this);
        mMapMarkerUtil.setMyPosition(ADELAIDE);
        // 从CategoryFragment点击item跳转过来，要选择子分类的列表
        mChildCategoryId = getIntent().getStringExtra(K_CHILD_CATEGORY_ID);
        mTabCategories = new ArrayList<>();
        mViewPagerAdapter = new MapListFragmentAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // 获得标签分类
        mModel.getCategories(getCategoryLevel(), getParentCategoryId(), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull List<CategoryResponse> categoryResponses) {
                mTabCategories.clear();

                // 添加全部对应的Tab
                CategoryResponse allCategory = new CategoryResponse();
                allCategory.setName(getString(R.string.all));
                allCategory.setId(getAllCategoryId());
                mTabCategories.add(allCategory);
                mTabCategories.addAll(categoryResponses);

                // 0 表示要获得全部的信息，添加全部对应的ListFragment
                addFistFragmentToList(allCategory);
                int size = mTabCategories.size();
                mTabLayout.removeAllTabs();
                for (int i = 0; i < size; i++) {
                    TabLayout.Tab tab = mTabLayout.newTab();
                    CategoryResponse categoryResponse = mTabCategories.get(i);
                    tab.setCustomView(makeTabView(categoryResponse.getName()));
                    mTabLayout.addTab(tab);

                    // 0 位置对应的是全部，已经在循环外添加过了
                    if (i != 0) {
                        addFragmentToList(categoryResponse);
                    }
                    if (categoryResponse.getId().equals(mChildCategoryId)) {
                        mChildTabIndex = i;
                    }
                }

                mViewPagerAdapter.setListFragments(getListFragments());
                mViewPagerAdapter.setTabCategories(mTabCategories);

                mViewPager.setAdapter(mViewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setOffscreenPageLimit(mTabCategories.size());
                if (mChildTabIndex != -1) {
                    mViewPager.setCurrentItem(mChildTabIndex);
                }
            }
        });
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

        // 点击我的位置icon
        mMyLocationIcon.setOnClickListener(this::onMyLocationIconClicked);
    }

    private void onMyLocationIconClicked(View view) {
        mMapMarkerUtil.backMyPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPopupLayout != null) {
            mPopupLayout.setBackgroundColor(getColor(R.color.white));
        }
    }

    protected void onDelInputClicked(View view) {
        ((TextView) mSearchEt).setText("");
        mDelInputIv.setVisibility(View.GONE);
        showEmptySearchAdapter();
    }

    protected boolean onSearchActionClicked(TextView textView, int actionId, KeyEvent keyEvent) {
        String input = textView.getText().toString();
        if (input.isEmpty()) {
            mMapMarkerUtil.restoreMap();
            mCardFragment.restore();
            mCardFragment.selectPosition(mMapMarkerUtil.getCurrentPosition());
            hideInput();
        } else {
            showLoading();
            getListDataByInput(input);
        }
        return false;
    }

    protected void onSearchItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        MechanismResponse item = mSearchAdapter.getData().get(position);
        mMapMarkerUtil.clearMap();
        mSearchRecycler.setVisibility(View.GONE);
        mMapMarkerUtil.addTempMarker(item);
        mCardFragment.bindSingleData(item);
        hideInput();
    }

    protected void showEmptySearchAdapter() {
        mSearchAdapter.getData().clear();
        mSearchAdapter.notifyDataSetChanged();
        mSearchAdapter.setEmptyView(R.layout.view_empty_data_brvah);
    }

    /**
     * 软键盘显示隐藏的监听回调
     *
     * @param open true：显示 false：隐藏
     */
    protected void onKeyboardStatusChanged(boolean open) {
        if (!open) {
            mSearchRecycler.setVisibility(View.GONE);
        } else {
            if (mSearchAdapter.getItemCount() > 0) {
                mSearchRecycler.setVisibility(View.VISIBLE);
            }
        }
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

    /**
     * ViewPager item 滑动选择回调
     *
     * @param position 索引
     */
    protected void onVpPageSelected(int position) {
        // 选中 position 对应的 marker
        mMapMarkerUtil.selectMarker(position);
    }

    /**
     * 点击了箭头，展示政府机构列表
     *
     * @param view
     */
    protected void onTopArrowClick(View view) {
        mPopupLayout.showOrHide();
        changeArrowIcon();
    }

    protected void changeArrowIcon() {
        if (mPopupLayout.isShow()) {
            mTopArrowIv.setImageResource(R.mipmap.ic_big_arrow_bottom);
        } else {
            mTopArrowIv.setImageResource(R.mipmap.ic_big_arrow_top);
        }
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
        getDataAtMapFromNet();

        mGoogleMap.setOnMarkerClickListener(this::onMarkerClicked);
    }

    /**
     * 添加第一个Fragment 对应：全部
     */
    protected abstract void addFistFragmentToList(CategoryResponse categoryResponse);

    /**
     * 添加Fragment
     */
    protected abstract void addFragmentToList(CategoryResponse categoryResponse);

    /**
     * 获得展示在 Map 上的数据
     */
    protected abstract void getDataAtMapFromNet();

    /**
     * 搜索
     */
    protected abstract void getListDataByInput(String input);

    /**
     * 获得全部分类对应的分类id
     * （政府地图页拿到的就是政府一级id）
     * （旅游景点地图页拿到的就是旅游景点的id）
     */
    protected abstract String getAllCategoryId();

    /**
     * 获得获得当前分类id，注意不是type
     */
    protected abstract String getParentCategoryId();

    /**
     * 获得子分类的级别
     */
    protected abstract int getCategoryLevel();

    /**
     * 拿到子类的Fragment列表设置到VP上
     */
    protected abstract List<? extends Fragment> getListFragments();

    /**
     * 第一个fragment获取网络数据
     */
    protected abstract void firstFragmentGetData();

    /**
     * 获得tab view
     *
     * @param title tab title
     */
    protected View makeTabView(String title) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_normal_only_text, null);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabTitle.setText(title);
        return tabView;
    }

    /**
     * 绑定 popupLayout 适配器
     */
    protected void bindPopupLayoutAdapter() {
        mPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {
            @Override
            public void onShowCompleted() {
                firstFragmentGetData();
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
    protected boolean isStatusDark() {
        return true;
    }
}

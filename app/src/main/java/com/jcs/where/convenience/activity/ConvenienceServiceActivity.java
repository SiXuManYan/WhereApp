package com.jcs.where.convenience.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.convenience.adapter.CityAdapter;
import com.jcs.where.convenience.model.ConvenienceServiceModel;
import com.jcs.where.government.adapter.MapListFragmentAdapter;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.search.SearchActivity;
import com.jcs.where.search.tag.SearchTag;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.RequestResultCode;
import com.jcs.where.utils.SPKey;
import com.jcs.where.view.popup.PopupConstraintLayout;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * 综合服务页面
 * create by zyf on 2021/1/5 3:55 下午
 */
public class ConvenienceServiceActivity extends BaseActivity {
    public static final String K_CATEGORIES = "categoryId";
    public static final String K_SERVICE_NAME = "serviceName";
    public static final String K_CHILD_CATEGORY_ID = "childCategoryId";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PopupConstraintLayout mPopupLayout;
    private RecyclerView mCityRecycler;
    private TextView mCityTv;
    private MapListFragmentAdapter mViewPagerAdapter;
    private ConvenienceServiceModel mModel;
    private CityAdapter mCityAdapter;

    private String mCategoryId;
    private String mServiceName;
    private String mChildCategoryId;

    /**
     * 展示机构列表的Fragment集合
     */
    private List<MechanismListFragment> mMechanismListFragments;

    /**
     * 机构列表分类数据，展示在TabLayout上
     */
    private List<CategoryResponse> mTabCategories;

    /**
     * 城市选择数据
     */
    private List<CityResponse> mCities;
    private CityResponse mCurrentCityResponse;
    private int mDefaultIndex;
    private View mSearchView;
    private EditText mSearchEt;

    @Override
    protected void initView() {
        mSearchView = findViewById(R.id.searchEtView);
        mSearchEt = findViewById(R.id.searchEt);
        mCityTv = findViewById(R.id.cityTv);
        mPopupLayout = findViewById(R.id.topPopupLayout);
        mPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {
            @Override
            public boolean enableAnim() {
                return false;
            }

            @Override
            public int getMaxHeight() {
                return getPxFromDp(345);
            }
        });
        mCityRecycler = findViewById(R.id.cityRecycler);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPagerAdapter = new MapListFragmentAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mCityAdapter = new CityAdapter();
        mCityRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCityRecycler.setAdapter(mCityAdapter);

    }

    @Override
    protected void initData() {
        mModel = new ConvenienceServiceModel();
        Intent intent = getIntent();
        mCategoryId = intent.getStringExtra(K_CATEGORIES);
        mServiceName = intent.getStringExtra(K_SERVICE_NAME);
        mChildCategoryId = intent.getStringExtra(K_CHILD_CATEGORY_ID);
        mJcsTitle.setMiddleTitle(mServiceName);
        mTabCategories = new ArrayList<>();
        mMechanismListFragments = new ArrayList<>();

        getDataFromNativeOrNet();
    }

    private void getDataFromNativeOrNet() {
        String jsonCity = mModel.needUpdateCity();
        String jsonCategory = mModel.needUpdateCategory(mCategoryId);

        if (jsonCity.isEmpty() && jsonCategory.isEmpty()) {
            // 获取网路数据
            getInitConvenienceService();
            return;
        }

        if (jsonCategory.isEmpty()) {
            // 获取 或 更新 当前页的分类数据
            getCategoriesFromNet();
        } else {
            addCategoryNamedAll();
            mTabCategories.addAll(
                    JsonUtil.getInstance().fromJsonToList(
                            jsonCategory, new TypeToken<List<CategoryResponse>>() {
                            }.getType()
                    )
            );
            // 展示数据
            injectTabDataToView();
        }

        if (jsonCity.isEmpty()) {
            // 获取 或 更新 city 数据
            getCitiesFromNet();
        } else {
            List<CityResponse> cities = JsonUtil.getInstance().fromJsonToList(
                    jsonCity, new TypeToken<List<CityResponse>>() {
                    }.getType()
            );
            injectCityDataToView(cities);
        }


    }

    private void getCategoriesFromNet() {
        showLoading();
        mModel.getCategories(mCategoryId, new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull List<CategoryResponse> categoryResponses) {
                stopLoading();
                addCategoryNamedAll();
                mTabCategories.addAll(categoryResponses);
                // 展示数据
                injectTabDataToView();
            }
        });
    }

    private void getCitiesFromNet() {
        showLoading();
        mModel.getAreaList(new BaseObserver<List<CityResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull List<CityResponse> cityResponses) {
                stopLoading();
                injectCityDataToView(cityResponses);
            }
        });
    }

    private void injectCityDataToView(@NotNull List<CityResponse> cityResponses) {
        mCities = cityResponses;
        int currentCityIndex = mModel.getCityResponseIndexById(mCities);
        mCityAdapter.setSelectedPosition(currentCityIndex);
        if (currentCityIndex != -1) {
            mCurrentCityResponse = mCities.get(currentCityIndex);
            mCityTv.setText(mCurrentCityResponse.getName());
        } else {
            mCityTv.setText(getString(R.string.all));
        }
        mModel.saveCities(cityResponses);
        mCityAdapter.getData().clear();
        mCityAdapter.addData(cityResponses);
    }


    private void getInitConvenienceService() {
        showLoading();
        mModel.getInitData(mCategoryId, new BaseObserver<ConvenienceServiceModel.ConvenienceServiceZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull ConvenienceServiceModel.ConvenienceServiceZipResponse convenienceServiceZipResponse) {
                stopLoading();
                mTabCategories.clear();
                mMechanismListFragments.clear();

                List<CityResponse> cities = convenienceServiceZipResponse.getCities();
                if (cities != null) {
                    // 存储城市数据
                    CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_ALL_CITIES, cities);
                }


                List<CategoryResponse> categories = convenienceServiceZipResponse.getCategories();

                addCategoryNamedAll();
                mTabCategories.addAll(categories);

                // 存储当前分类信息
                CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_SERVICE_CATEGORIES, categories);

                // 展示数据
                injectTabDataToView();
                if (cities != null) {
                    injectCityDataToView(cities);
                }
            }
        });
    }

    /**
     * 展示数据
     */
    private void injectTabDataToView() {
        mDefaultIndex = 0;
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
            if (categoryResponse.getId().equals(mChildCategoryId)) {
                mDefaultIndex = i;
            }
        }

        mViewPagerAdapter.setListFragments(mMechanismListFragments);
        mViewPagerAdapter.setTabCategories(mTabCategories);

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mViewPagerAdapter.getCount());
        mViewPagerAdapter.notifyDataSetChanged();
        if (mDefaultIndex != 0) {
            mViewPager.setCurrentItem(mDefaultIndex);
        }
    }

    private void addCategoryNamedAll() {
        // 添加全部对应的Tab
        CategoryResponse allCategory = new CategoryResponse();
        allCategory.setName(getString(R.string.all));
        allCategory.setId(mCategoryId);
        mTabCategories.add(allCategory);

        // 0 表示要获得全部的信息，添加全部对应的ListFragment
        mMechanismListFragments.add(MechanismListFragment.newInstance(allCategory, true));
    }

    @Override
    protected void bindListener() {
        mCityAdapter.setOnItemClickListener(this::onCityItemClicked);
        mCityTv.setOnClickListener(this::onCityTvClicked);
        mSearchView.setOnClickListener(this::onSearchViewClicked);
        mSearchEt.setOnClickListener(this::onSearchViewClicked);
    }

    private void onSearchViewClicked(View view) {
        SearchActivity.goTo(this, "", mCategoryId, SearchTag.CONVENIENCE_SERVICE, RequestResultCode.CONVENIENCE_SERVICE_TO_SEARCH);
    }

    private void onCityTvClicked(View view) {
        mPopupLayout.showOrHide();
    }

    private void onCityItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        mCityAdapter.setSelectedPosition(position);
        mCityAdapter.notifyDataSetChanged();
        mCurrentCityResponse = mCities.get(position);
        mCityTv.setText(mCurrentCityResponse.getName());
        mPopupLayout.hide();
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
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_convenience_service;
    }

}

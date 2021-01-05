package com.jcs.where.convenience.activity;

import android.view.LayoutInflater;
import android.view.View;
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
import com.jcs.where.government.adapter.MechanismAdapter;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
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
 * 便民服务
 * create by zyf on 2021/1/5 3:55 下午
 */
public class ConvenienceServiceActivity extends BaseActivity {
    public static final String K_CATEGORIES = "categoryId";
    private final int TYPE_GOVERNMENT = 3;


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PopupConstraintLayout mPopupLayout;
    private RecyclerView mCityRecycler;
    private TextView mCityTv;
    private MechanismAdapter mViewPagerAdapter;
    private ConvenienceServiceModel mModel;
    private CityAdapter mCityAdapter;

    private String mCategoryId;
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

    @Override
    protected void initView() {
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
        mViewPagerAdapter = new MechanismAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mCityAdapter = new CityAdapter();
        mCityRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCityRecycler.setAdapter(mCityAdapter);

    }

    @Override
    protected void initData() {
        mModel = new ConvenienceServiceModel();
        mCategoryId = getIntent().getStringExtra(K_CATEGORIES);
        mTabCategories = new ArrayList<>();
        mMechanismListFragments = new ArrayList<>();

        getDataFromNativeOrNet();
    }

    private void getDataFromNativeOrNet() {
        String jsonCity = mModel.needUpdateCity();
        String jsonCategory = mModel.needUpdateCategory();
        if (jsonCity.isEmpty()) {
            // 获取 或 更新 city 数据
            getCitiesFromNet();
        } else {
            List<CityResponse> cities = JsonUtil.getInstance().fromJsonToList(
                    jsonCity, new TypeToken<List<CategoryResponse>>() {
                    }.getType()
            );
            injectCityDataToView(cities);
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

        if (jsonCity.isEmpty() && jsonCategory.isEmpty()) {
            // 获取网路数据
            getInitConvenienceService();
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
            public void onNext(@NotNull List<CategoryResponse> categoryResponses) {
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
            public void onNext(@NotNull List<CityResponse> cityResponses) {
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
            public void onNext(@NotNull ConvenienceServiceModel.ConvenienceServiceZipResponse convenienceServiceZipResponse) {
                stopLoading();
                mTabCategories.clear();
                mMechanismListFragments.clear();

                List<CityResponse> cities = convenienceServiceZipResponse.getCities();
                long currentTime = System.currentTimeMillis();
                if (cities != null) {
                    String citiesJsonStr = JsonUtil.getInstance().toJsonStr(cities);
                    String saveCities = citiesJsonStr + SPKey.K_DELIMITER + currentTime;
                    SPUtil.getInstance().saveString(SPKey.K_ALL_CITIES, saveCities);
                }


                List<CategoryResponse> categories = convenienceServiceZipResponse.getCategories();

                addCategoryNamedAll();
                mTabCategories.addAll(categories);

                // 存储当前分类信息
                String categoriesJsonStr = JsonUtil.getInstance().toJsonStr(categories);
                String saveCategories = categoriesJsonStr + SPKey.K_DELIMITER + currentTime;
                SPUtil.getInstance().saveString(SPKey.K_CONVENIENCE_SERVICE_CATEGORIES, saveCategories);

                // 展示数据
                injectTabDataToView();
                injectCityDataToView(cities);
            }
        });
    }

    /**
     * 展示数据
     */
    private void injectTabDataToView() {
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

    private void addCategoryNamedAll() {
        // 添加全部对应的Tab
        CategoryResponse allCategory = new CategoryResponse();
        allCategory.setName(getString(R.string.all));
        allCategory.setId(0);
        allCategory.setType(TYPE_GOVERNMENT);
        mTabCategories.add(allCategory);

        // 0 表示要获得全部的信息，添加全部对应的ListFragment
        mMechanismListFragments.add(MechanismListFragment.newInstance(allCategory, true));
    }

    @Override
    protected void bindListener() {
        mCityAdapter.setOnItemClickListener(this::onCityItemClicked);
        mCityTv.setOnClickListener(this::onCityTvClicked);
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

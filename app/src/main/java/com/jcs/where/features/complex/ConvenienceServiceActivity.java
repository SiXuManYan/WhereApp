package com.jcs.where.features.complex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.features.complex.child.ConvenienceChildFragment;
import com.jcs.where.features.search.SearchAllActivity;
import com.jcs.where.government.adapter.MapListFragmentAdapter;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.view.empty.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private MapListFragmentAdapter mViewPagerAdapter;
    private ConvenienceServiceModel mModel;

    private String mCategoryId;
    private String mServiceName;
    private String mChildCategoryId;

    /**
     * 展示机构列表的Fragment集合
     */
    private List<ConvenienceChildFragment> mMechanismListFragments;

    /**
     * 机构列表分类数据，展示在TabLayout上
     */
    private List<CategoryResponse> mTabCategories;


    private int mDefaultIndex;
    private EmptyView service_empty;
    private ImageView filer_iv;
    private RadioGroup sort_rg;

    /**
     * 1 推荐
     * 0 距离最近
     */
    private Integer recommend = 1;
    private LinearLayout filter_ll;

    @Override
    protected void initView() {

        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPagerAdapter = new MapListFragmentAdapter(getSupportFragmentManager(), 0);

        service_empty = findViewById(R.id.service_empty);
        service_empty.hideEmptyContainer();

        filter_ll = findViewById(R.id.filter_ll);
        filer_iv = findViewById(R.id.filer_iv);
        sort_rg = findViewById(R.id.sort_rg);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }

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


    }

    private void getCategoriesFromNet() {
        showLoadingDialog();
        mModel.getCategories(mCategoryId, new BaseObserver<List<CategoryResponse>>() {


            @Override
            public void onSuccess(@NotNull List<CategoryResponse> categoryResponses) {
                dismissLoadingDialog();
                service_empty.setVisibility(View.GONE);
                addCategoryNamedAll();
                mTabCategories.addAll(categoryResponses);
                // 展示数据
                injectTabDataToView();
            }


            @Override
            protected void onError(ErrorResponse errorResponse) {
                dismissLoadingDialog();
                if (service_empty.getVisibility() != View.VISIBLE) {
                    service_empty.setVisibility(View.VISIBLE);
                    service_empty.showNetworkError(v -> {
                        getCategoriesFromNet();
                        service_empty.setVisibility(View.GONE);
                    });

                }
            }
        });
    }


    private void getInitConvenienceService() {
        showLoadingDialog();
        mModel.getInitData(mCategoryId, new BaseObserver<ConvenienceServiceModel.ConvenienceServiceZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                dismissLoadingDialog();
                if (!emptyViewList.isEmpty()) {
                    for (EmptyView emptyView : emptyViewList) {
                        emptyView.showNetworkError(null);
                    }
                }
            }

            @Override
            public void onSuccess(@NotNull ConvenienceServiceModel.ConvenienceServiceZipResponse convenienceServiceZipResponse) {
                dismissLoadingDialog();
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

            }
        });
    }

    /**
     * 展示数据
     */
    private void injectTabDataToView() {
        mDefaultIndex = 0;

        int size = mTabCategories.size();
        for (int i = 0; i < size; i++) {
            CategoryResponse categoryResponse = mTabCategories.get(i);
            // 0 位置对应的是全部，已经在循环外添加过了
            if (i != 0) {
                mMechanismListFragments.add(ConvenienceChildFragment.Companion.newInstance(categoryResponse));
            }
        }
        mViewPagerAdapter.setListFragments(mMechanismListFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(mViewPagerAdapter.getCount());
        mViewPagerAdapter.setTabCategories(mTabCategories);
        mViewPagerAdapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);

//        mTabLayout.removeAllTabs();
        for (int i = 0; i < size; i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            CategoryResponse categoryResponse = mTabCategories.get(i);
            tab.setCustomView(makeTabView(categoryResponse.getName()));
//            mTabLayout.addTab(tab);

            if (categoryResponse.getId().equals(mChildCategoryId)) {
                mDefaultIndex = i;
            }
        }

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
        mMechanismListFragments.add(ConvenienceChildFragment.Companion.newInstance(allCategory));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void bindListener() {


        mJcsTitle.setFirstRightIvClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PARAM_TYPE, 2);
            bundle.putString(Constant.PARAM_CATEGORY_ID, mCategoryId);
            startActivity(SearchAllActivity.class, bundle);
        });
        filer_iv.setOnClickListener(v -> {
            handleFilterVisibility();
        });

        sort_rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.praise_rb:
                    recommend = 1;
                    EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_REFRESH_CONVENIENCE_CHILD, recommend));
                    handleFilterVisibility();
                    break;
                case R.id.sales_rb:
                    recommend = 0;
                    EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_REFRESH_CONVENIENCE_CHILD, recommend));
                    handleFilterVisibility();
                    break;

                default:
                    break;
            }
        });


        findViewById(R.id.dismiss_view).setOnClickListener(v -> {
            handleFilterVisibility();
        });


    }

    private void handleFilterVisibility() {
        if (filter_ll.getVisibility() == View.GONE) {
            filter_ll.setVisibility(View.VISIBLE);
        } else {
            filter_ll.setVisibility(View.GONE);
        }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

    }

}

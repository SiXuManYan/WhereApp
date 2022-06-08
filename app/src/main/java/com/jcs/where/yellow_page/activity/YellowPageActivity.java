package com.jcs.where.yellow_page.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.map.MechanismAdapter;
import com.jcs.where.features.mechanism.MechanismActivity;
import com.jcs.where.features.search.SearchAllActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.list.DividerDecoration;
import com.jcs.where.yellow_page.CategoryToSelectedListFragment;
import com.jcs.where.yellow_page.model.YellowPageModel;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 页面-企业黄页
 * create by zyf on 2021/1/3 10:14 AM
 */
public class YellowPageActivity extends BaseActivity implements OnLoadMoreListener {

    public static final String K_ID = "id";
    public static final String K_CATEGORIES = "categories";
    public static final String K_DEFAULT_CHILD_CATEGORY_ID = "defaultChildCategoryId";

    private TextView mFirstCateTv, mSecondCateTv, mThirdCateTv;
    private ImageView mFirstArrowIv, mSecondArrowIv, mThirdArrowIv;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;

    private MechanismAdapter mAdapter;

    private YellowPageModel mModel;

    private CategoryToSelectedListFragment mToSelectedListFragment;

    /**
     * 企业黄页对应的分类id
     */
    private int mId = 2;
    private List<Integer> mCategories;
    private String mDefaultChildCategoryId;
    private Intent mIntent;
    private List<CategoryResponse> mFirstLevelCategories;


    @Override
    protected void initView() {

        mFirstCateTv = findViewById(R.id.firstCateTv);
        mSecondCateTv = findViewById(R.id.secondCateTv);
        mThirdCateTv = findViewById(R.id.thirdCateTv);

        mFirstArrowIv = findViewById(R.id.firstArrowIv);
        mSecondArrowIv = findViewById(R.id.secondArrowIv);
        mThirdArrowIv = findViewById(R.id.thirdArrowIv);
        mJcsTitle.setFirstRightIvClickListener(this::onSearchClicked);

        setSecondLevelEnable(false);
        setThirdLevelEnable(false);

        mSwipeLayout = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.yellowPageRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        EmptyView emptyView = new EmptyView(this);
        emptyView.showEmptyDefault();

        // adapter
        mAdapter = new MechanismAdapter();
        mAdapter.setEmptyView(emptyView);
        BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
        loadMoreModule.setAutoLoadMore(true);
        loadMoreModule.setEnableLoadMoreIfNotFullPage(true);
        loadMoreModule.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        DividerDecoration decoration = new DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0);
        mRecyclerView.addItemDecoration(decoration);


        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mToSelectedListFragment = (CategoryToSelectedListFragment) supportFragmentManager.findFragmentById(R.id.categoryFragment);
        if (mToSelectedListFragment != null) {
            mToSelectedListFragment.setListener(this::onCategorySelected);
        }
        getSupportFragmentManager().beginTransaction().hide(mToSelectedListFragment).commit();
    }

    @Override
    protected void initData() {
        mModel = new YellowPageModel();
        mIntent = getIntent();
        mDefaultChildCategoryId = mIntent.getStringExtra(K_DEFAULT_CHILD_CATEGORY_ID);
        // 获取企业黄页的下级分类id
        initCategories();

        // 从本地或网络获取数据
        getData();
    }

    private void getData() {
        String jsonStr = CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_CATEGORIES);
        if (jsonStr.equals("")) {

            getInitYellowPage();
        } else {
            mFirstLevelCategories = JsonUtil.getInstance().fromJsonToList(jsonStr, new TypeToken<List<CategoryResponse>>() {
            }.getType());
            // 将分类数据注入分类选中Fragment中
            injectToSelectFragment();
            if (mDefaultChildCategoryId == null) {
                getMechanismDataFromNet(page, mCategories.toString(), "");
            } else {
                // 根据选中的分类，设置 mFirstCateTv  mSecondCateTv mThirdCateTv 的状态
                toChangeLevelTitleStatus();
                getMechanismDataFromNet(page, mDefaultChildCategoryId, "");
            }
        }
    }

    /**
     * 一次性获取当前页面默认展示的数据
     * 分类，对应企业黄页下所有分类的数据列表
     */
    private void getInitYellowPage() {
        showLoading();
        String categoryIds = mCategories.toString();
        if (mDefaultChildCategoryId != null) {
            categoryIds = mDefaultChildCategoryId;
        }
        mModel.getInitData(categoryIds, new BaseObserver<YellowPageModel.YellowPageZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull YellowPageModel.YellowPageZipResponse yellowPageZipResponse) {
                stopLoading();
                mFirstLevelCategories = yellowPageZipResponse.getCategories();
                // 将分类数据注入分类选择Fragment中
                injectToSelectFragment();
                if (mDefaultChildCategoryId != null) {
                    // 根据选中的分类，设置 mFirstCateTv  mSecondCateTv mThirdCateTv 的状态
                    toChangeLevelTitleStatus();
                }

                // 缓存
                if (mFirstLevelCategories.size() > 0) {
                    CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_CATEGORIES, mFirstLevelCategories);
                }
                PageResponse<MechanismResponse> mechanismPageResponse = yellowPageZipResponse.getMechanismPageResponse();
                updateAdapter(mechanismPageResponse);
            }
        });
    }

    /**
     * 根据选中的分类，设置 mFirstCateTv  mSecondCateTv mThirdCateTv 的状态
     */
    private void toChangeLevelTitleStatus() {
        CategoryResponse selectFirstCate = mToSelectedListFragment.getSelectFirstCate();
        if (selectFirstCate == null) {
            return;
        }
        mFirstCateTv.setText(selectFirstCate.getName());
        CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
        if (selectSecondCate == null) {
            changeLevelTitleStatus(mFirstCateTv, R.color.blue_4D9FF2, mFirstArrowIv, R.mipmap.ic_store_down);
            return;
        }
        mSecondCateTv.setText(selectSecondCate.getName());
        mSecondCateTv.setOnClickListener(this::onSecondCateClicked);
        mThirdCateTv.setOnClickListener(this::onThirdCateClicked);
        changeLevelTitleStatus(mFirstCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_store_down);
        changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_store_down);
        CategoryResponse selectThirdCate = mToSelectedListFragment.getSelectThirdCate();
        if (selectThirdCate == null) {
            changeLevelTitleStatus(mSecondCateTv, R.color.blue_4D9FF2, mSecondArrowIv, R.mipmap.ic_store_down);
            return;
        }

        mThirdCateTv.setText(selectThirdCate.getName());
        changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mSecondArrowIv, R.mipmap.ic_store_down);
        changeLevelTitleStatus(mThirdCateTv, R.color.blue_377BFF, mThirdArrowIv, R.mipmap.ic_store_down);
    }

    private void changeLevelTitleStatus(TextView cateTv, int colorRes, ImageView arrowIv, int iconRes) {
        cateTv.setTextColor(getColor(colorRes));
        arrowIv.setImageResource(iconRes);
    }

    /**
     * 将分类数据注入分类选中Fragment中
     */
    private void injectToSelectFragment() {
        mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
        mToSelectedListFragment.setTotalCategories(mFirstLevelCategories);
        mToSelectedListFragment.setFirstLevelTotalIds(mCategories.toString());
        mToSelectedListFragment.setDefaultChildCategoryId(mDefaultChildCategoryId);
    }

    private void getMechanismDataFromNet(int page, String categoryId, String search) {
        mModel.getMechanismList2(page, categoryId, search, new BaseObserver<PageResponse<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull PageResponse<MechanismResponse> mechanismPageResponse) {
                mSwipeLayout.setRefreshing(false);
                updateAdapter(mechanismPageResponse);
            }
        });
    }

    private void updateAdapter(@NonNull PageResponse<MechanismResponse> response) {
        List<MechanismResponse> data = response.getData();
        BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
        boolean lastPage = response.getLastPage() == page;

        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                loadMoreModule.loadMoreComplete();
            } else {
                loadMoreModule.loadMoreEnd();
            }
            return;
        }

        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data);
            loadMoreModule.checkDisableLoadMoreIfNotFullPage();
        } else {
            mAdapter.addData(data);
            if (lastPage) {
                loadMoreModule.loadMoreEnd();
            } else {
                loadMoreModule.loadMoreComplete();
            }
        }

    }

    private void initCategories() {
        mCategories = mIntent.getIntegerArrayListExtra(K_CATEGORIES);
    }

    @Override
    protected void bindListener() {
        mFirstCateTv.setOnClickListener(this::onFirstCateClicked);

        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);

        mAdapter.setOnItemClickListener(this::onMechanismItemClicked);

    }

    private void onSearchClicked(View view) {
        String categoryId = mToSelectedListFragment.getCurrentCategoryId();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.PARAM_TYPE, 1);
        bundle.putString(Constant.PARAM_CATEGORY_ID, categoryId);
        startActivity(SearchAllActivity.class, bundle);
    }

    private void onMechanismItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        MechanismResponse response = mAdapter.getData().get(position);
        Bundle b = new Bundle();
        b.putInt(Constant.PARAM_ID, response.id);
        startActivity(MechanismActivity.class, b);
    }

    private int page = Constant.DEFAULT_FIRST_PAGE;

    private void onSwipeRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;

        loadData();
    }

    private void loadData() {
        if (mToSelectedListFragment.isNoLevel()) {
            // 获得全部数据
            getMechanismDataFromNet(page, mFirstLevelCategories.toString(), "");
        } else {
            // 根据当前分类id刷新数据
            String id = mToSelectedListFragment.getCurrentCategoryId();
            getMechanismDataFromNet(page, id, "");
        }
    }

    public void onFirstCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isFirstLevel()) {
            changeLevelTitleStatus(mFirstCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_store_down);
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isFirstLevel()) {
                // 如果选项列表数据不是一级分类数据，则设置为一级分类数据
                mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
                CategoryResponse selectFirstCate = mToSelectedListFragment.getSelectFirstCate();
                if (selectFirstCate != null && selectFirstCate.getChild_categories().size() > 0) {
                    changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mSecondArrowIv, R.mipmap.ic_store_down);
                } else {
                    setThirdLevelEnable(false);
                }

                CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
                if (selectSecondCate != null && selectSecondCate.getChild_categories().size() > 0) {
                    changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_store_down);
                } else {
                    setThirdLevelEnable(false);
                }
            } else {
                mToSelectedListFragment.notifyAdapter();
            }
            changeLevelTitleStatus(mFirstCateTv, R.color.blue_4D9FF2, mFirstArrowIv, R.mipmap.ic_store_up);
            showCategoryListFragment();
        }
    }

    public void onSecondCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isSecondLevel()) {
            changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mSecondArrowIv, R.mipmap.ic_store_down);
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isSecondLevel()) {
                // 如果选项列表数据不是二级分类数据，则设置为二级分类数据
                CategoryResponse firstSelected = mFirstLevelCategories.get(mToSelectedListFragment.getSelectFirstPosition());
                mToSelectedListFragment.setData(firstSelected.getChild_categories(), CategoryToSelectedListFragment.LEVEL_SECOND);
                changeLevelTitleStatus(mFirstCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_store_down);
                CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
                if (selectSecondCate != null && selectSecondCate.getChild_categories().size() > 0) {
                    changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_store_down);
                } else {
                    setThirdLevelEnable(false);
                }
            } else {
                // 不刷新的话，选中状态的 对勾 icon 无法显示
                mToSelectedListFragment.notifyAdapter();
            }
            changeLevelTitleStatus(mSecondCateTv, R.color.blue_4D9FF2, mSecondArrowIv, R.mipmap.ic_store_up);
            showCategoryListFragment();
        }
    }

    public void onThirdCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isThirdLevel()) {
            hideCategoryListFragment();
            changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_store_down);
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isThirdLevel()) {
                // 如果选项列表数据不是三级分类数据，则设置为三级分类数据
                CategoryResponse firstSelected = mFirstLevelCategories.get(mToSelectedListFragment.getSelectFirstPosition());
                CategoryResponse secondSelected = firstSelected.getChild_categories().get(mToSelectedListFragment.getSelectSecondPosition());
                mToSelectedListFragment.setData(secondSelected.getChild_categories(), CategoryToSelectedListFragment.LEVEL_THIRD);
                mFirstCateTv.setTextColor(getColor(R.color.black_333333));
                changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_store_down);
                mSecondArrowIv.setImageResource(R.mipmap.ic_store_down);
            } else {
                // 不刷新的话，选中状态的 对勾 icon 无法显示
                mToSelectedListFragment.notifyAdapter();
            }
            changeLevelTitleStatus(mThirdCateTv, R.color.blue_4D9FF2, mThirdArrowIv, R.mipmap.ic_store_up);
            showCategoryListFragment();
        }
    }

    private void setSecondLevelEnable(boolean b) {
        mSecondCateTv.setText(getString(R.string.all));
        if (b) {
            mSecondCateTv.setTextColor(getColor(R.color.black_333333));
            mSecondCateTv.setOnClickListener(this::onSecondCateClicked);
        } else {
            mSecondCateTv.setTextColor(getColor(R.color.grey_BFBFBF));
            mSecondCateTv.setOnClickListener(null);
            mSecondArrowIv.setImageResource(R.mipmap.ic_store_down);
        }

        if (mToSelectedListFragment != null) {
            // 清空缓存数据
            mToSelectedListFragment.unSelectSecond();
            mToSelectedListFragment.unSelectThird();
        }
    }

    private void setThirdLevelEnable(boolean b) {
        mThirdCateTv.setText(getString(R.string.all));
        if (b) {
            mThirdCateTv.setTextColor(getColor(R.color.black_333333));
            mThirdCateTv.setOnClickListener(this::onThirdCateClicked);
        } else {
            changeLevelTitleStatus(mThirdCateTv, R.color.grey_BFBFBF, mThirdArrowIv, R.mipmap.ic_store_down);
            mThirdCateTv.setOnClickListener(null);
        }

        if (mToSelectedListFragment != null) {
            // 清空缓存数据
            mToSelectedListFragment.unSelectThird();
        }
    }

    /**
     * 分类列表点击回调
     *
     * @param level            当前是 全部-全部-全部，第几个级别的分类数据
     * @param categoryResponse 选中的分类对象
     */
    private void onCategorySelected(int level, CategoryResponse categoryResponse) {
        mDefaultChildCategoryId = null;
        hideCategoryListFragment();
        int childNum = categoryResponse.getChild_categories().size();
        ImageView tempArrowIv = null;
        switch (level) {
            case CategoryToSelectedListFragment.LEVEL_FIRST:
                mFirstCateTv.setText(categoryResponse.getName());
                tempArrowIv = mFirstArrowIv;
                setSecondLevelEnable(childNum > 0);
                setThirdLevelEnable(false);
                break;
            case CategoryToSelectedListFragment.LEVEL_SECOND:
                mSecondCateTv.setText(categoryResponse.getName());
                tempArrowIv = mSecondArrowIv;
                setThirdLevelEnable(childNum > 0);
                break;
            case CategoryToSelectedListFragment.LEVEL_THIRD:
                mThirdCateTv.setText(categoryResponse.getName());
                tempArrowIv = mThirdArrowIv;
                break;
        }
        if (tempArrowIv != null) {
            tempArrowIv.setImageResource(R.mipmap.ic_store_down);
        }
        page = Constant.DEFAULT_FIRST_PAGE;
        getMechanismDataFromNet(page, mToSelectedListFragment.getCurrentCategoryId(), "");
    }

    private void showCategoryListFragment() {
        if (!mToSelectedListFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().show(mToSelectedListFragment).commit();
        }
    }

    private void hideCategoryListFragment() {
        if (mToSelectedListFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mToSelectedListFragment).commit();
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yellow_page;
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }
}

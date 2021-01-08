package com.jcs.where.yellow_page.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.government.activity.MechanismDetailActivity;
import com.jcs.where.government.adapter.MechanismListAdapter;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.yellow_page.CategoryToSelectedListFragment;
import com.jcs.where.yellow_page.model.YellowPageModel;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.annotations.NonNull;

/**
 * 企业黄页
 * create by zyf on 2021/1/3 10:14 AM
 */
public class YellowPageActivity extends BaseActivity {
    public static final String K_ID = "id";
    public static final String K_CATEGORIES = "categories";
    public static final String K_DEFAULT_CHILD_CATEGORY_ID = "defaultChildCategoryId";

    private TextView mFirstCateTv, mSecondCateTv, mThirdCateTv;
    private ImageView mFirstArrowIv, mSecondArrowIv, mThirdArrowIv;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;

    private MechanismListAdapter mAdapter;
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

        setSecondLevelEnable(false);
        setThirdLevelEnable(false);

        mSwipeLayout = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.yellowPageRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MechanismListAdapter();
        mRecyclerView.setAdapter(mAdapter);

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
        getDataFromNativeOrNet();
    }

    private void getDataFromNativeOrNet() {
        Log.e("YellowPageActivity", "getDataFromNativeOrNet: " + "");
        String jsonStr = CacheUtil.needUpdateBySpKey(SPKey.K_YELLOW_PAGE_CATEGORIES);
        if (jsonStr.equals("")) {
            // 获取网路数据
            getInitYellowPage();
        } else {
            mFirstLevelCategories = JsonUtil.getInstance().fromJsonToList(jsonStr, new TypeToken<List<CategoryResponse>>() {
            }.getType());
            // 将分类数据注入分类选中Fragment中
            injectToSelectFragment();
            if (mDefaultChildCategoryId == null) {
                getMechanismDataFromNet(mCategories.toString(), "");
            } else {
                // 根据选中的分类，设置 mFirstCateTv  mSecondCateTv mThirdCateTv 的状态
                toChangeLevelTitleStatus();
                getMechanismDataFromNet(mDefaultChildCategoryId, "");
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
            public void onNext(@NonNull YellowPageModel.YellowPageZipResponse yellowPageZipResponse) {
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
                    CacheUtil.cacheWithCurrentTime(SPKey.K_YELLOW_PAGE_CATEGORIES, mFirstLevelCategories);
                }
                MechanismPageResponse mechanismPageResponse = yellowPageZipResponse.getMechanismPageResponse();
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
            changeLevelTitleStatus(mFirstCateTv, R.color.blue_4D9FF2, mFirstArrowIv, R.mipmap.ic_arrow_bottom_black);
            return;
        }
        mSecondCateTv.setText(selectSecondCate.getName());
        mSecondCateTv.setOnClickListener(this::onSecondCateClicked);
        mThirdCateTv.setOnClickListener(this::onThirdCateClicked);
        changeLevelTitleStatus(mFirstCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_arrow_bottom_black);
        changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_arrow_bottom_black);
        CategoryResponse selectThirdCate = mToSelectedListFragment.getSelectThirdCate();
        if (selectThirdCate == null) {
            changeLevelTitleStatus(mSecondCateTv, R.color.blue_4D9FF2, mSecondArrowIv, R.mipmap.ic_arrow_bottom_black);
            return;
        }

        mThirdCateTv.setText(selectThirdCate.getName());
        changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mSecondArrowIv, R.mipmap.ic_arrow_bottom_black);
        changeLevelTitleStatus(mThirdCateTv, R.color.blue_4B9DF1, mThirdArrowIv, R.mipmap.ic_arrow_bottom_black);
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

    private void getMechanismDataFromNet(String categoryId, String search) {
        mModel.getMechanismList(categoryId, search, new BaseObserver<MechanismPageResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull MechanismPageResponse mechanismPageResponse) {
                mSwipeLayout.setRefreshing(false);
                mAdapter.getData().clear();
                updateAdapter(mechanismPageResponse);
            }
        });
    }

    private void updateAdapter(@NonNull MechanismPageResponse mechanismPageResponse) {
        List<MechanismResponse> data = mechanismPageResponse.getData();
        if (data != null && data.size() > 0) {
            mAdapter.addData(data);
        } else {
            mAdapter.notifyDataSetChanged();
            mAdapter.setEmptyView(R.layout.view_empty_data_brvah_mechanism);
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

    private void onMechanismItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        MechanismResponse mechanismResponse = mAdapter.getData().get(position);
        toActivity(MechanismDetailActivity.class, new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, String.valueOf(mechanismResponse.getId())));
    }

    private void onSwipeRefresh() {
        if (mToSelectedListFragment.isNoLevel()) {
            // 获得全部数据
            getMechanismDataFromNet(mFirstLevelCategories.toString(), "");
        } else {
            // 根据当前分类id刷新数据
            String id = mToSelectedListFragment.getCurrentCategoryId();
            getMechanismDataFromNet(id, "");
        }
    }

    public void onFirstCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isFirstLevel()) {
            changeLevelTitleStatus(mFirstCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_arrow_bottom_black);
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isFirstLevel()) {
                // 如果选项列表数据不是一级分类数据，则设置为一级分类数据
                mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
                CategoryResponse selectFirstCate = mToSelectedListFragment.getSelectFirstCate();
                if (selectFirstCate != null && selectFirstCate.getChild_categories().size() > 0) {
                    changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mSecondArrowIv, R.mipmap.ic_arrow_bottom_black);
                } else {
                    setThirdLevelEnable(false);
                }

                CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
                if (selectSecondCate != null && selectSecondCate.getChild_categories().size() > 0) {
                    changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_arrow_bottom_black);
                } else {
                    setThirdLevelEnable(false);
                }
            } else {
                mToSelectedListFragment.notifyAdapter();
            }
            changeLevelTitleStatus(mFirstCateTv, R.color.blue_4D9FF2, mFirstArrowIv, R.mipmap.ic_arrow_top_blue);
            showCategoryListFragment();
        }
    }

    public void onSecondCateClicked(View view) {
        Log.e("YellowPageActivity", "onSecondCateClicked: " + "");
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isSecondLevel()) {
            changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mSecondArrowIv, R.mipmap.ic_arrow_bottom_black);
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isSecondLevel()) {
                // 如果选项列表数据不是二级分类数据，则设置为二级分类数据
                CategoryResponse firstSelected = mFirstLevelCategories.get(mToSelectedListFragment.getSelectFirstPosition());
                mToSelectedListFragment.setData(firstSelected.getChild_categories(), CategoryToSelectedListFragment.LEVEL_SECOND);
                changeLevelTitleStatus(mFirstCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_arrow_bottom_black);
                CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
                if (selectSecondCate != null && selectSecondCate.getChild_categories().size() > 0) {
                    changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_arrow_bottom_black);
                } else {
                    setThirdLevelEnable(false);
                }
            } else {
                // 不刷新的话，选中状态的 对勾 icon 无法显示
                mToSelectedListFragment.notifyAdapter();
            }
            changeLevelTitleStatus(mSecondCateTv, R.color.blue_4D9FF2, mSecondArrowIv, R.mipmap.ic_arrow_top_blue);
            showCategoryListFragment();
        }
    }

    public void onThirdCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isThirdLevel()) {
            hideCategoryListFragment();
            changeLevelTitleStatus(mThirdCateTv, R.color.black_333333, mThirdArrowIv, R.mipmap.ic_arrow_bottom_black);
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isThirdLevel()) {
                // 如果选项列表数据不是三级分类数据，则设置为三级分类数据
                CategoryResponse firstSelected = mFirstLevelCategories.get(mToSelectedListFragment.getSelectFirstPosition());
                CategoryResponse secondSelected = firstSelected.getChild_categories().get(mToSelectedListFragment.getSelectSecondPosition());
                mToSelectedListFragment.setData(secondSelected.getChild_categories(), CategoryToSelectedListFragment.LEVEL_THIRD);
                mFirstCateTv.setTextColor(getColor(R.color.black_333333));
                changeLevelTitleStatus(mSecondCateTv, R.color.black_333333, mFirstArrowIv, R.mipmap.ic_arrow_bottom_black);
                mSecondArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
            } else {
                // 不刷新的话，选中状态的 对勾 icon 无法显示
                mToSelectedListFragment.notifyAdapter();
            }
            changeLevelTitleStatus(mThirdCateTv, R.color.blue_4D9FF2, mThirdArrowIv, R.mipmap.ic_arrow_top_blue);
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
            mSecondArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
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
            changeLevelTitleStatus(mThirdCateTv, R.color.grey_BFBFBF, mThirdArrowIv, R.mipmap.ic_arrow_bottom_black);
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
            tempArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
        }
        getMechanismDataFromNet(mToSelectedListFragment.getCurrentCategoryId(), "");
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

}

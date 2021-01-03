package com.jcs.where.yellow_page.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.adapter.MechanismListAdapter;
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
    private int mId;
    private List<Integer> mCategories;
    private Intent mIntent;
    private List<CategoryResponse> mFirstLevelCategories;
    private List<CategoryResponse> mSecondLevelCategories;
    private List<CategoryResponse> mThirdLevelCategories;


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

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mToSelectedListFragment = (CategoryToSelectedListFragment) supportFragmentManager.findFragmentById(R.id.categoryFragment);
        mToSelectedListFragment.setListener(this::onCategorySelected);
        supportFragmentManager.beginTransaction().hide(mToSelectedListFragment).commit();
    }

    @Override
    protected void initData() {
        mModel = new YellowPageModel();
        mAdapter = new MechanismListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mIntent = getIntent();

        // 获取id
        initId();

        // 获取企业黄页的下级分类id
        initCategories();

        // 获取网路数据
        getInitYellowPage();
    }

    /**
     * 一次性获取当前页面默认展示的数据
     * 分类，对应企业黄页下所有分类的数据列表
     */
    private void getInitYellowPage() {
        showLoading();
        mModel.getInitData(mCategories, new BaseObserver<YellowPageModel.YellowPageZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull YellowPageModel.YellowPageZipResponse yellowPageZipResponse) {
                stopLoading();
                List<CategoryResponse> categories = yellowPageZipResponse.getCategories();
                mFirstLevelCategories = categories;
                mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
                MechanismPageResponse mechanismPageResponse = yellowPageZipResponse.getMechanismPageResponse();
                updateAdapter(mechanismPageResponse);
            }
        });
    }

    private void getCategoriesFromNet(int categoryLevel, int parentLevel, int clickedLevel) {
        showLoading();
        mModel.getCategories(categoryLevel, getCurrentCategoryId(), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                stopLoading();
                if (parentLevel == CategoryToSelectedListFragment.LEVEL_FIRST) {
                    mSecondLevelCategories = categoryResponses;
                }

                if (parentLevel == CategoryToSelectedListFragment.LEVEL_SECOND) {
                    mThirdLevelCategories = categoryResponses;
                }
                mToSelectedListFragment.setData(categoryResponses, clickedLevel);
                showCategoryListFragment();
            }
        });
    }

    /**
     * 获得当前分类id
     *
     * @return 分类id字符串，可能是 "1" "[1,2,3]"
     */
    private String getCurrentCategoryId() {
        String id = "";
        // 根据当前level 和 选择 level 的分类id，获取数据
        if (mToSelectedListFragment.isFirstLevel()) {
            int selectFirst = mToSelectedListFragment.getSelectFirst();
            id = String.valueOf(mFirstLevelCategories.get(selectFirst).getId());
        }

        if (mToSelectedListFragment.isSecondLevel()) {
            int selectSecond = mToSelectedListFragment.getSelectSecond();
            id = String.valueOf(mSecondLevelCategories.get(selectSecond).getId());
        }

        if (mToSelectedListFragment.isThirdLevel()) {
            int selectThird = mToSelectedListFragment.getSelectThird();
            id = String.valueOf(mThirdLevelCategories.get(selectThird).getId());
        }
        return id;
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
        }
    }

    private void initCategories() {
        mCategories = mIntent.getIntegerArrayListExtra(K_CATEGORIES);
    }

    private void initId() {
        String idStr = mIntent.getStringExtra(K_ID);
        if (idStr == null || idStr.isEmpty()) {
            mId = 0;
        } else {
            try {
                mId = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                mId = 0;
            }
        }
    }

    @Override
    protected void bindListener() {
        mFirstCateTv.setOnClickListener(this::onFirstCateClicked);
        mSecondCateTv.setOnClickListener(this::onSecondCateClicked);
        mThirdCateTv.setOnClickListener(this::onThirdCateClicked);

        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);
    }

    private void onSwipeRefresh() {
        if (mToSelectedListFragment.isNoLevel()) {
            // 获得全部数据
            getMechanismDataFromNet(mFirstLevelCategories.toString(), "");
        } else {
            // 根据当前分类id刷新数据
            String id = getCurrentCategoryId();
            getMechanismDataFromNet(id, "");
        }
    }

    public void onFirstCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isFirstLevel()) {
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isFirstLevel()) {
                // 如果选项列表数据不是一级分类数据，则设置为一级分类数据
                mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
            } else {
                mToSelectedListFragment.notifyAdapter();
            }
            setSecondLevelEnable(true);
            showCategoryListFragment();
        }
    }

    public void onSecondCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isSecondLevel()) {
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isSecondLevel()) {
                // 如果选项列表数据不是二级分类数据，则设置为二级分类数据
                if (mSecondLevelCategories == null) {
                    // 根据前置条件（一级数据id），获得二级分类数据
                    getCategoriesFromNet(
                            2,
                            CategoryToSelectedListFragment.LEVEL_FIRST,
                            CategoryToSelectedListFragment.LEVEL_SECOND);
                } else {
                    mToSelectedListFragment.setData(mSecondLevelCategories, CategoryToSelectedListFragment.LEVEL_SECOND);
                }
            } else {
                mToSelectedListFragment.notifyAdapter();
                showCategoryListFragment();
            }
            setThirdLevelEnable(true);
        }
    }

    public void onThirdCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isThirdLevel()) {
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isThirdLevel()) {
                // 如果选项列表数据不是三级分类数据，则设置为三级分类数据
                if (mThirdLevelCategories == null) {
                    // 根据前置条件（二级数据id），获得三级分类数据
                    getCategoriesFromNet(
                            3,
                            CategoryToSelectedListFragment.LEVEL_SECOND,
                            CategoryToSelectedListFragment.LEVEL_THIRD);
                } else {
                    mToSelectedListFragment.setData(mThirdLevelCategories, CategoryToSelectedListFragment.LEVEL_THIRD);
                }
            } else {
                mToSelectedListFragment.notifyAdapter();
                showCategoryListFragment();
            }
        }
    }

    private void setSecondLevelEnable(boolean b) {
        mSecondCateTv.setEnabled(b);
        mSecondArrowIv.setEnabled(b);

        mSecondCateTv.setText(getString(R.string.all));
        mThirdCateTv.setText(getString(R.string.all));

        if (mToSelectedListFragment != null) {
            // 清空缓存数据
            if (mSecondLevelCategories != null) {
                mSecondLevelCategories = null;
                mToSelectedListFragment.unSelectSecond();
            }
            if (mThirdLevelCategories != null) {
                mThirdLevelCategories = null;
                mToSelectedListFragment.unSelectThird();
            }
        }
    }

    private void setThirdLevelEnable(boolean b) {
        mThirdCateTv.setEnabled(b);
        mThirdArrowIv.setEnabled(b);
        mThirdCateTv.setText(getString(R.string.all));

        if (mToSelectedListFragment != null && mThirdLevelCategories != null) {
            // 清空缓存数据
            mThirdLevelCategories = null;
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
        hideCategoryListFragment();
        switch (level) {
            case CategoryToSelectedListFragment.LEVEL_FIRST:
                mFirstCateTv.setText(categoryResponse.getName());
                break;
            case CategoryToSelectedListFragment.LEVEL_SECOND:
                mSecondCateTv.setText(categoryResponse.getName());
                break;
            case CategoryToSelectedListFragment.LEVEL_THIRD:
                mThirdCateTv.setText(categoryResponse.getName());
                break;
        }
        String categoryId = String.valueOf(categoryResponse.getId());
        getMechanismDataFromNet(categoryId, "");
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
    protected int getLayoutId() {
        return R.layout.activity_yellow_page;
    }


}

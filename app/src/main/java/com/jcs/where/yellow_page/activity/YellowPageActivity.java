package com.jcs.where.yellow_page.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.adapter.MechanismListAdapter;
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

    private TextView mFirstCateTv, mSecondCateTv, mThirdCateTv;
    private ImageView mFirstArrowIv, mSecondArrowIv, mThirdArrowIv;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;

    private MechanismListAdapter mAdapter;
    private YellowPageModel mModel;

    private CategoryToSelectedListFragment mToSelectedListFragment;

    /**
     * 默认存储分类数据一天
     */
    private final long CATEGORY_SAVE_TIME = 60 * 1000 * 60 * 24;

    /**
     * 企业黄页对应的分类id
     */
    private int mId;
    private List<Integer> mCategories;
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

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mToSelectedListFragment = (CategoryToSelectedListFragment) supportFragmentManager.findFragmentById(R.id.categoryFragment);
        if (mToSelectedListFragment != null) {
            mToSelectedListFragment.setListener(this::onCategorySelected);
        }
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

        // 从本地或网络获取数据
        getDataFromNativeOrNet();
    }

    private void getDataFromNativeOrNet() {
        String jsonData = SPUtil.getInstance().getString(SPKey.K_YELLOW_PAGE_CATEGORIES);
        long savedTime = 0;
        String jsonStr = "";
        try {
            if (jsonData != null) {
                String[] strArray = jsonData.split("_____");
                savedTime = Long.parseLong(strArray[1]);
                jsonStr = strArray[0];
            }
        } catch (Exception e) {
            Log.e("YellowPageActivity", "initData: " + e.getMessage());
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - savedTime >= CATEGORY_SAVE_TIME) {
            // 获取网路数据
            getInitYellowPage();
        } else {
            mFirstLevelCategories = JsonUtil.getInstance().fromJsonToList(jsonStr, new TypeToken<List<CategoryResponse>>() {
            }.getType());
            // 将分类数据注入分类选中Fragment中
            injectToSelectFragment();
            getMechanismDataFromNet(mCategories.toString(), "");
        }
    }

    /**
     * 一次性获取当前页面默认展示的数据
     * 分类，对应企业黄页下所有分类的数据列表
     */
    private void getInitYellowPage() {
        showLoading();
        String categoryIds = mCategories.toString();
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
                // 将分类数据注入分类选中Fragment中
                injectToSelectFragment();

                String value = JsonUtil.getInstance().toJsonStr(mFirstLevelCategories);
                String valueWithTime = value + "_____" + System.currentTimeMillis();
                SPUtil.getInstance().saveString(SPKey.K_YELLOW_PAGE_CATEGORIES, valueWithTime);
                MechanismPageResponse mechanismPageResponse = yellowPageZipResponse.getMechanismPageResponse();
                updateAdapter(mechanismPageResponse);
            }
        });
    }

    /**
     * 将分类数据注入分类选中Fragment中
     */
    private void injectToSelectFragment() {
        mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
        mToSelectedListFragment.setTotalCategories(mFirstLevelCategories);
        mToSelectedListFragment.setFirstLevelTotalIds(mCategories.toString());
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

        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);
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
            mFirstCateTv.setTextColor(getColor(R.color.black_333333));
            mFirstArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isFirstLevel()) {
                // 如果选项列表数据不是一级分类数据，则设置为一级分类数据
                mToSelectedListFragment.setData(mFirstLevelCategories, CategoryToSelectedListFragment.LEVEL_FIRST);
                CategoryResponse selectFirstCate = mToSelectedListFragment.getSelectFirstCate();
                if (selectFirstCate != null && selectFirstCate.getChild_categories().size() > 0) {
                    mSecondCateTv.setTextColor(getColor(R.color.black_333333));
                    mSecondArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
                } else {
                    setThirdLevelEnable(false);
                }

                CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
                if (selectSecondCate != null && selectSecondCate.getChild_categories().size() > 0) {
                    mThirdCateTv.setTextColor(getColor(R.color.black_333333));
                    mThirdArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
                } else {
                    setThirdLevelEnable(false);
                }
            } else {
                mToSelectedListFragment.notifyAdapter();
            }
            mFirstCateTv.setTextColor(getColor(R.color.blue_4D9FF2));
            mFirstArrowIv.setImageResource(R.mipmap.ic_arrow_top_blue);
            showCategoryListFragment();
        }
    }

    public void onSecondCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isSecondLevel()) {
            mSecondCateTv.setTextColor(getColor(R.color.black_333333));

            mSecondArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
            hideCategoryListFragment();
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isSecondLevel()) {
                // 如果选项列表数据不是二级分类数据，则设置为二级分类数据
                CategoryResponse firstSelected = mFirstLevelCategories.get(mToSelectedListFragment.getSelectFirstPosition());
                mToSelectedListFragment.setData(firstSelected.getChild_categories(), CategoryToSelectedListFragment.LEVEL_SECOND);
                mFirstCateTv.setTextColor(getColor(R.color.black_333333));
                mFirstArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
                CategoryResponse selectSecondCate = mToSelectedListFragment.getSelectSecondCate();
                if (selectSecondCate != null && selectSecondCate.getChild_categories().size() > 0) {
                    mThirdCateTv.setTextColor(getColor(R.color.black_333333));
                    mThirdArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
                } else {
                    setThirdLevelEnable(false);
                }
            } else {
                // 不刷新的话，选中状态的 对勾 icon 无法显示
                mToSelectedListFragment.notifyAdapter();
            }
            mSecondCateTv.setTextColor(getColor(R.color.blue_4D9FF2));
            mSecondArrowIv.setImageResource(R.mipmap.ic_arrow_top_blue);
            showCategoryListFragment();
        }
    }

    public void onThirdCateClicked(View view) {
        if (mToSelectedListFragment.isVisible() && mToSelectedListFragment.isThirdLevel()) {
            hideCategoryListFragment();
            mThirdCateTv.setTextColor(getColor(R.color.black_333333));
            mThirdArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
        } else {
            // 如果选项列表未显示
            if (!mToSelectedListFragment.isThirdLevel()) {
                // 如果选项列表数据不是三级分类数据，则设置为三级分类数据
                CategoryResponse firstSelected = mFirstLevelCategories.get(mToSelectedListFragment.getSelectFirstPosition());
                CategoryResponse secondSelected = firstSelected.getChild_categories().get(mToSelectedListFragment.getSelectSecondPosition());
                mToSelectedListFragment.setData(secondSelected.getChild_categories(), CategoryToSelectedListFragment.LEVEL_THIRD);
                mFirstCateTv.setTextColor(getColor(R.color.black_333333));
                mSecondCateTv.setTextColor(getColor(R.color.black_333333));
                mFirstArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
                mSecondArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
            } else {
                // 不刷新的话，选中状态的 对勾 icon 无法显示
                mToSelectedListFragment.notifyAdapter();
            }
            mThirdCateTv.setTextColor(getColor(R.color.blue_4D9FF2));
            mThirdArrowIv.setImageResource(R.mipmap.ic_arrow_top_blue);
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
            mThirdCateTv.setTextColor(getColor(R.color.grey_BFBFBF));
            mThirdArrowIv.setImageResource(R.mipmap.ic_arrow_bottom_black);
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

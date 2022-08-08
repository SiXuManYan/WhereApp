package com.jcs.where.government.fragment;

import static com.jcs.where.utils.Constant.PARAM_ID;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.features.map.MechanismAdapter;
import com.jcs.where.features.mechanism.MechanismActivity;
import com.jcs.where.government.model.MechanismListModel;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.list.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 机构列表
 * create by zyf on 2020/12/28 9:07 PM
 */
public class MechanismListFragment extends BaseFragment implements OnLoadMoreListener {
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecycler;
    private RadioGroup mRadioGroup;
    private HorizontalScrollView mHScrollView;

    private MechanismAdapter mAdapter;
    private MechanismListModel mModel;
    private CategoryResponse mCategoryResponse;
    private List<CategoryResponse> mChildCategories;
    /**
     * 第一次展示时获取网络数据
     * 此 tag 表示是否已经获取过网络数据
     */
    private boolean mIsDataLoaded = false;

    /**
     * 是否是ViewPager中的第一个Fragment
     * 第一个Fragment在onResume中加载数据时会导致
     * PopupLayout弹出时略微卡顿，所以第一个Fragment不在onResume中加载数据
     * 而是在 《PopupLayout弹出后》 手动调用方法获取数据
     */
    private boolean mIsFirstFragment = false;
    /**
     * 当前展示的数据是属于哪个分类的
     */
    private String mCurrentCategoryId;
    private static final String KEY_CATEGORY_RESPONSE = "categoryResponse";
    private static final String KEY_IS_FIRST_FRAGMENT = "isFirstFragment";

    private EmptyView emptyView;


    private int page = Constant.DEFAULT_FIRST_PAGE;

    public static MechanismListFragment newInstance(CategoryResponse category) {
        Bundle args = new Bundle();
        return deployArgs(category, args);
    }

    public static MechanismListFragment newInstance(CategoryResponse category, boolean isFirst) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_FIRST_FRAGMENT, isFirst);
        return deployArgs(category, args);
    }

    private static MechanismListFragment deployArgs(CategoryResponse category, Bundle args) {
        args.putSerializable(KEY_CATEGORY_RESPONSE, category);
        MechanismListFragment fragment = new MechanismListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.mechanismRefresh);
        mRecycler = view.findViewById(R.id.mechanismRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRadioGroup = view.findViewById(R.id.mechanismRadioGroup);
        mHScrollView = view.findViewById(R.id.radioScroll);
        mHScrollView.setVisibility(View.GONE);


    }

    @Override
    protected void initData() {
        mModel = new MechanismListModel();

        emptyView = new EmptyView(getActivity());
        emptyView.showEmptyDefault();
        addEmptyList(emptyView);


        mAdapter = new MechanismAdapter();
        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0));


        mChildCategories = new ArrayList<>();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsFirstFragment = arguments.getBoolean(KEY_IS_FIRST_FRAGMENT);
            mCategoryResponse = (CategoryResponse) arguments.getSerializable(KEY_CATEGORY_RESPONSE);
            mCurrentCategoryId = String.valueOf(mCategoryResponse.getId());
        }

        if (mIsFirstFragment) {
            getNetData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstFragment) {
            getNetData();
        }
    }

    public void getNetData() {
        Bundle arguments = getArguments();
        if (arguments != null && !mIsDataLoaded) {
            getChildCategory();
            onSwipeRefresh();
            mIsDataLoaded = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 获得当前分类下的子分类
     */
    private void getChildCategory() {
        Log.e("MechanismListFragment", "getChildCategory: " + "mCurrentCategoryId=" + mCurrentCategoryId + "----mCategoryResponse.getId()=" + mCategoryResponse.getId());
        mModel.getChildCategories(3, mCategoryResponse.getId(), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull List<CategoryResponse> categoryResponses) {
                RadioButton allRadio = (RadioButton) mRadioGroup.getChildAt(0);
                mChildCategories.clear();
                mChildCategories.addAll(categoryResponses);
                int size = categoryResponses.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        if (i == 0) {
                            mRadioGroup.setVisibility(View.VISIBLE);
                            mHScrollView.setVisibility(View.VISIBLE);
                        }
                        RadioButton temp = new RadioButton(getContext());
                        CategoryResponse categoryResponse = categoryResponses.get(i);
                        temp.setButtonDrawable(null);
                        temp.setBackgroundResource(R.drawable.selector_store_third_category);
                        temp.setText(categoryResponse.getName());
                        temp.setTextColor(getResources().getColorStateList(R.color.selector_grey_blue, null));
                        temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                        temp.setLayoutParams(allRadio.getLayoutParams());
                        // 使用分类id作为RadioButton的id
                        temp.setId(Integer.parseInt(categoryResponse.getId()));
                        temp.setPadding(allRadio.getPaddingLeft(), allRadio.getPaddingTop(), allRadio.getPaddingRight(), allRadio.getPaddingBottom());
                        mRadioGroup.addView(temp, mRadioGroup.getChildCount());
                    }

                } else {
                    mHScrollView.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 根据分类id获取机构信息
     *
     * @param categoryId 分类id
     */
    private void getMechanismList(String categoryId) {
        mCurrentCategoryId = categoryId;

        mModel.getMechanismList(page, categoryId, new BaseObserver<PageResponse<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull PageResponse<MechanismResponse> response) {
                mSwipeLayout.setRefreshing(false);
                List<MechanismResponse> data = response.getData();
                boolean isLastPage = response.getLastPage() == page;

                BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                if (data.isEmpty() || data == null) {
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        mAdapter.setNewInstance(null);
                        loadMoreModule.loadMoreComplete();
                        emptyView.showEmptyContainer();
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
                    if (isLastPage) {
                        loadMoreModule.loadMoreEnd();
                    } else {
                        loadMoreModule.loadMoreComplete();
                    }
                }


            }
        });
    }

    @Override
    protected void bindListener() {
        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);
        mRadioGroup.setOnCheckedChangeListener(this::onRadioChecked);
        mAdapter.setOnItemClickListener(this::onMechanismItemClicked);
    }

    private void onMechanismItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        int mechanismId = mAdapter.getData().get(position).getId();
        Bundle b = new Bundle();
        b.putInt(PARAM_ID, mechanismId);
        startActivity(MechanismActivity.class, b);

    }

    /**
     * radioButton 的选择事件
     *
     * @param i radioButton 的id
     */
    private void onRadioChecked(RadioGroup radioGroup, int i) {
        // 当前不处于刷新状态才可以执行操作
        // 防止连续多次点击或者网速慢在刷新的点击
        if (!mSwipeLayout.isRefreshing()) {
            if (i == R.id.allRadio) {
                // 选择了全部
                mCurrentCategoryId = String.valueOf(mCategoryResponse.getId());
                onSwipeRefresh();
            } else {
                // 选择了其他的子分类
                int size = mChildCategories.size();
                for (int j = 0; j < size; j++) {
                    CategoryResponse categoryResponse = mChildCategories.get(j);
                    int categoryId = Integer.parseInt(categoryResponse.getId());
                    if (i == categoryId) {
                        mCurrentCategoryId = String.valueOf(categoryResponse.getId());
                        onSwipeRefresh();
                    }
                }
            }
        }

    }

    private void onSwipeRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;
        getMechanismList(mCurrentCategoryId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mechanism_list;
    }

    @Override
    public void onLoadMore() {
        page++;
        getMechanismList(mCurrentCategoryId);
    }
}

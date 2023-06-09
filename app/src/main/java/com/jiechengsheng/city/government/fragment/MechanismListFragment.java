package com.jiechengsheng.city.government.fragment;

import static com.jiechengsheng.city.utils.Constant.PARAM_ID;

import android.graphics.Color;
import android.os.Bundle;
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
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.response.CategoryResponse;
import com.jiechengsheng.city.api.response.MechanismResponse;
import com.jiechengsheng.city.api.response.PageResponse;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.BaseFragment;
import com.jiechengsheng.city.base.EventCode;
import com.jiechengsheng.city.features.map.MechanismAdapter;
import com.jiechengsheng.city.features.mechanism.MechanismActivity;
import com.jiechengsheng.city.government.model.MechanismListModel;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.view.empty.EmptyView;
import com.jiechengsheng.city.widget.list.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    /**
     * 1 推荐
     * 0 距离最近
     */
    private Integer recommend = 1;

    private boolean needRefresh = false;


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
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }


        mSwipeLayout = view.findViewById(R.id.mechanismRefresh);
        mRecycler = view.findViewById(R.id.mechanismRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRadioGroup = view.findViewById(R.id.mechanismRadioGroup);
        mHScrollView = view.findViewById(R.id.radioScroll);
        mHScrollView.setVisibility(View.GONE);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }

    }

    @Override
    protected void initData() {
        mModel = new MechanismListModel();

        emptyView = new EmptyView(getActivity());
        emptyView.showEmptyDefault();
        addEmptyList(emptyView);


        mAdapter = new MechanismAdapter();
        mAdapter.setEmptyView(emptyView);
        BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
        loadMoreModule.setAutoLoadMore(true);
        loadMoreModule.setEnableLoadMoreIfNotFullPage(false);
        loadMoreModule.setOnLoadMoreListener(this);

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


    /**
     * 获得当前分类下的子分类
     */
    private void getChildCategory() {
        mModel.getChildCategories(3, mCategoryResponse.getId(), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
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

        mModel.getMechanismList(page, categoryId, recommend, new BaseObserver<PageResponse<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
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
     * @param checkedId radioButton 的id
     */
    private void onRadioChecked(RadioGroup radioGroup, int checkedId) {
        // 当前不处于刷新状态才可以执行操作
        // 防止连续多次点击或者网速慢在刷新的点击
        if (!mSwipeLayout.isRefreshing()) {
            if (checkedId == R.id.allRadio) {
                // 选择了全部
                mCurrentCategoryId = String.valueOf(mCategoryResponse.getId());
                onSwipeRefresh();
            } else {
                // 选择了其他的子分类
                int size = mChildCategories.size();
                for (int j = 0; j < size; j++) {
                    CategoryResponse categoryResponse = mChildCategories.get(j);
                    int categoryId = Integer.parseInt(categoryResponse.getId());
                    if (checkedId == categoryId) {
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
        needRefresh = false;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

        if (baseEvent.code == EventCode.EVENT_REFRESH_CONVENIENCE_CHILD) {

            int newRecommend = (int) baseEvent.data;

            if (recommend != newRecommend) {
                recommend = newRecommend;
                needRefresh = true;
                if (isViewVisible) {
                    onSwipeRefresh();
                }
            }

        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && needRefresh) {
            onSwipeRefresh();
        }
    }

}


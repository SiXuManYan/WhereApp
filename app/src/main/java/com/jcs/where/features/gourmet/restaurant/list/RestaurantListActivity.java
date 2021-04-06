package com.jcs.where.features.gourmet.restaurant.list;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.api.response.category.Category;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.bean.RestaurantListRequest;
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity;
import com.jcs.where.features.gourmet.restaurant.list.filter.more.MoreFilterFragment;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.list.DividerDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.jcs.where.utils.Constant.PARAM_ID;

/**
 * Created by Wangsw  2021/3/24 13:56.
 * 餐厅列表
 */
public class RestaurantListActivity extends BaseMvpActivity<RestaurantListPresenter> implements RestaurantListView, OnLoadMoreListener, OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {


    private int page = Constant.DEFAULT_FIRST_PAGE;

    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler;
    private LinearLayout
            category_ll,
            area_filter_ll,
            food_filter_ll,
            other_filter_ll,
            filter_container_ll;
    private ViewPager filter_pager;
    private View dismiss_view;
    private AppCompatEditText city_et;


    private RestaurantListAdapter mAdapter;
    private RestaurantListRequest mRequest;
    private Animation mFilterShowAnimation;
    private Animation mFilterHideAnimation;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_gourmet_list;
    }

    @Override
    protected void initView() {
        swipe_layout = findViewById(R.id.swipe_layout);
        recycler = findViewById(R.id.recycler);
        city_et = findViewById(R.id.cityEt);


        // filter
        category_ll = findViewById(R.id.category_ll);
        area_filter_ll = findViewById(R.id.area_filter_ll);
        food_filter_ll = findViewById(R.id.food_filter_ll);
        other_filter_ll = findViewById(R.id.other_filter_ll);

        filter_container_ll = findViewById(R.id.filter_container_ll);
        filter_pager = findViewById(R.id.filter_pager);
        dismiss_view = findViewById(R.id.dismiss_view);
        filter_pager.setOffscreenPageLimit(2);
        filter_pager.setAdapter(new RestaurantPagerAdapter(getSupportFragmentManager(), 0));

        // list
        mAdapter = new RestaurantListAdapter();
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.addChildClickViewIds(R.id.take_ll);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(getItemDecoration());

        // 动画
        mFilterShowAnimation = AnimationUtils.loadAnimation(this, R.anim.filter_in);
        mFilterHideAnimation = AnimationUtils.loadAnimation(this, R.anim.filter_out);

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        presenter = new RestaurantListPresenter(this);
        mRequest = new RestaurantListRequest();
        onRefresh();
    }

    @Override
    protected void bindListener() {
        swipe_layout.setOnRefreshListener(this);
        findViewById(R.id.back_iv).setOnClickListener(v -> finish());
        findViewById(R.id.clearIv).setOnClickListener(this::onClearSearchClick);
        area_filter_ll.setOnClickListener(this::onAreaFilterClick);
        food_filter_ll.setOnClickListener(this::onFoodFilterClick);
        other_filter_ll.setOnClickListener(this::onOtherFilterClick);
        dismiss_view.setOnClickListener(this::onFilterDismissClick);
        filter_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < category_ll.getChildCount(); i++) {
                    changeFilterTabStyle(position, i);
                }

            }
        });
        city_et.setOnEditorActionListener((v, actionId, event) -> {
            String searchKey = city_et.getText().toString().trim();

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mRequest.search_input = searchKey;
                onRefresh();
                KeyboardUtils.hideSoftInput(city_et);
                return true;
            }

            return false;
        });

    }

    private void onClearSearchClick(View view) {
        city_et.setText("");
        if (!TextUtils.isEmpty(mRequest.search_input)) {
            mRequest.search_input = null;
            onRefresh();
        }
    }


    @Override
    public void onRefresh() {
        swipe_layout.setRefreshing(true);
        page = Constant.DEFAULT_FIRST_PAGE;
        presenter.getList(page, mRequest);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.getList(page, mRequest);
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        RestaurantResponse data = mAdapter.getData().get(position);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ID, data.id);
        startActivityAfterLogin(RestaurantDetailActivity.class, bundle);
    }

    @Override
    public void bindList(List<RestaurantResponse> data, boolean isLastPage) {
        if (swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(false);
        }

        BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
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
            if (isLastPage) {
                loadMoreModule.loadMoreEnd();
            } else {
                loadMoreModule.loadMoreComplete();
            }
        }
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }


    /**
     * 区域筛选
     */
    private void onAreaFilterClick(View view) {
        switchFilterPager(0);
    }

    /**
     * 美食类型筛选
     */
    private void onFoodFilterClick(View view) {
        switchFilterPager(1);
    }

    /**
     * 其他筛选
     */
    private void onOtherFilterClick(View view) {
        switchFilterPager(2);
    }

    private void onFilterDismissClick(View view) {
        handleFilterVisible(mFilterHideAnimation, View.GONE);

        // 清空tab选中状态
        LinearLayout childTabLL = (LinearLayout) category_ll.getChildAt(filter_pager.getCurrentItem());
        CheckedTextView tabText = (CheckedTextView) childTabLL.getChildAt(0);
        ImageView tabImage = (ImageView) childTabLL.getChildAt(1);
        tabText.setChecked(false);
        tabImage.setImageResource(R.mipmap.ic_arrow_filter_black);
    }


    private void switchFilterPager(int index) {
        int currentItem = filter_pager.getCurrentItem();
        if (filter_container_ll.getVisibility() == View.GONE) {

            handleFilterVisible(mFilterShowAnimation, View.VISIBLE);
            // 切换tab状态
            changeFilterTabStyle(currentItem, index);
        } else {

            if (currentItem == index) {

                handleFilterVisible(mFilterHideAnimation, View.GONE);

                // 清空tab选中状态
                LinearLayout childTabLL = (LinearLayout) category_ll.getChildAt(index);
                CheckedTextView tabText = (CheckedTextView) childTabLL.getChildAt(0);
                ImageView tabImage = (ImageView) childTabLL.getChildAt(1);
                tabText.setChecked(false);
                tabImage.setImageResource(R.mipmap.ic_arrow_filter_black);

            }
        }
        filter_pager.setCurrentItem(index, true);
    }

    private void handleFilterVisible(Animation mFilterHideAnimation, int gone) {
//        filter_container_ll.startAnimation(mFilterHideAnimation);
        filter_container_ll.setVisibility(gone);
    }

    private void changeFilterTabStyle(int pagerPosition, int tabIndex) {
        LinearLayout childTabLL = (LinearLayout) category_ll.getChildAt(tabIndex);
        CheckedTextView tabText = (CheckedTextView) childTabLL.getChildAt(0);
        ImageView tabImage = (ImageView) childTabLL.getChildAt(1);
        if (pagerPosition == tabIndex) {
            tabText.setChecked(true);
            tabImage.setImageResource(R.mipmap.ic_arrow_filter_blue);
        } else {
            tabText.setChecked(false);
            tabImage.setImageResource(R.mipmap.ic_arrow_filter_black);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {
        Object data = baseEvent.data;

        if (data instanceof AreaResponse) {
            // 区域筛选
            mRequest.trading_area_id = ((AreaResponse) data).id;

        } else if (data instanceof Category) {
            // 美食列别筛选
            mRequest.category_id = ((Category) data).id;


        } else if (data instanceof MoreFilterFragment.MoreFilter) {
            // 其他筛选
            MoreFilterFragment.MoreFilter more = (MoreFilterFragment.MoreFilter) data;
            mRequest.per_price = more.per_price;
            mRequest.service = more.service;
            mRequest.sort = more.sort;
        }

        onRefresh();
        dismiss_view.performClick();
    }


}

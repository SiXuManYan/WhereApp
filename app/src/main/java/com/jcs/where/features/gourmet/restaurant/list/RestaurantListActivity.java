package com.jcs.where.features.gourmet.restaurant.list;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.bean.RestaurantListRequest;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.list.DividerDecoration;

import java.util.List;

/**
 * Created by Wangsw  2021/3/24 13:56.
 * 餐厅列表
 */
public class RestaurantListActivity extends BaseMvpActivity<RestaurantListPresenter> implements RestaurantListView, OnLoadMoreListener, OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {


    private int page = Constant.DEFAULT_FIRST_PAGE;

    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler;
    private LinearLayout
            area_filter_ll,
            food_filter_ll,
            other_filter_ll,
            filter_container_ll;
    private ViewPager filter_pager;
    private View dismiss_view;


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

        // filter
        area_filter_ll = findViewById(R.id.area_filter_ll);
        food_filter_ll = findViewById(R.id.food_filter_ll);
        other_filter_ll = findViewById(R.id.other_filter_ll);

        filter_container_ll = findViewById(R.id.filter_container_ll);
        filter_pager = findViewById(R.id.filter_pager);
        dismiss_view = findViewById(R.id.dismiss_view);


        // list
        mAdapter = new RestaurantListAdapter();
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.addChildClickViewIds(R.id.take_ll);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(getItemDecoration());

        // 动画
//        mFilterShowAnimation = AnimationUtils.loadAnimation(this, R.anim.filter_in);
//        mFilterHideAnimation = AnimationUtils.loadAnimation(this, R.anim.filter_out);


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
        area_filter_ll.setOnClickListener(this::onAreaFilterClick);
        food_filter_ll.setOnClickListener(this::onFoodFilterClick);
        other_filter_ll.setOnClickListener(this::onOtherFilterClick);
        dismiss_view.setOnClickListener(this::onFilterDismissClick);
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


    private void onAreaFilterClick(View view) {
        handleFilterVisible(true);
    }


    private void onFoodFilterClick(View view) {
        handleFilterVisible(true);
    }

    private void onOtherFilterClick(View view) {
        handleFilterVisible(true);
    }

    private void onFilterDismissClick(View view) {
        handleFilterVisible(false);
    }

    private void handleFilterVisible(boolean show) {
        if (show) {
            if (filter_container_ll.getVisibility() == View.GONE) {
//                filter_container_ll.startAnimation(mFilterShowAnimation);
                filter_container_ll.setVisibility(View.VISIBLE);
            }
        } else {
            if (filter_container_ll.getVisibility() == View.VISIBLE) {
//                filter_container_ll.startAnimation(mFilterHideAnimation);
                filter_container_ll.setVisibility(View.GONE);
            }
        }
    }


}

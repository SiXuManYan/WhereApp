package com.jcs.where.features.gourmet.restaurant.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private RestaurantListAdapter mAdapter;
    private RestaurantListRequest mRequest;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_gourmet_list;
    }

    @Override
    protected void initView() {
        swipe_layout = findViewById(R.id.swipe_layout);
        recycler = findViewById(R.id.recycler);

        mAdapter = new RestaurantListAdapter();
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.addChildClickViewIds(R.id.take_ll);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(getItemDecoration());
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
}

package com.jcs.where.home.fragment;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.home.adapter.OrderListAdapter;
import com.jcs.where.home.decoration.MarginTopDecoration;
import com.jcs.where.home.model.OrderModel;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.annotations.NonNull;

/**
 * create by zyf on 2020/12/10 10:30 PM
 */
public class OrderListFragment extends BaseFragment {

    private OrderType mOrderType;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecycler;
    private OrderListAdapter mAdapter;
    private boolean isFirstLoad = true;
    private OrderModel mModel;

    public OrderListFragment(OrderType orderType) {
        this.mOrderType = orderType;
    }

    @Override
    protected void initView(View view) {
        mRecycler = view.findViewById(R.id.orderRecycler);
        mSwipeRefresh = view.findViewById(R.id.swipeLayout);
    }

    @Override
    protected void initData() {
        mModel = new OrderModel();
        mAdapter = new OrderListAdapter(R.layout.item_order_list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addItemDecoration(new MarginTopDecoration() {
            @Override
            public int getMarginTop() {
                return 10;
            }
        });
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected void bindListener() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderByType();
            }
        });

        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeRefresh.setRefreshing(false);
                mAdapter.getLoadMoreModule().setEnableLoadMore(true);
                getOrderByType();
            }
        });
        //不自动加载
        mAdapter.getLoadMoreModule().setAutoLoadMore(false);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            getOrderByType();
            isFirstLoad = false;
        }
    }

    private void getOrderByType() {
        mModel.getOrderList(mOrderType.type, new BaseObserver<OrderListResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopRefresh();
            }

            @Override
            public void onNext(@NonNull OrderListResponse orderListResponse) {
                mAdapter.getData().clear();
                mAdapter.addData(orderListResponse.getData());
                stopRefresh();
            }
        });
    }

    private void stopRefresh() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    enum OrderType {
        All(0), WaitForPay(1), WaitForUse(2), WaitForComment(3), AfterSale(4);
        int type;

        OrderType(int type) {
            this.type = type;
        }
    }
}

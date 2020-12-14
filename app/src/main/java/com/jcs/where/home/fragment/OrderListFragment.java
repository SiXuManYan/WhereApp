package com.jcs.where.home.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.home.adapter.OrderListAdapter;
import com.jcs.where.home.decoration.MarginTopDecoration;
import com.jcs.where.model.OrderModel;
import com.jcs.where.hotel.HotelOrderDetailActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.annotations.NonNull;

/**
 * 首页订单列表展示列表的Fragment
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
        mAdapter.addChildClickViewIds(R.id.rightToTv, R.id.leftToTv);
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

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@androidx.annotation.NonNull BaseQuickAdapter adapter, @androidx.annotation.NonNull View view, int position) {
                int id = view.getId();
                if (id == R.id.rightToTv) {
                    toActivity(mAdapter.getToRightClass(position), new IntentEntry("id", String.valueOf(mAdapter.getItemId(position))));
                }

                if (id == R.id.leftToTv) {
                    toActivity(mAdapter.getToLeftClass(position), new IntentEntry("id", String.valueOf(mAdapter.getItemId(position))));
                }
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull View view, int position) {
                HotelOrderDetailActivity.goTo(getContext(), String.valueOf(mAdapter.getItemId(position)));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            getOrderByType();
            isFirstLoad = false;
        }
    }

    public void getOrderByType() {
        getOrderByType("");
    }

    public void getOrderByType(String keyword) {
        mModel.getOrderList(mOrderType.type, keyword, new BaseObserver<OrderListResponse>() {
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

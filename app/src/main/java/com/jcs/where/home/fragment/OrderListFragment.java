package com.jcs.where.home.fragment;

import android.view.View;
import android.widget.TextView;

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
import io.reactivex.annotations.NonNull;

/**
 * create by zyf on 2020/12/10 10:30 PM
 */
public class OrderListFragment extends BaseFragment {

    private OrderType mOrderType;
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

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            mModel.getOrderList(mOrderType.type, new BaseObserver<OrderListResponse>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {

                }

                @Override
                public void onNext(@NonNull OrderListResponse orderListResponse) {
                    mAdapter.getData().clear();
                    mAdapter.addData(orderListResponse.getData());
                }
            });
            isFirstLoad = false;
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

package com.jcs.where.home.fragment;

import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseFragment;

import androidx.recyclerview.widget.RecyclerView;

/**
 * create by zyf on 2020/12/10 10:30 PM
 */
public class OrderListFragment extends BaseFragment {

    private OrderType mOrderType;
    private RecyclerView mRecycler;
    private boolean isFirstLoad = true;
    private TextView mOrderTv;

    public OrderListFragment(OrderType orderType) {
        this.mOrderType = orderType;
    }

    @Override
    protected void initView(View view) {
        mRecycler = view.findViewById(R.id.orderRecycler);
        mOrderTv = view.findViewById(R.id.orderTv);

        mOrderTv.setText("我要展示的分类类型为：" + mOrderType.type);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {


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

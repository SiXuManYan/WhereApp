package com.jcs.where.home.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.home.adapter.OrderListAdapter;
import com.jcs.where.home.decoration.MarginTopDecoration;
import com.jcs.where.home.dialog.CancelOrderDialog;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.hotel.activity.HotelOrderDetailActivity;
import com.jcs.where.hotel.activity.HotelPayActivity;
import com.jcs.where.model.OrderModel;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.widget.calendar.JcsCalendarDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 首页订单列表展示列表的Fragment
 * create by zyf on 2020/12/10 10:30 PM
 */
public class OrderListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private final OrderType mOrderType;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecycler;
    private OrderListAdapter mAdapter;
    private boolean isFirstLoad = true;
    private OrderModel mModel;

    private View emptyView;

    private int page = Constant.DEFAULT_FIRST_PAGE;
    private String searchInput = "";

    public OrderListFragment(OrderType orderType) {
        this.mOrderType = orderType;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        mRecycler = view.findViewById(R.id.orderRecycler);
        mSwipeRefresh = view.findViewById(R.id.swipeLayout);

        // empty
        emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
        ImageView empty_iv = emptyView.findViewById(R.id.empty_iv);
        TextView empty_message_tv = emptyView.findViewById(R.id.empty_message_tv);
        empty_iv.setImageResource(R.mipmap.ic_empty_order);
        empty_message_tv.setText(R.string.empty_order_list);

    }

    @Override
    protected void initData() {
        mModel = new OrderModel();


        mAdapter = new OrderListAdapter(getContext());
        mAdapter.addChildClickViewIds(R.id.rightToTv, R.id.leftToTv);

        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

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
    protected void loadOnVisible() {
        getOrder(searchInput, page);
    }

    @Override
    protected void bindListener() {


        mSwipeRefresh.setOnRefreshListener(this);

        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            page++;
            getOrder(searchInput, page);
        });


        mAdapter.setOnItemChildClickListener(this::onOrderItemChildClicked);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderListResponse data = mAdapter.getData().get(position);
            HotelOrderDetailActivity.goTo(getContext(), String.valueOf(data.id));
        });
    }

    private void onOrderItemChildClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        int id = view.getId();

        OrderListResponse data = mAdapter.getData().get(position);

        Context context = getContext();
        if (context == null) {
            return;
        }
        if (id == R.id.rightToTv) {
            Class<? extends AppCompatActivity> toRightClass = mAdapter.getToRightClass(position);

            if (toRightClass != null) {
                String simpleName = toRightClass.getSimpleName();
                if (simpleName.equals("HotelPayActivity")) {
                    HotelPayActivity.goTo(context, null);
                } else if (simpleName.equals("HotelDetailActivity")) {
                    JcsCalendarDialog jcsCalendarDialog = new JcsCalendarDialog();
                    jcsCalendarDialog.initCalendar(context);
                    JcsCalendarAdapter.CalendarBean startBean = jcsCalendarDialog.getStartBean();
                    JcsCalendarAdapter.CalendarBean endBean = jcsCalendarDialog.getEndBean();
                    Integer modelId = mAdapter.getData().get(position).model_id;
                    HotelDetailActivity.goTo(context, modelId, startBean, endBean, 1, "", "", 1);
                } else {
                    toActivity(toRightClass, new IntentEntry("id", String.valueOf(data.id)));
                }
            }
        }

        if (id == R.id.leftToTv) {
            Log.e("OrderListFragment", "onItemChildClick: " + "left");
            Class<? extends AppCompatActivity> toLeftClass = mAdapter.getToLeftClass(position);

            if (toLeftClass != null) {
                String simpleName = toLeftClass.getSimpleName();
                if (simpleName.equals("CancelOrderActivity")) {
                    // 展示取消订单的dialog
                    CancelOrderDialog cancelOrderDialog = new CancelOrderDialog();
                    cancelOrderDialog.show(getChildFragmentManager());
                } else {
                    toActivity(toLeftClass, new IntentEntry("id", String.valueOf(data.id)));
                }
            }
        }
    }


    /**
     * 获取订单列表
     *
     * @param searchInput 搜索关键字，不传代表全集
     * @param page        页码
     */
    public void getOrder(String searchInput, int page) {
        this.searchInput = searchInput;

        mModel.getOrderList(mOrderType.type, searchInput, page, new BaseObserver<PageResponse<OrderListResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopRefresh();
                mAdapter.setEmptyView(emptyView);
            }

            @Override
            public void onSuccess(@NonNull PageResponse<OrderListResponse> response) {
                stopRefresh();
                boolean isLastPage = response.getLastPage() == page;

                List<OrderListResponse> data = response.getData();
                BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                if (data.isEmpty()) {
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        loadMoreModule.loadMoreComplete();
                        mAdapter.setEmptyView(emptyView);
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

    private void stopRefresh() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;
        getOrder(searchInput, page);
    }

    enum OrderType {
        All(0), WaitForPay(1), WaitForUse(2), WaitForComment(3), AfterSale(4);
        int type;

        OrderType(int type) {
            this.type = type;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<String> baseEvent) {

        if (baseEvent.code == EventCode.EVENT_REFRESH_ORDER_LIST) {
            onRefresh();
        }

    }


}

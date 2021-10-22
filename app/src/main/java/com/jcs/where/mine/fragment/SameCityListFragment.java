package com.jcs.where.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.EventCode;
import com.jcs.where.features.hotel.detail.HotelDetailActivity2;
import com.jcs.where.features.mechanism.MechanismActivity;
import com.jcs.where.features.store.detail.StoreDetailActivity;
import com.jcs.where.features.travel.detail.TravelDetailActivity;
import com.jcs.where.mine.adapter.SameCityListAdapter;
import com.jcs.where.mine.model.CollectionListModel;
import com.jcs.where.mine.view_type.SameCityType;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.widget.list.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 页面-同城收藏列表
 * create by zyf on 2021/2/2 8:26 下午
 */
public class SameCityListFragment extends BaseFragment implements OnLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private SameCityListAdapter mAdapter;
    private CollectionListModel mModel;
    private boolean mIsFirst = false;
    private boolean mIsLoaded = false;


    private int page = Constant.DEFAULT_FIRST_PAGE;


    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        mSwipeLayout = view.findViewById(R.id.swipeLayout);
        mRecyclerView = view.findViewById(R.id.sameCityRecycler);

        EmptyView emptyView = new EmptyView(getActivity());
        emptyView.showEmptyDefault();
        mAdapter = new SameCityListAdapter();
        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
//        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(getItemDecoration());
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }

    @Override
    protected void initData() {
        mModel = new CollectionListModel();
        if (mIsFirst && !mIsLoaded) {
            getCollectionSameCityList(page);
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            showLoading();
            getCollectionSameCityList(page);
            mIsLoaded = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getCollectionSameCityList(int page) {

        mModel.getCollectionSameCity(page, new BaseObserver<PageResponse<CollectedResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            protected void onSuccess(PageResponse<CollectedResponse> response) {
                mSwipeLayout.setRefreshing(false);
                boolean isLastPage = response.getLastPage() == page;
                List<CollectedResponse> data = response.getData();

                List<CollectedResponse> newData = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {

                    Integer type = data.get(i).getType();

                    // 只处理现有的三种类型
                    if (type == 1 || type == 2 || type == 11 || type == 13) {
                        newData.add(data.get(i));
                    }
                }


                BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                if (newData.isEmpty()) {
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        loadMoreModule.loadMoreComplete();
                    } else {
                        loadMoreModule.loadMoreEnd();
                    }
                    return;
                }
                if (page == Constant.DEFAULT_FIRST_PAGE) {
                    mAdapter.setNewInstance(newData);
                    loadMoreModule.checkDisableLoadMoreIfNotFullPage();
                } else {
                    mAdapter.addData(newData);
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
        mSwipeLayout.setOnRefreshListener(this::onRefresh);
        mAdapter.setOnItemClickListener(this::onSameCityItemClicked);
    }

    private void onSameCityItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        CollectedResponse response = mAdapter.getItem(position);
        Integer type = response.getType();
        Context context = getContext();
        if (context != null) {
            switch (type) {
                case SameCityType.Hotel:
                    JcsCalendarDialog dialog = new JcsCalendarDialog();
                    dialog.initCalendar(context);
                    HotelDetailActivity2.Companion.navigation(requireContext(), response.hotel.getId(), dialog.getStartBean(), dialog.getEndBean(), null, null, null,1);
                    break;
                case SameCityType.Mechanism:
                    Bundle b = new Bundle();
                    b.putInt(Constant.PARAM_ID, response.general.id);
                    startActivity(MechanismActivity.class, b);
                    break;
                case SameCityType.TouristAttraction:
                    TravelDetailActivity.Companion.navigation(requireContext(), getId());
                    break;
                case SameCityType.Store:
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.PARAM_ID, response.estore.getId());
                    startActivity(StoreDetailActivity.class, bundle);
                    break;
                default:
                    break;

            }
        }
    }

    private void onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;
        getCollectionSameCityList(page);
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collected_same_city_list;
    }

    @Override
    public void onLoadMore() {
        page++;
        getCollectionSameCityList(page);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {
        if (baseEvent.code == EventCode.EVENT_REFRESH_COLLECTION) {
            onRefresh();
        }
    }

}

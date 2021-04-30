package com.jcs.where.mine.fragment;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.government.activity.MechanismDetailActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.mine.adapter.SameCityListAdapter;
import com.jcs.where.mine.model.CollectionListModel;
import com.jcs.where.mine.view_type.SameCityType;
import com.jcs.where.news.item_decoration.NewsListItemDecoration;
import com.jcs.where.travel.TouristAttractionDetailActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.calendar.JcsCalendarDialog;

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
        mSwipeLayout = view.findViewById(R.id.swipeLayout);
        mRecyclerView = view.findViewById(R.id.sameCityRecycler);

        mAdapter = new SameCityListAdapter();
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mRecyclerView.addItemDecoration(new NewsListItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
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
                boolean isLastPage = response.getLastPage() == page;
                List<CollectedResponse> data = response.getData();

                List<CollectedResponse> newData = response.getData();
                for (int i = 0; i < data.size(); i++) {

                    Integer type = data.get(i).getType();

                    // 只处理现有的三种类型
                    if (type == 1 || type == 2 || type == 11) {
                        newData.add(data.get(i));
                    }
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
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
        mAdapter.setOnItemClickListener(this::onSameCityItemClicked);
    }

    private void onSameCityItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        CollectedResponse collectedResponse = mAdapter.getItem(position);
        Integer type = collectedResponse.getType();
        Context context = getContext();
        if (context != null) {
            switch (type) {
                case SameCityType.Hotel:
                    JcsCalendarDialog dialog = new JcsCalendarDialog();
                    dialog.initCalendar(context);
                    HotelDetailActivity.goTo(context, collectedResponse.getHotel().getId(), dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
                    break;
                case SameCityType.Mechanism:
                    toActivity(MechanismDetailActivity.class, new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, collectedResponse.getGeneral().getId()));
                    break;
                case SameCityType.TouristAttraction:
                    TouristAttractionDetailActivity.goTo(context, collectedResponse.getTravel().getId());
                    break;
            }
        }
    }

    private void onRefreshListener() {
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
}

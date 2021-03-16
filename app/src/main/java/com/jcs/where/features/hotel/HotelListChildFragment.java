package com.jcs.where.features.hotel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.hotel.HotelListResponse;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;

import java.util.List;

/**
 * Created by Wangsw  2021/3/16 14:51.
 * 酒店列表
 */
public class HotelListChildFragment extends BaseMvpFragment<HotelListChildPresenter> implements HotelListChildView, OnLoadMoreListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private int page = Constant.DEFAULT_FIRST_PAGE;


    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler;
    private HotelListAdapter mAdapter;

    private String mSearchText = "";
    private String mHotelTypeIds;
    private String mCityId;
    private String mPrice;
    private String mStar;
    private String mGrade;
    private int mTotalDay;
    private int mRoomCount;
    private JcsCalendarAdapter.CalendarBean mStartBean;
    private JcsCalendarAdapter.CalendarBean mEndBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }

    public static HotelListChildFragment newInstance(String hotelTypeIds, String cityId, String price, String star, JcsCalendarAdapter.CalendarBean startBean, JcsCalendarAdapter.CalendarBean endBean, int totalDay, int roomNumber) {
        Bundle args = new Bundle();
        args.putString(Constant.PARAM_HOTEL_TYPE_IDS, hotelTypeIds);
        args.putString(HotelSelectDateHelper.EXT_CITY_ID, cityId);
        args.putString(HotelSelectDateHelper.EXT_PRICE, price);
        args.putString(HotelSelectDateHelper.EXT_STAR, star);
        args.putInt(HotelSelectDateHelper.EXT_TOTAL_DAY, totalDay);
        args.putInt(HotelSelectDateHelper.EXT_ROOM_NUMBER, roomNumber);
        args.putSerializable(HotelSelectDateHelper.EXT_START_DATE_BEAN, startBean);
        args.putSerializable(HotelSelectDateHelper.EXT_END_DATE_BEAN, endBean);
        HotelListChildFragment fragment = new HotelListChildFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initView(View view) {
        swipe_layout = view.findViewById(R.id.swipe_layout);
        recycler = view.findViewById(R.id.recycler);
    }

    @Override
    protected void initData() {
        initExtra();
        presenter = new HotelListChildPresenter(this);
        mAdapter = new HotelListAdapter();
        recycler.setAdapter(mAdapter);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.setOnItemClickListener(this);
    }

    private void initExtra() {
        Bundle bundle = getArguments();
        mHotelTypeIds = bundle.getString(Constant.PARAM_HOTEL_TYPE_IDS);
        mCityId = bundle.getString(HotelSelectDateHelper.EXT_CITY_ID);
        mPrice = bundle.getString(HotelSelectDateHelper.EXT_PRICE);
        mStar = bundle.getString(HotelSelectDateHelper.EXT_STAR);
        mTotalDay = bundle.getInt(HotelSelectDateHelper.EXT_TOTAL_DAY);
        mRoomCount = bundle.getInt(HotelSelectDateHelper.EXT_ROOM_NUMBER);
        mStartBean = (JcsCalendarAdapter.CalendarBean) bundle.getSerializable(HotelSelectDateHelper.EXT_START_DATE_BEAN);
        mEndBean = (JcsCalendarAdapter.CalendarBean) bundle.getSerializable(HotelSelectDateHelper.EXT_END_DATE_BEAN);
    }

    @Override
    protected void bindListener() {
        swipe_layout.setOnRefreshListener(this);
    }

    @Override
    protected void loadOnVisible() {
        onRefresh();
    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
    }


    @Override
    public void onRefresh() {
        swipe_layout.setRefreshing(true);
        page = Constant.DEFAULT_FIRST_PAGE;
        getData();
    }


    private void getData() {
        presenter.getList(page, mCityId, mPrice, mStar, mHotelTypeIds, mSearchText, mGrade);
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

    }


    @Override
    public void bindList(List<HotelListResponse> data, boolean isLastPage) {
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
}

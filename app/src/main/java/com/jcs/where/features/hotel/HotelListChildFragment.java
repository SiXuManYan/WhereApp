package com.jcs.where.features.hotel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.hotel.HotelListResponse;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.widget.list.DividerDecoration;

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

    /**
     * 搜索内容
     */
    private String mSearchText = "";

    /**
     * 当前类型id
     */
    private String mHotelTypeIds;

    /**
     * 选中的城市id
     */
    private String mCityId;

    /**
     * 选中的城市名
     */
    private String mCityName;

    /**
     * 选中的星级
     */
    private int mStar;

    /**
     * 入住总天数
     */
    private int mTotalDay;


    /**
     * 价格区间，开始
     */
    private int mPriceStart;

    /**
     * 价格区间，结束
     */
    private int mPriceEnd;

    /**
     * 预订房间数
     */
    private int mRoomNum;

    /**
     * 选中的评分
     */
    private float mScore;

    /**
     * 选中的开始日期
     */
    private JcsCalendarAdapter.CalendarBean mStartDateBean;

    /**
     * 选中的结束日期
     */
    private JcsCalendarAdapter.CalendarBean mEndDateBean;
    private EmptyView mEmptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected void initView(View view) {
        swipe_layout = view.findViewById(R.id.swipe_layout);
        recycler = view.findViewById(R.id.recycler);
        recycler.addItemDecoration(getItemDecoration());
        mEmptyView = new EmptyView(getContext());
        mEmptyView.showEmptyDefault();
    }

    /**
     * @param mHotelTypeIds 当前 fragment 类型id
     * @param startBean     开始日期
     * @param endBean       结束日期
     * @param totalDay      入住时长 (天)
     * @param cityId        城市id
     * @param startPrice    价格区间(开始)
     * @param endPrice      价格区间(结束)
     * @param star          选中的星级
     * @param score         选中的酒店评分
     * @param roomNumber    预订房间总数
     * @return
     */
    public static HotelListChildFragment getInstance(String mHotelTypeIds,

                                                     JcsCalendarAdapter.CalendarBean startBean,
                                                     JcsCalendarAdapter.CalendarBean endBean,
                                                     int totalDay,
                                                     String cityId,
                                                     int startPrice,
                                                     int endPrice,
                                                     int star,
                                                     float score,
                                                     int roomNumber) {

        Bundle bundle = new Bundle();
        bundle.putString(Constant.PARAM_HOTEL_TYPE_IDS, mHotelTypeIds);
        bundle.putSerializable(HotelSelectDateHelper.EXT_START_DATE_BEAN, startBean);
        bundle.putSerializable(HotelSelectDateHelper.EXT_END_DATE_BEAN, endBean);
        bundle.putInt(HotelSelectDateHelper.EXT_TOTAL_DAY, totalDay);
        bundle.putString(HotelSelectDateHelper.EXT_CITY_ID, cityId);

        bundle.putInt(HotelSelectDateHelper.EXT_PRICE_START, startPrice);
        bundle.putInt(HotelSelectDateHelper.EXT_PRICE_END, endPrice);

        bundle.putInt(HotelSelectDateHelper.EXT_STAR, star);
        bundle.putFloat(HotelSelectDateHelper.EXT_SCORE, score);

        bundle.putInt(HotelSelectDateHelper.EXT_ROOM_NUMBER, roomNumber);

        HotelListChildFragment fragment = new HotelListChildFragment();
        fragment.setArguments(bundle);

        return fragment;
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
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setOnItemClickListener(this);
    }

    private void initExtra() {
        Bundle bundle = getArguments();
        mHotelTypeIds = bundle.getString(Constant.PARAM_HOTEL_TYPE_IDS);

        // 开始结束日期
        mStartDateBean = (JcsCalendarAdapter.CalendarBean) bundle.getSerializable(HotelSelectDateHelper.EXT_START_DATE_BEAN);
        mEndDateBean = (JcsCalendarAdapter.CalendarBean) bundle.getSerializable(HotelSelectDateHelper.EXT_END_DATE_BEAN);

        // 入住总天数
        mTotalDay = bundle.getInt(HotelSelectDateHelper.EXT_TOTAL_DAY, 0);

        // 城市
        mCityId = bundle.getString(HotelSelectDateHelper.EXT_CITY_ID, "");

        // 价格区间
        mPriceStart = bundle.getInt(HotelSelectDateHelper.EXT_PRICE_START, 0);
        mPriceEnd = bundle.getInt(HotelSelectDateHelper.EXT_PRICE_END, 0);

        // 星级
        mStar = bundle.getInt(HotelSelectDateHelper.EXT_STAR);

        // 评分
        mScore = bundle.getFloat(HotelSelectDateHelper.EXT_SCORE, 0);

        // 房间总数
        mRoomNum = bundle.getInt(HotelSelectDateHelper.EXT_ROOM_NUMBER, 1);
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
        presenter.getList(page, mCityId, mPriceStart, mPriceEnd, mStar, mHotelTypeIds, mSearchText, mScore);
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        HotelListResponse data = mAdapter.getData().get(position);
        HotelDetailActivity.goTo(getContext(), data.id, mStartDateBean, mEndDateBean, mTotalDay, mStartDateBean.getYear() + "", mEndDateBean.getYear() + "", mRoomNum);
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

    private RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }

}

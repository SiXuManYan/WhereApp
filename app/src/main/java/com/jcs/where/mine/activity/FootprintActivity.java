package com.jcs.where.mine.activity;

import static com.jcs.where.utils.Constant.PARAM_ID;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.FootprintResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity;
import com.jcs.where.features.hotel.detail.HotelDetailActivity2;
import com.jcs.where.features.mechanism.MechanismActivity;
import com.jcs.where.features.store.detail.StoreDetailActivity;
import com.jcs.where.features.travel.detail.TravelDetailActivity;
import com.jcs.where.mine.adapter.FootprintListAdapter;
import com.jcs.where.mine.model.FootprintModel;
import com.jcs.where.mine.view_type.FootprintType;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.calendar.JcsCalendarDialog;

import java.util.List;

/**
 * 页面-足迹
 * create by zyf on 2021/2/2 11:17 下午
 */
public class FootprintActivity extends BaseActivity {
    private FootprintModel mModel;
    private PageResponse<FootprintResponse> mPageResponse;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipeLayout;
    private FootprintListAdapter mAdapter;

    @Override
    protected void initView() {
        mSwipeLayout = findViewById(R.id.swipeLayout);
        mRecycler = findViewById(R.id.footprintRecycler);
        mAdapter = new FootprintListAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new FootprintModel();
        showLoading();
        getDataFromNet();
    }

    private void getDataFromNet() {
        mModel.getFootprintList(new BaseObserver<PageResponse<FootprintResponse>>() {

            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(PageResponse<FootprintResponse> response) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
                mPageResponse = response;
                List<FootprintResponse> data = mPageResponse.getData();
                mAdapter.getData().clear();
                if (data != null && data.size() > 0) {
                    mAdapter.addData(data);
                } else {
                    mAdapter.setEmptyView(R.layout.view_empty_data_brvah);
                }
            }
        });
    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemClickListener(this::onItemClicked);
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
    }

    private void onRefreshListener() {
        getDataFromNet();
    }

    private void onItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        FootprintResponse data = mAdapter.getData().get(position);
        Integer type = data.getType();
        FootprintResponse.ModuleDataDTO dto = data.getModuleData();
        Integer dtoId = dto.getId();
        switch (type) {
            case FootprintType.Hotel:
                JcsCalendarDialog dialog = new JcsCalendarDialog();
                dialog.initCalendar(this);
                HotelDetailActivity2.Companion.navigation(this, dtoId, dialog.getStartBean(), dialog.getEndBean(), null, null, null, 1);
                break;
            case FootprintType.TouristAttraction:
                TravelDetailActivity.Companion.navigation(this, dtoId);
                break;
            case FootprintType.Mechanism:
                Bundle b = new Bundle();
                b.putInt(PARAM_ID, dtoId);
                startActivity(MechanismActivity.class, b);
                break;
            case FootprintType.Restaurant:
                String id = String.valueOf(data.getModuleData().getId());
                Bundle bundle = new Bundle();
                bundle.putString(PARAM_ID, id);
                startActivity(RestaurantDetailActivity.class, bundle);
                break;
            case FootprintType.E_STORE:
                int shopId = data.getModuleData().getId();
                Bundle b1 = new Bundle();
                b1.putInt(Constant.PARAM_ID, shopId);
                startActivity(StoreDetailActivity.class, b1);
                break;
            default:
                break;

        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_footprint;
    }
}

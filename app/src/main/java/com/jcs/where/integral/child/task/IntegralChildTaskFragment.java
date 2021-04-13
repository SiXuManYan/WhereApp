package com.jcs.where.integral.child.task;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.government.activity.MechanismDetailActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.travel.TouristAttractionDetailActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.widget.list.DividerDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分任务
 */
public class IntegralChildTaskFragment extends BaseMvpFragment<IntegralChildTaskPresenter> implements IntegralChildTaskView, OnLoadMoreListener, OnItemClickListener {


    private TextView mSignInTv;

    private RecyclerView mRv;
    private HomeRecommendAdapter mAdapter;
    private int page = Constant.DEFAULT_FIRST_PAGE;

    public static IntegralChildTaskFragment newInstance() {
        return new IntegralChildTaskFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integral_child_task;
    }

    @Override
    protected void initView(View view) {
        mSignInTv = view.findViewById(R.id.sign_in_tv);
        mRv = view.findViewById(R.id.recycler);

    }

    @Override
    protected void initData() {
        presenter = new IntegralChildTaskPresenter(this);

        View header = getLayoutInflater().inflate(R.layout.header_integral_task, null);
        mSignInTv = header.findViewById(R.id.sign_in_tv);

        mAdapter = new HomeRecommendAdapter(false);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(getItemDecoration());
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.setHeaderView(header);
        mAdapter.setHeaderWithEmptyEnable(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setOnItemClickListener(this);
        presenter.getRecommendList(page);
    }

    @Override
    protected void loadOnVisible() {

    }

    @Override
    protected void bindListener() {
        mSignInTv.setOnClickListener(this::signInClick);
    }

    public void signInClick(View view) {

        if (mSignInTv.isEnabled()) {
            EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_IN_REQUEST));
        }
    }


    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        super.onEventReceived(baseEvent);
        if (baseEvent.code == EventCode.EVENT_SIGN_IN_CHANGE_STATUS) {
            Boolean data = (Boolean) baseEvent.data;
            changeSignStatus(data);
        }
    }

    private void changeSignStatus(boolean isSigned) {
        if (mSignInTv.getVisibility() != View.VISIBLE) {
            mSignInTv.setVisibility(View.VISIBLE);
        }
        mSignInTv.setEnabled(!isSigned);
        if (isSigned) {
            mSignInTv.setText(R.string.already_sign_in);
        } else {
            mSignInTv.setText(R.string.sign_in_now);
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        presenter.getRecommendList(page);
    }

    @Override
    public void bindDetailData(List<HomeRecommendResponse> data, boolean isLastPage) {
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
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(15f), 0, 0);
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        HomeRecommendResponse data = mAdapter.getData().get(position);
        int itemViewType = mAdapter.getItemViewType(position + mAdapter.getHeaderLayoutCount());
        switch (itemViewType) {
            case HomeRecommendResponse.MODULE_TYPE_1_HOTEL:
                JcsCalendarDialog dialog = new JcsCalendarDialog();
                dialog.initCalendar(getActivity());
                HotelDetailActivity.goTo(getActivity(), data.id, dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
                break;
            case HomeRecommendResponse.MODULE_TYPE_2_SERVICE:
                toActivity(MechanismDetailActivity.class, new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, String.valueOf(data.id)));
                break;
            case HomeRecommendResponse.MODULE_TYPE_3_FOOD:
                ToastUtils.showShort(R.string.coming_soon);
                break;
            case HomeRecommendResponse.MODULE_TYPE_4_TRAVEL:
                TouristAttractionDetailActivity.goTo(getActivity(), data.id);
                break;
            default:
                break;
        }


    }
}

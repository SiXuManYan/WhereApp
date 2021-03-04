package com.jcs.where.integral.child.detail;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.IntegralDetailResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.utils.Constant;

import java.util.List;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分明细
 */
public class IntegralChildDetailFragment extends BaseMvpFragment<IntegralChildDetailPresenter> implements IntegralChildDetailView, OnLoadMoreListener {


    private RecyclerView mRv;
    private IntegralChildAdapter mAdapter;
    private int page = Constant.DEFAULT_FIRST_PAGE;


    public static IntegralChildDetailFragment newInstance() {
        return new IntegralChildDetailFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integral_child;
    }

    @Override
    protected void initView(View view) {
        mRv = view.findViewById(R.id.integral_rv);
    }

    @Override
    protected void initData() {
        presenter = new IntegralChildDetailPresenter(this);
        mAdapter = new IntegralChildAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
    }

    @Override
    protected void loadOnVisible() {
        presenter.getIntegralDetailList(page);
    }

    @Override
    protected void bindListener() {

    }


    @Override
    public void bindDetailData(List<IntegralDetailResponse> data, boolean isLastPage) {
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

    @Override
    public void onLoadMore() {
        page++;
        presenter.getIntegralDetailList(page);
    }



    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        if (baseEvent.code == EventCode.EVENT_SIGN_IN_CHANGE_STATUS) {
            if (isViewCreated) {
                page = Constant.DEFAULT_FIRST_PAGE;
                presenter.getIntegralDetailList(page);
            }
        }
    }
}

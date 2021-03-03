package com.jcs.where.integral.child.task;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Wangsw  2021/1/22 10:06.
 * 积分任务
 */
public class IntegralChildTaskFragment extends BaseMvpFragment<IntegralChildTaskPresenter> implements IntegralChildTaskView, OnLoadMoreListener {


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

        mAdapter = new HomeRecommendAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.setHeaderView(header);
        mAdapter.setHeaderWithEmptyEnable(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(false);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    protected void loadOnVisible() {
        presenter.getRecommendList(page);
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
}

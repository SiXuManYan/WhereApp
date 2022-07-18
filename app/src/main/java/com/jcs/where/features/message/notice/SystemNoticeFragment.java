package com.jcs.where.features.message.notice;

import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.response.message.SystemMessageResponse;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.features.message.notice.detail.SystemMessageDetailActivity;
import com.jcs.where.features.web.WebViewActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.list.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/2/20 15:14.
 * 系统通知
 */
public class SystemNoticeFragment extends BaseMvpFragment<SystemNoticePresenter>
        implements SystemNoticeView, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private int page = Constant.DEFAULT_FIRST_PAGE;

    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler;
    private SystemMessageAdapter mAdapter;
    public ArrayList<String> id = new ArrayList<>();
    private EmptyView emptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }


    @Override
    protected void initView(View view) {
        swipe_layout = view.findViewById(R.id.swipe_layout);
        recycler = view.findViewById(R.id.recycler);
        recycler.addItemDecoration(new DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5), SizeUtils.dp2px(1f), SizeUtils.dp2px(15f), 0));
        emptyView = new EmptyView(requireContext());
        emptyView.setEmptyImage(R.mipmap.ic_empty_un_notice);
        emptyView.setEmptyHint(R.string.no_notice_yet);

    }

    @Override
    protected void initData() {

        presenter = new SystemNoticePresenter(this);
        mAdapter = new SystemMessageAdapter();
        recycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(false);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            SystemMessageResponse data = mAdapter.getData().get(position);

            if (data.detail_type == 1) {
                String link = data.link;
                if (TextUtils.isEmpty(link)) {
                    return;
                }
                WebViewActivity.goTo(getActivity(), link);
            } else {
                SystemMessageDetailActivity.goTo(getActivity(), data.title, data.message, data.created_at);
            }

            // 设置消息已读
            if (data.is_read != 1) {
                data.is_read = 1;
                mAdapter.notifyItemChanged(position);
                id.add(data.id);
                presenter.setMessageRead(id);
            }
        });
    }

    @Override
    protected void loadOnVisible() {
        onRefresh();
    }

    @Override
    protected void bindListener() {
        swipe_layout.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        swipe_layout.setRefreshing(true);
        page = Constant.DEFAULT_FIRST_PAGE;
        presenter.getMessageList(page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.getMessageList(page);
    }

    @Override
    public void bindList(List<SystemMessageResponse> data, boolean isLastPage) {

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

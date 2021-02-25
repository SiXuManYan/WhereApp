package com.jcs.where.features.message.conversation;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpFragment;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Wangsw  2021/2/20 15:04.
 * 商家对话
 *
 * @see <a href="https://www.rongcloud.cn/docs/api/android/imlib/io/rong/imlib/RongIMClient.html#getConversationList-io.rong.imlib.RongIMClient">融云API 文档</a>
 */
public class BusinessConversationFragment extends BaseMvpFragment<BusinessConversationPresenter> implements BusinessConversationView, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    /**
     * 时间戳（毫秒），通过获取从此时间戳往前的会话，传入 0 表示从最新会话开始获取。参考 Message.getSentTime() 。
     */
    private Long timeStamp = 0L;
    private int page = 0;

    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler;
    private BusinessConversationAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected void initView(View view) {
        swipe_layout = view.findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(this);
        recycler = view.findViewById(R.id.recycler);
        mAdapter = new BusinessConversationAdapter();
        recycler.setAdapter(mAdapter);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(false);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            Conversation conversation = mAdapter.getData().get(position);
            Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
            String targetId = conversation.getTargetId();
            String title = "";
            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
            if (userInfo != null) {
                title = userInfo.getName();
            }
            RongIM.getInstance().startConversation(getActivity(), conversationType, targetId, title, null);
        });
    }

    @Override
    protected void initData() {
        presenter = new BusinessConversationPresenter(this);
        onRefresh();
    }


    @Override
    protected void bindListener() {
        swipe_layout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipe_layout.setRefreshing(true);
        page = 0;
        timeStamp = 0L;
        presenter.getConversationList(page, timeStamp);
    }


    @Override
    public void bindList(List<Conversation> data, boolean isLastPage) {
        if (swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(false);
        }
        BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
        if (data == null || data.isEmpty()) {
            if (page == 0) {
                loadMoreModule.loadMoreComplete();
            } else {
                loadMoreModule.loadMoreEnd();
            }
            return;
        }
        if (page == 0) {
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
        timeStamp = data.get(data.size() - 1).getSentTime();

    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.getConversationList(page, timeStamp);
    }

    @Override
    public void getListError(RongIMClient.ErrorCode errorCode) {
        swipe_layout.setRefreshing(false);
    }
}

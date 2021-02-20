package com.jcs.where.features.message.conversation;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpFragment;

/**
 * Created by Wangsw  2021/2/20 15:04.
 * 商家对话
 *
 * @see <a href="https://www.rongcloud.cn/docs/api/android/imlib/io/rong/imlib/RongIMClient.html#getConversationList-io.rong.imlib.RongIMClient">融云API 文档</a>
 */
public class BusinessConversationFragment extends BaseMvpFragment<BusinessConversationPresenter> implements BusinessConversationView, SwipeRefreshLayout.OnRefreshListener {


    private int page = 0;

    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected void initView(View view) {
        swipe_layout = view.findViewById(R.id.swipe_layout);
        recycler = view.findViewById(R.id.recycler);
        // getConversationListByPage(ResultCallback, long, int, Conversation.ConversationType...)
//        RongIMClient.getInstance().getConversationListByPage();
    }

    @Override
    protected void initData() {
        presenter = new BusinessConversationPresenter(this);
    }

    @Override
    protected void bindListener() {
        swipe_layout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        page = 0;
        presenter.getConversationList(page);
    }
}

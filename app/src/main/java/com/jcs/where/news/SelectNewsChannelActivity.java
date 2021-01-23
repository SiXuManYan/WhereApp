package com.jcs.where.news;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.adapter.SelectNewsChannelAdapter;
import com.jcs.where.news.item_decoration.NewsChannelItemDecoration;

import java.io.Serializable;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 选择新闻频道的页面
 * create by zyf on 2021/1/20 9:23 下午
 */
public class SelectNewsChannelActivity extends BaseActivity {
    private boolean mIsEditing = false;
    public static final String K_NEWS_FOLLOW_CHANNEL = "follow";
    public static final String K_NEWS_MORE_CHANNEL = "more";

    private TextView mOperateChannelTv;
    private RecyclerView mFollowRecycler, mMoreRecycler;
    private SelectNewsChannelAdapter mFollowAdapter;
    private SelectNewsChannelAdapter mMoreAdapter;
    private List<NewsChannelResponse> mFollowChannel;
    private List<NewsChannelResponse> mMoreChannel;

    @Override
    protected void initView() {
        NewsChannelItemDecoration decor = new NewsChannelItemDecoration();
        mOperateChannelTv = findViewById(R.id.operateMyChannelTv);
        mFollowRecycler = findViewById(R.id.followRecycler);
        mFollowAdapter = new SelectNewsChannelAdapter();
        mFollowRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mFollowRecycler.setAdapter(mFollowAdapter);
        mFollowRecycler.addItemDecoration(decor);

        mMoreRecycler = findViewById(R.id.moreRecycler);
        mMoreAdapter = new SelectNewsChannelAdapter();
        mMoreRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mMoreRecycler.setAdapter(mMoreAdapter);
        mMoreRecycler.addItemDecoration(decor);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        Intent intent = getIntent();

        Serializable follows = intent.getSerializableExtra(K_NEWS_FOLLOW_CHANNEL);
        if (follows instanceof List) {
            mFollowChannel = (List<NewsChannelResponse>) follows;
            // 将在新闻列表页，用于占位TabLayout的最后一个对象移除
            mFollowChannel.remove(mFollowChannel.size() - 1);
        }

        Serializable mores = intent.getSerializableExtra(K_NEWS_MORE_CHANNEL);
        if (mores instanceof List) {
            mMoreChannel = (List<NewsChannelResponse>) mores;
        }

        // 展示已关注的频道
        mFollowAdapter.getData().clear();
        mFollowAdapter.addData(mFollowChannel);

        // 展示未关注的频道
        mMoreAdapter.getData().clear();
        mMoreAdapter.addData(mMoreChannel);

    }

    @Override
    protected void bindListener() {
        mOperateChannelTv.setOnClickListener(this::onOperateFollowChannelClicked);
        mFollowAdapter.setOnItemClickListener(this::onFollowChannelClicked);
        mMoreAdapter.setOnItemClickListener(this::onMoreChannelClicked);

    }

    private void onMoreChannelClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsChannelResponse channel = mMoreAdapter.getItem(position);
        mMoreAdapter.notifyItemRemoved(position);
        mFollowAdapter.addData(mFollowAdapter.getItemCount(), channel);
    }

    private void onFollowChannelClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsChannelResponse channel = mFollowAdapter.getItem(position);
        mFollowAdapter.notifyItemRemoved(position);
        mMoreAdapter.addData(0, channel);
    }

    private void onOperateFollowChannelClicked(View view) {
        if (mIsEditing) {
            mOperateChannelTv.setText(getString(R.string.news_finish_channel));
            showNormalRecycler();
        } else {
            mOperateChannelTv.setText(getString(R.string.news_edit_channel));
            showActionRecycler();
        }
        mIsEditing = !mIsEditing;
    }

    private void showActionRecycler() {
        mFollowAdapter.showDel();
    }

    private void showNormalRecycler() {
        mFollowAdapter.hideDel();
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_news_category;
    }
}

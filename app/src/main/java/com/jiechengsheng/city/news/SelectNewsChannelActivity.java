package com.jiechengsheng.city.news;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.response.NewsChannelResponse;
import com.jiechengsheng.city.base.BaseActivity;
import com.jiechengsheng.city.news.adapter.SelectNewsChannelAdapter;
import com.jiechengsheng.city.news.dto.FollowAndUnfollowDTO;
import com.jiechengsheng.city.news.item_decoration.NewsChannelItemDecoration;
import com.jiechengsheng.city.utils.RequestResultCode;

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

    private final int STATUS_FOLLOWED = 1;
    private final int STATUS_UNFOLLOWED = 2;

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
//            // 将在新闻列表页，用于占位TabLayout的最后一个对象移除
//            mFollowChannel.remove(mFollowChannel.size() - 1);
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
        mFollowAdapter.addChildClickViewIds(R.id.delChannelIv);
        mOperateChannelTv.setOnClickListener(this::onOperateFollowChannelClicked);
        mFollowAdapter.setOnItemChildClickListener(this::onFollowDelClicked);
        mFollowAdapter.setOnItemClickListener(this::onFollowClicked);
        mMoreAdapter.setOnItemClickListener(this::onMoreChannelClicked);
        findViewById(R.id.back_iv).setOnClickListener(v -> {

            doFinish(1);


        });
    }

    /**
     * 普通状态下-点击了关注列表的item
     * 1，finish当前页面
     * 2，在新闻页面将关注列表更新
     * 3，在新闻页面，展示当前点击的item的新闻信息
     */
    private void onFollowClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        if (!mIsEditing) {
            doFinish(position);
        }
    }

    private void doFinish(int position) {
        FollowAndUnfollowDTO dto = new FollowAndUnfollowDTO();
        // 新闻页 TabLayout 要展示的所有频道标签数据
        dto.followed = mFollowAdapter.getData();

        // 未关注的数据要取消关注
        dto.more = mMoreAdapter.getData();

        // 新闻页要展示哪个频道的新闻
        dto.showPosition = position;

        Intent result = new Intent();
        result.putExtra("dto", dto);
        setResult(RequestResultCode.RESULT_FOLLOW_TO_NEWS, result);
        finish();
    }

    /**
     * 编辑状态下-点击了关注频道中的 del icon
     */
    private void onFollowDelClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsChannelResponse channel = mFollowAdapter.getData().remove(position);
        mFollowAdapter.notifyItemRemoved(position);
        // 改变为未关注状态
        channel.setFollowStatus(STATUS_UNFOLLOWED);
        mMoreAdapter.addData(0, channel);
    }

    /**
     * 点击了更多频道中的item，将所点击的item添加到关注频道列表中
     */
    private void onMoreChannelClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsChannelResponse channel = mMoreAdapter.getData().remove(position);
        mMoreAdapter.notifyItemRemoved(position);
        // 改变为关注状态
        channel.setFollowStatus(STATUS_FOLLOWED);
        mFollowAdapter.addData(mFollowAdapter.getItemCount(), channel);
    }

    /**
     * 点击了编辑按钮，切换关注列表的展示状态
     */
    private void onOperateFollowChannelClicked(View view) {
        if (mIsEditing) {
            mOperateChannelTv.setText(getString(R.string.news_edit_channel));
            showNormalRecycler();
        } else {
            mOperateChannelTv.setText(getString(R.string.news_finish_channel));
            showActionRecycler();
        }
        mIsEditing = !mIsEditing;
    }

    /**
     * 展示关注列表的 del icon
     */
    private void showActionRecycler() {
        mFollowAdapter.showDel();
    }

    /**
     * 隐藏关注列表的 del icon
     */
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

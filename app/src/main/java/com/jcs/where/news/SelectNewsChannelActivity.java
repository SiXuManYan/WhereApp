package com.jcs.where.news;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsTabResponse;
import com.jcs.where.api.response.ParentNewsTabResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.adapter.SelectNewsChannelAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 选择新闻频道的页面
 * create by zyf on 2021/1/20 9:23 下午
 */
public class SelectNewsChannelActivity extends BaseActivity {
    private boolean mIsEditing = false;

    private TextView mOperateChannelTv;
    private RecyclerView mRecycler;
    private SelectNewsChannelAdapter mAdapter;
    private List<ParentNewsTabResponse> mData;

    @Override
    protected void initView() {
//        mOperateChannelTv = findViewById(R.id.operateMyChannelTv);
        mRecycler = findViewById(R.id.channelRecycler);
        mAdapter = new SelectNewsChannelAdapter();
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                BaseNode item = mAdapter.getItem(position);
                if (item instanceof ParentNewsTabResponse) {
                    return 4;
                }
                return 1;
            }
        });
        mRecycler.setLayoutManager(manager);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        ParentNewsTabResponse myChannel = new ParentNewsTabResponse();
        myChannel.setPrompt(getString(R.string.news_my_channel));
        myChannel.setAction(getString(R.string.news_click_to_channel));
        myChannel.setShowEdit(true);
        ParentNewsTabResponse moreChannel = new ParentNewsTabResponse();
        moreChannel.setPrompt(getString(R.string.news_more_channel));
        moreChannel.setPrompt(getString(R.string.news_click_to_add_channel));
        moreChannel.setShowEdit(false);
        List<BaseNode> my = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            String name = "my：" + i + "";
            my.add(new NewsTabResponse(name));
        }
        myChannel.setChildren(my);
        List<BaseNode> more = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            String name = "more：" + i + "";
            more.add(new NewsTabResponse(name));
        }
        moreChannel.setChildren(more);

        mData.add(myChannel);
        mData.add(moreChannel);

        mAdapter.getData().clear();
        mAdapter.addData(mData);
    }

    @Override
    protected void bindListener() {
//        mOperateChannelTv.setOnClickListener(this::onOperateChannelClicked);
        mAdapter.setOnItemClickListener(this::onItemChannelClicked);

    }

    private void onItemChannelClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {

        BaseNode item = mAdapter.getItem(position);
        if (item instanceof NewsTabResponse) {
            if (position > mData.get(0).getChildNode().size() + 1) {
                mAdapter.notifyItemMoved(position, mData.get(0).getChildNode().size());
            }
        }

    }

    private void onOperateChannelClicked(View view) {
        if (mIsEditing) {
            mOperateChannelTv.setText(getString(R.string.news_finish_channel));
        } else {
            mOperateChannelTv.setText(getString(R.string.news_edit_channel));
        }
        mIsEditing = !mIsEditing;
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

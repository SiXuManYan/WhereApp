package com.jcs.where.news.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.api.response.ParentNewsTabResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2021/1/20 10:18 下午
 */
public class SelectNewsChannelAdapter extends BaseNodeAdapter {

    public static final int ITEM_TYPE_GROUP = 0;
    public static final int ITEM_TYPE_CHILD = 1;

    public SelectNewsChannelAdapter() {
        addFullSpanNodeProvider(new GroupProvider());
        addNodeProvider(new ChildProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> list, int position) {
        BaseNode node = list.get(position);
        if (node instanceof ParentNewsTabResponse) {
            return ITEM_TYPE_GROUP;
        } else if (node instanceof NewsChannelResponse) {
            return ITEM_TYPE_CHILD;
        }
        return -1;
    }

    public static class GroupProvider extends BaseNodeProvider {

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_select_news_channel_group;
        }

        @Override
        public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
            if (baseViewHolder.getAdapterPosition() == 0) {
                ParentNewsTabResponse parent = (ParentNewsTabResponse) baseNode;
                baseViewHolder.setText(R.id.myChannelPrompt, parent.getPrompt());
                baseViewHolder.setText(R.id.clickToChannelTv, parent.getAction());
                baseViewHolder.setGone(R.id.operateMyChannelTv, parent.isShowEdit());
            }else {
                baseViewHolder.itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static class ChildProvider extends BaseNodeProvider {

        @Override
        public int getItemViewType() {
            return 1;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_select_news_channel_child;
        }

        @Override
        public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
            NewsChannelResponse newsChannelResponse = (NewsChannelResponse) baseNode;
            baseViewHolder.setText(R.id.channelTitleTv, newsChannelResponse.getName());
        }
    }
}

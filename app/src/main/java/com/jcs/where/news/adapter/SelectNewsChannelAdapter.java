package com.jcs.where.news.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsChannelResponse;

import org.jetbrains.annotations.NotNull;

/**
 * create by zyf on 2021/1/20 10:18 下午
 */
public class SelectNewsChannelAdapter extends BaseQuickAdapter<NewsChannelResponse, BaseViewHolder> {

    private boolean mIsShowDel = false;


    public SelectNewsChannelAdapter() {
        super(R.layout.item_select_news_channel_child);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsChannelResponse newsChannelResponse) {
        baseViewHolder.setText(R.id.channelTitleTv, newsChannelResponse.getName());
        // 默认文字颜色
        baseViewHolder.setTextColor(R.id.channelTitleTv,getContext().getColor(R.color.black_333333));

        if (newsChannelResponse.isEditable()) {
            //根据tag设置删除icon的显示隐藏
            baseViewHolder.setGone(R.id.delChannelIv, !mIsShowDel);
        }else {
            baseViewHolder.setGone(R.id.delChannelIv, true);
            // -2 表示是推荐频道，UI要求推荐频道的颜色
            if (newsChannelResponse.getId() == -2){
                baseViewHolder.setTextColor(R.id.channelTitleTv,getContext().getColor(R.color.orange_FD8181));
            }
        }

        // 已关注的不需要展示添加图片
        baseViewHolder.setGone(R.id.addIcon, newsChannelResponse.getFollowStatus() == 1);
    }

    public void showDel() {
        mIsShowDel = true;
        notifyDataSetChanged();
    }

    public void hideDel() {
        mIsShowDel = false;
        notifyDataSetChanged();
    }
}

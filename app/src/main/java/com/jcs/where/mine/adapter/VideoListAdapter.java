package com.jcs.where.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.JcsVideoPlayer;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * author : hwd
 * date   : 2021/1/6-23:45
 */
public class VideoListAdapter extends BaseQuickAdapter<CollectedResponse, BaseViewHolder> {

    public VideoListAdapter() {
        super(R.layout.item_news_more_video);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CollectedResponse collectedResponse) {
        NewsResponse news = collectedResponse.getNews();
        // 所有 ViewType 都有的信息
        baseViewHolder.setText(R.id.newsTitleTv, news.getTitle());
        baseViewHolder.setText(R.id.newsAuthorTv, news.getPublisher().getNickname());
        baseViewHolder.setText(R.id.newsTimeTv, news.getCreatedAt());
        baseViewHolder.setText(R.id.newsVideoDurationTv, news.getVideoTime());
        RoundedImageView riv = baseViewHolder.findView(R.id.newsIconIv);
        List<String> coverImages = news.getCoverImages();
        if (coverImages.size() > 0) {
            GlideUtil.load(getContext(), coverImages.get(0), riv);
        } else {
            GlideUtil.load(getContext(), "", riv);
        }
    }
}

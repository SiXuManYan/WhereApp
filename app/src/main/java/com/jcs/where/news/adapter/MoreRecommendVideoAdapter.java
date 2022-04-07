package com.jcs.where.news.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2021/1/28 11:01 上午
 */
public class MoreRecommendVideoAdapter extends BaseQuickAdapter<NewsResponse, BaseViewHolder> {
    public MoreRecommendVideoAdapter() {
        super(R.layout.item_news_more_video);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsResponse newsResponse) {
        baseViewHolder.setText(R.id.newsTitleTv, newsResponse.getTitle());
        baseViewHolder.setText(R.id.newsAuthorTv, newsResponse.getPublisher().getNickname());
        baseViewHolder.setText(R.id.newsTimeTv, newsResponse.getCreatedAt());
        baseViewHolder.setText(R.id.newsVideoDurationTv, newsResponse.getVideoTime());
        RoundedImageView riv = baseViewHolder.findView(R.id.newsIconIv);
        List<String> coverImages = newsResponse.getCoverImages();
        if (coverImages.size() > 0) {
            GlideUtil.load(getContext(), coverImages.get(0), riv);
        } else {
            GlideUtil.load(getContext(), "", riv);
        }
    }
}

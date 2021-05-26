package com.jcs.where.news.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.news.view_type.NewsType;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.JcsVideoPlayer;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * author : hwd
 * date   : 2021/1/6-23:45
 */
public class NewsFragmentAdapter extends BaseMultiItemQuickAdapter<NewsResponse, BaseViewHolder> implements LoadMoreModule {

    public NewsFragmentAdapter() {

        addItemType(NewsType.TEXT, R.layout.item_news_normal_text);
        addItemType(NewsType.SINGLE_IMAGE, R.layout.item_news_single_image);
        addItemType(NewsType.THREE_IMAGE, R.layout.item_news_three_image);
        addItemType(NewsType.VIDEO, R.layout.item_news_video);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsResponse newsResponse) {
        // 所有 ViewType 都有的信息
        baseViewHolder.setText(R.id.newsTitleTv, newsResponse.getTitle());
        baseViewHolder.setText(R.id.newsAuthorTv, newsResponse.getPublisher().getNickname());
        baseViewHolder.setText(R.id.newsTimeTv, newsResponse.getCreatedAt());

        List<String> images = newsResponse.getCoverImages();
        switch (baseViewHolder.getItemViewType()) {
            case NewsType.TEXT:
                break;
            case NewsType.SINGLE_IMAGE:
                RoundedImageView riv = baseViewHolder.findView(R.id.newsIconIv);
                GlideUtil.load(getContext(), images.get(0), riv);
                break;
            case NewsType.THREE_IMAGE:
                dealThreeImages(images, baseViewHolder);
                break;
            case NewsType.VIDEO:

                JcsVideoPlayer videoPlayer = baseViewHolder.findView(R.id.newsVideoPlayer);
                if (videoPlayer != null) {
                    videoPlayer.setUp(newsResponse.getVideoLink(), newsResponse.getTitle());
                    GlideUtil.load(getContext(), images.get(0), videoPlayer.posterImageView);
                    baseViewHolder.setText(R.id.newsVideoDurationTv, newsResponse.getVideoTime());
                }
                break;
            default:
                break;
        }
    }

    public void dealThreeImages(List<String> images, BaseViewHolder holder) {

        RoundedImageView first = holder.getView(R.id.newsIconFirstIv);
        RoundedImageView second = holder.getView(R.id.newsIconSecondIv);
        RoundedImageView third = holder.getView(R.id.newsIconThirdIv);

        int size = images.size();

        for (int i = 0; i < size; i++) {
            if (i == 0) {
                GlideUtil.load(getContext(), images.get(i), first);
            }
            if (i == 1) {
                GlideUtil.load(getContext(), images.get(i), second);
            }
            if (i == 2) {
                GlideUtil.load(getContext(), images.get(i), third);
            }
        }
    }
}

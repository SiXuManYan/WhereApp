package com.jcs.where.mine.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.news.view_type.NewsType;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.JcsVideoPlayer;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2021/1/31 7:46 下午
 */
public class ArticleListAdapter  extends BaseMultiItemQuickAdapter<CollectedResponse, BaseViewHolder> implements LoadMoreModule {
    private List<Integer> mImageResIds;

    public ArticleListAdapter() {
        // 绑定 layout 对应的 type
        addItemType(NewsType.TEXT, R.layout.item_news_normal_text);
        addItemType(NewsType.SINGLE_IMAGE, R.layout.item_news_single_image);
        addItemType(NewsType.THREE_IMAGE, R.layout.item_news_three_image);

        mImageResIds = new ArrayList<>();
        mImageResIds.add(R.id.newsIconFirstIv);
        mImageResIds.add(R.id.newsIconSecondIv);
        mImageResIds.add(R.id.newsIconThirdIv);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CollectedResponse collectedResponse) {
        NewsResponse news = collectedResponse.getNews();
        // 所有 ViewType 都有的信息
        baseViewHolder.setText(R.id.newsTitleTv, news.getTitle());
        baseViewHolder.setText(R.id.newsAuthorTv, news.getPublisher().getNickname());
        baseViewHolder.setText(R.id.newsTimeTv, news.getCreatedAt());

        List<String> images = news.getCoverImages();
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
        }
    }

    public void dealThreeImages(List<String> images, BaseViewHolder baseViewHolder) {
        int size = images.size();
        for (int i = 0; i < size; i++) {
            RoundedImageView riv = baseViewHolder.findView(mImageResIds.get(i));
            GlideUtil.load(getContext(), images.get(i), riv);
        }
    }
}

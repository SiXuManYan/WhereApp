package com.jcs.where.features.news

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.NewsResponse
import com.jcs.where.news.view_type.NewsType
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.JcsVideoPlayer
import com.makeramen.roundedimageview.RoundedImageView

/**
 * Created by Wangsw  2022/11/10 11:47.
 * 新闻列表
 */
class NewsAdapter : BaseMultiItemQuickAdapter<NewsResponse, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(NewsType.TEXT, R.layout.item_news_normal_text)
        addItemType(NewsType.SINGLE_IMAGE, R.layout.item_news_single_image)
        addItemType(NewsType.THREE_IMAGE, R.layout.item_news_three_image)
        addItemType(NewsType.VIDEO, R.layout.item_news_video)
    }


    override fun convert(holder: BaseViewHolder, item: NewsResponse) {

        holder.setText(R.id.newsTitleTv, item.title)
        holder.setText(R.id.newsAuthorTv, item.publisher.nickname)
        holder.setText(R.id.newsTimeTv, item.createdAt)

        val coverImages = item.coverImages
        when (holder.itemViewType) {
            NewsType.SINGLE_IMAGE -> {
                val newsIconIv = holder.getView<ImageView>(R.id.newsIconIv)
                if (coverImages.isNotEmpty()) {
                    GlideUtil.load(context, coverImages[0], newsIconIv, 4)
                }
            }
            NewsType.THREE_IMAGE -> {
                val first = holder.getView<RoundedImageView>(R.id.newsIconFirstIv)
                val second = holder.getView<RoundedImageView>(R.id.newsIconSecondIv)
                val third = holder.getView<RoundedImageView>(R.id.newsIconThirdIv)
                coverImages.forEachIndexed { index, s ->
                    when (index) {
                        0 -> {
                            GlideUtil.load(context, s, first)
                        }
                        1 -> {
                            GlideUtil.load(context, s, second)
                        }
                        2 -> {
                            GlideUtil.load(context, s, third)
                        }
                        else -> {
                            return@forEachIndexed
                        }
                    }
                }
            }
            NewsType.VIDEO -> {
                val videoPlayer = holder.getView<JcsVideoPlayer>(R.id.newsVideoPlayer)
                if (videoPlayer != null) {
                    videoPlayer.setUp(item.videoLink, item.title)
                    if (coverImages.isNotEmpty()) {
                        GlideUtil.load(context, coverImages[0], videoPlayer.posterImageView)
                    }
                    holder.setText(R.id.newsVideoDurationTv, item.videoTime)
                }
            }
            else -> {}
        }


    }
}
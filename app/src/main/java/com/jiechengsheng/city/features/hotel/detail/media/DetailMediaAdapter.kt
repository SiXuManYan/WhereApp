package com.jiechengsheng.city.features.hotel.detail.media

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.chrisbanes.photoview.PhotoView
import com.jiechengsheng.city.R
import com.jiechengsheng.city.features.media.MediaDetailActivity
import com.jiechengsheng.city.features.media.video.WhereVideo
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/5/19 14:44.
 */
class DetailMediaAdapter : BaseMultiItemQuickAdapter<MediaData, BaseViewHolder>() {

    init {
        addItemType(MediaData.VIDEO, R.layout.media_item_video)
        addItemType(MediaData.IMAGE, R.layout.media_item_image)
        addItemType(MediaData.IMAGE_FOR_MEDIA_DETAIL, R.layout.media_item_image_4_media_detail)
        addItemType(MediaData.VIDEO_FOR_MEDIA_DETAIL, R.layout.media_item_video)
    }

    companion object {
        @kotlin.jvm.JvmField
        var TAG: String = "tangtang"
    }


    override fun convert(holder: BaseViewHolder, item: MediaData) {

        when (holder.itemViewType) {
            MediaData.VIDEO -> convertVideo(holder, item)
            MediaData.IMAGE -> convertImage(holder, item)
            MediaData.IMAGE_FOR_MEDIA_DETAIL -> convertMediaImage(holder, item)
            MediaData.VIDEO_FOR_MEDIA_DETAIL -> convertMediaVideo(holder, item)
        }

    }


    /**
     * 详情页图片
     */
    private fun convertImage(holder: BaseViewHolder, item: MediaData) {
        val imageIv = holder.getView<ImageView>(R.id.image_iv)
        GlideUtil.load(context, item.cover, imageIv)
        imageIv.setOnClickListener {
            MediaDetailActivity.navigationOnlyImage(context, holder.adapterPosition, data)
        }

    }

    /**
     * 媒体页图片
     */
    private fun convertMediaImage(holder: BaseViewHolder, item: MediaData) {
        val imagePv = holder.getView<PhotoView>(R.id.image_pv)
        imagePv.maximumScale = 5.0f
        Glide.with(context).load(item.cover).into(imagePv)
    }


    /**
     * 详情页视频
     */
    private fun convertVideo(holder: BaseViewHolder, item: MediaData) {
        val video_gsy = holder.getView<WhereVideo>(R.id.video_gsy)

        video_gsy.apply {
            loadCoverImage(item.cover,R.mipmap.ic_empty_gray)
            setUpLazy(item.src, false, null, null, "")
            titleTextView.visibility = View.GONE
            backButton.visibility = View.GONE
            playTag = TAG
            playPosition = 0
            isAutoFullWithSize = false
            isReleaseWhenLossAudio = false
            startButton.setOnClickListener {
                MediaDetailActivity.navigationOnlyVideo(context, data)
            }
            fullscreenButton.visibility = View.GONE

        }
        // 增加圆角
        /*  video_gsy.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 8f)
            }
        }*/

    }


    /**
     * 媒体页视频
     */
    private fun convertMediaVideo(holder: BaseViewHolder, item: MediaData) {
        val video_gsy = holder.getView<WhereVideo>(R.id.video_gsy)

        video_gsy.apply {
            loadCoverImage(item.cover,R.mipmap.ic_empty_gray)
            setUpLazy(item.src, false, null, null, "")
            titleTextView.visibility = View.GONE
            backButton.visibility = View.GONE
            playTag = TAG
            playPosition = 0
            isAutoFullWithSize = false
            isReleaseWhenLossAudio = false
            fullscreenButton.visibility = View.GONE
//            startButton.performClick()
        }

    }






}
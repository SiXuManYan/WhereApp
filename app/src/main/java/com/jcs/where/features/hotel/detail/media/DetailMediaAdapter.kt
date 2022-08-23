package com.jcs.where.features.hotel.detail.media

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.chrisbanes.photoview.PhotoView
import com.jcs.where.R
import com.jcs.where.features.media.MediaDetailActivity
import com.jcs.where.utils.GlideUtil
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * Created by Wangsw  2021/5/19 14:44.
 */
class DetailMediaAdapter : BaseMultiItemQuickAdapter<MediaData, BaseViewHolder>() {

     var needImageControl = false

    init {
        addItemType(MediaData.VIDEO, R.layout.media_item_video)
        addItemType(MediaData.IMAGE, R.layout.media_item_image)
        addItemType(MediaData.CONTROLLABLE_IMAGE, R.layout.media_item_image_controllable)
    }

    companion object {
        @kotlin.jvm.JvmField
        var TAG: String = "tangtang"
    }


    override fun convert(holder: BaseViewHolder, item: MediaData) {

        when (holder.itemViewType) {
            MediaData.VIDEO -> {
                initVideo(holder, item)
            }
            MediaData.IMAGE -> {
                initImage(holder, item)
            }
            MediaData.CONTROLLABLE_IMAGE ->{
                initImageControllable(holder, item)
            }

        }

    }

    private fun initImage(holder: BaseViewHolder, item: MediaData) {
        val imageIv = holder.getView<ImageView>(R.id.image_iv)
        GlideUtil.load(context,item.cover,imageIv)
        imageIv.setOnClickListener {
            if (needImageControl) {
                MediaDetailActivity.navigation(context ,holder.adapterPosition , data)
            }
        }

    }

    private fun initImageControllable(holder: BaseViewHolder, item: MediaData) {
        val imagePv = holder.getView<PhotoView>(R.id.image_pv)
        Glide.with(context).load(item.cover).into(imagePv)
    }


    private fun initVideo(holder: BaseViewHolder, item: MediaData) {
        val video_gsy = holder.getView<StandardGSYVideoPlayer>(R.id.video_gsy)

        // 增加封面
        val image = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        Glide.with(context).load(item.cover).into(image)
        video_gsy.thumbImageView = image

        video_gsy.setUpLazy(item.src, false, null, null, "")

        video_gsy.titleTextView.visibility = View.GONE
        video_gsy.backButton.visibility = View.GONE
        video_gsy.fullscreenButton.visibility = View.GONE
        video_gsy.playTag = TAG
        video_gsy.playPosition = 0
        video_gsy.isAutoFullWithSize = false
        video_gsy.isReleaseWhenLossAudio = false


        // 增加圆角
//        video_gsy.outlineProvider = object : ViewOutlineProvider() {
//            override fun getOutline(view: View, outline: Outline) {
//                outline.setRoundRect(0, 0, view.width, view.height, 8f)
//            }
//        }


    }
}
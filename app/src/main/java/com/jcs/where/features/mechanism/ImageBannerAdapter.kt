package com.jcs.where.features.mechanism

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R

/**
 * Created by Wangsw  2021/9/6 17:06.
 *
 */
class ImageBannerAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.media_item_image) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        Glide.with(context).load(item).into(image_iv)
    }
}
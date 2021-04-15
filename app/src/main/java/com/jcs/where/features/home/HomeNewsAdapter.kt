package com.jcs.where.features.home

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.home.NewsList
import com.jcs.where.utils.image.GlideRoundedCornersTransform


/**
 * Created by Wangsw  2021/4/14 14:14.
 *
 */
class HomeNewsAdapter : BaseQuickAdapter<NewsList, BaseViewHolder>(R.layout.item_home_news) {


    var newHeight: Int = 0

    init {
        newHeight = ScreenUtils.getScreenWidth() * 180 / 345
    }

    override fun convert(holder: BaseViewHolder, data: NewsList) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val content_tv = holder.getView<TextView>(R.id.content_tv)
        val publisher_tv = holder.getView<TextView>(R.id.publisher_tv)
        val time_tv = holder.getView<TextView>(R.id.time_tv)

        val apply = image_iv.layoutParams.apply {
            height = newHeight
        }
        image_iv.layoutParams = apply

        // 图片
        val options = RequestOptions.bitmapTransform(GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)

        if (!data.cover_images.isNullOrEmpty()) {
            Glide.with(context).load(data.cover_images[0]).apply(options).into(image_iv)
        }

        // 内容
        content_tv.text = data.title
        data.publisher?.let {
            publisher_tv.text = it.nickname
        }
        time_tv.text = data.created_at
    }

    override fun getDefItemCount(): Int {
        return super.getDefItemCount()
    }
}
package com.jcs.where.features.footprint

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.footprint.Footprint
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/18 16:48.
 * 我的足迹
 */
class FootprintAdapter : BaseQuickAdapter<Footprint, BaseViewHolder>(R.layout.item_foot_print),LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Footprint) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(15f)
            } else {
                0
            }
        }
        container.layoutParams = layoutParams

        val module = item.module_data ?: return
        loadImage(module.images, image_iv)
        title_tv.text = module.title
        holder.setText(R.id.date_tv, item.created_at)

    }


    /**
     * 图片
     */
    fun loadImage(images: ArrayList<String>, image_iv: ImageView) {
        images.forEach {
            if (it.isNotBlank()) {
                GlideUtil.load(context, it, image_iv, 4)
                return@forEach
            }
        }

    }
}
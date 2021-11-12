package com.jcs.where.features.category

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/12 15:28.
 *
 */
class CategoryChildAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_category_child) {


    override fun convert(holder: BaseViewHolder, item: Category) {
        val imageIv = holder.getView<ImageView>(R.id.image_iv)
        val content_tv = holder.getView<TextView>(R.id.content_tv)
        GlideUtil.load(context,item.icon,imageIv)
        content_tv.text = item.name
    }
}
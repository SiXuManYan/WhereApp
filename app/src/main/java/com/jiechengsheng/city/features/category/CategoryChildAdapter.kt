package com.jiechengsheng.city.features.category

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.utils.GlideUtil

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
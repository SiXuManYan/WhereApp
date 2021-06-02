package com.jcs.where.features.store.list

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/2 11:48.
 * 商城 轮播图 （一级分类）
 */
class StoreBannerAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_store_banner) {

    override fun convert(holder: BaseViewHolder, item: Category) {

        val child_parent_ll = holder.getView<LinearLayout>(R.id.child_parent_ll)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val content_tv = holder.getView<TextView>(R.id.content_tv)

        val layoutParams = child_parent_ll.layoutParams.apply {
            width = ScreenUtils.getScreenWidth() / 4
            height = SizeUtils.dp2px(72f)
        }

        child_parent_ll.layoutParams = layoutParams
        GlideUtil.load(context, item.icon, image_iv)
        content_tv.text = item.name

    }
}
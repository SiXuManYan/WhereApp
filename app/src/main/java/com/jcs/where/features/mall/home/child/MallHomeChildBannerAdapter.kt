package com.jcs.where.features.mall.home.child

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallBannerCategory
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.features.mall.second.MallSecondActivity
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/2 11:48.
 * 商城 轮播图 （二级分类）
 */
class MallHomeChildBannerAdapter : BaseQuickAdapter<MallBannerCategory, BaseViewHolder>(R.layout.item_mall_banner) {

    override fun convert(holder: BaseViewHolder, item: MallBannerCategory) {

        val first_ll = holder.getView<LinearLayout>(R.id.container_first_ll)
        val second_ll = holder.getView<LinearLayout>(R.id.container_second_ll)

        item.childItem.forEachIndexed { index, category ->
            if (index < 3) {
                bindChild(first_ll, index, category)
            } else {
                bindChild(second_ll, index - 3, category)
            }
        }

    }

    private fun bindChild(childContainer: LinearLayout, index: Int, category: MallCategory) {

        val child = childContainer.getChildAt(index)

        val child_parent_ll = child.findViewById<LinearLayout>(R.id.child_parent_ll)
        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val content_tv = child.findViewById<TextView>(R.id.content_tv)

        GlideUtil.load(context, category.icon, image_iv,1)
        content_tv.text = category.name

        child.setOnClickListener {
            MallSecondActivity.navigation(context, category.id)
        }


    }


}

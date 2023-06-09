package com.jiechengsheng.city.features.mall.home.child

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallBannerCategory
import com.jiechengsheng.city.api.response.mall.MallCategory
import com.jiechengsheng.city.features.mall.second.MallSecondActivity
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/2 11:48.
 * 商城 轮播图 （二级分类）
 */
class MallHomeChildBannerAdapter : BaseQuickAdapter<MallBannerCategory, BaseViewHolder>(R.layout.item_mall_banner) {

    override fun convert(holder: BaseViewHolder, item: MallBannerCategory) {

        val first_ll = holder.getView<LinearLayout>(R.id.container_first_ll)

        item.childItem.forEachIndexed { index, category ->
            if (index < 3) {
                bindChild(first_ll, index, category)
            }
        }

    }

    private fun bindChild(childContainer: LinearLayout, index: Int, category: MallCategory) {

        val child = childContainer.getChildAt(index)

        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val content_tv = child.findViewById<TextView>(R.id.content_tv)

        GlideUtil.load(context, category.icon, image_iv,1)
        content_tv.text = category.name

        child.visibility = View.VISIBLE
        child.setOnClickListener {
            MallSecondActivity.navigation(context, category.id)
        }


    }


}

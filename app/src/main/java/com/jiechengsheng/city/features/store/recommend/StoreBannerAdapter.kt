package com.jiechengsheng.city.features.store.recommend

import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.api.response.category.StoryBannerCategory
import com.jiechengsheng.city.features.store.filter.StoreFilterActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/2 11:48.
 * 商城 轮播图 （一级分类）
 */
class StoreBannerAdapter : BaseQuickAdapter<StoryBannerCategory, BaseViewHolder>(R.layout.item_store_banner) {

    override fun convert(holder: BaseViewHolder, item: StoryBannerCategory) {

        val first_ll = holder.getView<LinearLayout>(R.id.container_first_ll)
        val second_ll = holder.getView<LinearLayout>(R.id.container_second_ll)

        item.childItem.forEachIndexed { index, category ->
            if (index < 4) {
                bindChild(first_ll, index, category)
            } else {
                bindChild(second_ll, index - 4, category)
            }
        }

    }

    private fun bindChild(childContainer: LinearLayout, index: Int, category: Category) {

        val child = childContainer.getChildAt(index)

        val child_parent_ll = child.findViewById<LinearLayout>(R.id.child_parent_ll)
        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val content_tv = child.findViewById<TextView>(R.id.content_tv)

        GlideUtil.load(context, category.icon, image_iv)
        content_tv.text = category.name

        child.setOnClickListener {

            context.startActivity(Intent(context, StoreFilterActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Constant.PARAM_PID, category.id))
        }


    }


}

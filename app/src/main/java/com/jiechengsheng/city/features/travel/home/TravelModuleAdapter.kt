package com.jiechengsheng.city.features.travel.home

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/9/13 16:28.
 *
 */
class TravelModuleAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_travel_stay_module) {


    override fun convert(holder: BaseViewHolder, item: Category) {


        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val name_tv = holder.getView<TextView>(R.id.name_tv)

        if (item.nativeIsWebType) {
            image_iv.setImageResource(R.mipmap.ic_tourism_sector)
            name_tv.text = StringUtils.getString(R.string.tourism_management)
        } else {
            GlideUtil.load(context, item.icon, image_iv)
            name_tv.text = item.name
        }

    }
}
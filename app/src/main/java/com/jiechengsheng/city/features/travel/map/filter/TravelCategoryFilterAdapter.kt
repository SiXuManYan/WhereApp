package com.jiechengsheng.city.features.travel.map.filter

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category

/**
 * Created by Wangsw  2022/7/28 11:30.
 * 类型筛选
 */
class TravelCategoryFilterAdapter :BaseQuickAdapter<Category , BaseViewHolder>(R.layout.item_travel_filter_category) {

    override fun convert(holder: BaseViewHolder, item: Category) {
        val itemTv = holder.getView<CheckedTextView>(R.id.item_text_tv)

        itemTv.text = item.name
        itemTv.isChecked = item.nativeIsSelected

    }

}
package com.jiechengsheng.city.features.mall.home

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallCategory

/**
 * Created by Wangsw  2021/4/27 17:11.
 *
 */
class TopCategoryAdapter : BaseQuickAdapter<MallCategory, BaseViewHolder>(R.layout.item_address_time) {


    override fun convert(holder: BaseViewHolder, item: MallCategory) {

        val time_tv = holder.getView<CheckedTextView>(R.id.time_tv)

        time_tv.text = item.name
        time_tv.isChecked = item.nativeIsSelected

    }
}
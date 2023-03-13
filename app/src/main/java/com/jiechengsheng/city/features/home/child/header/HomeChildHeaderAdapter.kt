package com.jiechengsheng.city.features.home.child.header

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallCategory

/**
 * Created by Wangsw  2021/12/3 16:30.
 * 首页子分类筛选
 */
class HomeChildHeaderAdapter : BaseQuickAdapter<MallCategory,BaseViewHolder>(R.layout.item_home_child_header){

    override fun convert(holder: BaseViewHolder, item: MallCategory) {

        val child_tv = holder.getView<CheckedTextView>(R.id.child_tv)
        child_tv.text = item.name
        child_tv.isChecked = item.nativeIsSelected
    }
}
package com.jiechengsheng.city.features.mall.second

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallCategory

/**
 * Created by Wangsw  2021/12/3 16:30.
 * 商城三级分类筛选
 */
class ThirdCategoryFilterAdapter : BaseQuickAdapter<MallCategory,BaseViewHolder>(R.layout.item_mall_category_filter){

    override fun convert(holder: BaseViewHolder, item: MallCategory) {

        val child_tv = holder.getView<CheckedTextView>(R.id.child_tv)
        child_tv.text = item.name
        child_tv.isChecked = item.nativeIsSelected
    }
}
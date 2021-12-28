package com.jcs.where.features.mall.second

import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.mall.MallCategory

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
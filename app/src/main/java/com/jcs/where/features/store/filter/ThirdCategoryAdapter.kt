package com.jcs.where.features.store.filter

import android.view.ViewGroup
import android.widget.CheckedTextView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.category.Category

/**
 * Created by Wangsw  2021/6/10 17:06.
 * 三级分类
 */
class ThirdCategoryAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_store_third_category) {

    override fun convert(holder: BaseViewHolder, item: Category) {

        val third_category = holder.getView<CheckedTextView>(R.id.third_category)
        third_category.apply {
            text = item.name
            isChecked = item.nativeIsSelected
        }


        val layoutParams = third_category.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            if (holder.adapterPosition == 0) {
                layoutParams.marginStart = SizeUtils.dp2px(15f)
            } else {
                layoutParams.marginStart = 0
            }

            if (holder.adapterPosition == data.size - 1) {
                layoutParams.marginEnd = SizeUtils.dp2px(15f)
            } else {
                layoutParams.marginEnd = SizeUtils.dp2px(5f)
            }
        }


    }
}
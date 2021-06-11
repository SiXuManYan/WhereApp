package com.jcs.where.features.store.filter.classification

import android.graphics.Typeface
import android.widget.CheckedTextView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.category.Category

/**
 * Created by Wangsw  2021/6/9 15:13.
 * 二级分类列表
 */
class SecondCategoryAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_second_category) {

    override fun convert(holder: BaseViewHolder, item: Category) {

        val second_category_ctv = holder.getView<CheckedTextView>(R.id.second_category_ctv)

        second_category_ctv.apply {
            text = item.name
            if (item.nativeIsSelected) {
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2))
            } else {
                typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                setTextColor(ColorUtils.getColor(R.color.black_333333))
            }
        }

    }


}
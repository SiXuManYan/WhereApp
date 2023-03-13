package com.jiechengsheng.city.features.enterprise.adapter

import android.widget.CheckedTextView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category

/**
 * Created by Wangsw  2022/9/13 17:00.
 * 企业黄页一二级分类筛选列表
 */
class EnterpriseCategoryAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_enterprise_category) {

    var isFirstCategoryList = false


    override fun convert(holder: BaseViewHolder, item: Category) {

        val categoryTv = holder.getView<CheckedTextView>(R.id.category_ctv)
        val nativeIsSelected = item.nativeIsSelected



        categoryTv.apply {
            text = item.name
            isChecked = nativeIsSelected
        }

        if (isFirstCategoryList) {
            if (nativeIsSelected) {
                categoryTv.setBackgroundColor(ColorUtils.getColor(R.color.grey_F5F5F5))
            } else {
                categoryTv.setBackgroundColor(ColorUtils.getColor(R.color.white))
            }
            if (item.changeTextColor) {
                categoryTv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
            }
        }
    }
}
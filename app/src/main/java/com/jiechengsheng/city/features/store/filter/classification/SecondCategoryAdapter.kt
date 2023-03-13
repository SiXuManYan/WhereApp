package com.jiechengsheng.city.features.store.filter.classification

import android.graphics.Typeface
import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category

/**
 * Created by Wangsw  2021/6/9 15:13.
 * 二级分类列表
 */
class SecondCategoryAdapter : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_second_category) {

    override fun convert(holder: BaseViewHolder, item: Category) {

        val second_category_ctv = holder.getView<CheckedTextView>(R.id.second_category_ctv)

        second_category_ctv.apply {
            text = item.name
            isChecked = item.nativeIsSelected
            typeface = if (item.nativeIsSelected) {
                Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                Typeface.defaultFromStyle(Typeface.NORMAL)
            }
        }

    }


}
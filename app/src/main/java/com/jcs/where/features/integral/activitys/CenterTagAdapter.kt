package com.jcs.where.features.integral.activitys

import android.view.ViewGroup
import android.widget.CheckedTextView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.integral.IntegralTag

/**
 * Created by Wangsw  2023/6/10 17:06.
 * 企业黄页三级分类
 */
class CenterTagAdapter : BaseQuickAdapter<IntegralTag, BaseViewHolder>(R.layout.item_enterprise_third_category) {


    override fun convert(holder: BaseViewHolder, item: IntegralTag) {

        val third_category = holder.getView<CheckedTextView>(R.id.third_category)
        third_category.apply {
            text = item.title
            isChecked = item.nativeIsSelected
        }

        val layoutParams = third_category.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            if (holder.adapterPosition == 0) {
                layoutParams.marginStart = SizeUtils.dp2px(15f)
            } else {
                layoutParams.marginStart = 0
            }
        }



    }
}
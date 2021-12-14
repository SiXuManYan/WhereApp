package com.jcs.where.features.mall.detail.sku

import android.widget.CheckedTextView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallAttributeValue

/**
 * Created by Wangsw  2021/12/13 17:12.
 *
 */
class SkuSecondAdapter : BaseQuickAdapter<MallAttributeValue, BaseViewHolder>(R.layout.item_sku_second) {

    override fun convert(holder: BaseViewHolder, item: MallAttributeValue) {
        val valueTv = holder.getView<CheckedTextView>(R.id.sku_value_tv)
        valueTv.text = item.name
        when (item.nativeIsSelected) {
            0 -> {
                valueTv.isChecked = false
            }
            1 -> {
                valueTv.isChecked = true
            }
            2 -> {
                valueTv.isChecked = false
                valueTv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
            }

        }


    }
}
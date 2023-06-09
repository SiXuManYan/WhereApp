package com.jiechengsheng.city.features.mall.detail.sku

import android.widget.CheckedTextView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallAttributeValue

/**
 * Created by Wangsw  2021/12/13 17:12.
 *
 */
class SkuSecondAdapter : BaseQuickAdapter<MallAttributeValue, BaseViewHolder>(R.layout.item_sku_second) {

    override fun convert(holder: BaseViewHolder, item: MallAttributeValue) {
        val valueTv = holder.getView<CheckedTextView>(R.id.sku_value_tv)
        valueTv.text = item.name

        if (item.nativeEnable) {
            valueTv.isChecked = item.nativeSelected
        } else {
            valueTv.isChecked = false
            valueTv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
        }


    }
}
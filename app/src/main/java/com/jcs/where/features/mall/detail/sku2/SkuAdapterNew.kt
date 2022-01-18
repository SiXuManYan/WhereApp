package com.jcs.where.features.mall.detail.sku2

import android.view.LayoutInflater
import android.widget.RadioGroup
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallAttribute
import com.jcs.where.features.mall.detail.sku.TargetGoodItemClickCallBack

/**
 * Created by Wangsw  2022/1/18 15:52.
 *
 */
class SkuAdapterNew : BaseQuickAdapter<MallAttribute, BaseViewHolder>(R.layout.item_sku_first_new) {

    var targetGoodItemClickCallBack: TargetGoodItemClickCallBack? = null

    override fun convert(holder: BaseViewHolder, item: MallAttribute) {
        holder.setText(R.id.sku_title_tv, item.key)

        val radioGroup = holder.getView<RadioGroup>(R.id.group_rg)
        initChildItem(item, radioGroup)
    }

    private fun initChildItem(item: MallAttribute, radioGroup: RadioGroup) {

        radioGroup.removeAllViews()
        item.value.forEachIndexed { index, mallAttributeValue ->

            val rb = LayoutInflater.from(context).inflate(R.layout.layout_sku_value, null) as MaterialRadioButton
            rb.text = mallAttributeValue.name
            when (mallAttributeValue.nativeIsSelected) {
                0 -> {
                    rb.setTextColor(ColorUtils.getColor(R.color.selector_gray666_blue))
                    rb.isEnabled = true
                    rb.isChecked = false
                }
                1 -> {
                    rb.setTextColor(ColorUtils.getColor(R.color.selector_gray666_blue))
                    rb.isEnabled = true
                    rb.isChecked = true
                }
                2 -> {
                    rb.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
                    rb.isChecked = false
                    rb.isEnabled = false
                }
            }

            radioGroup.addView(rb)

        }

        for (index in 0 until radioGroup.childCount) {

            val rb = radioGroup.getChildAt(index)
            val attrValue = item.value[index]

            rb.setOnClickListener {
                if (attrValue.nativeIsSelected == 2) {
                    return@setOnClickListener
                }
                if (attrValue.nativeIsSelected == 0) {
                    attrValue.nativeIsSelected = 1
                } else {
                    attrValue.nativeIsSelected = 0
                }
                targetGoodItemClickCallBack?.onItemClick2(attrValue)
            }

        }

//


    }
}
package com.jcs.where.features.bills.charges

import android.widget.CheckedTextView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ResourceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.bean.FaceValue

/**
 * Created by Wangsw  2022/6/16 15:38.
 *
 */
class CallChargesAdapter :BaseQuickAdapter<FaceValue,BaseViewHolder>(R.layout.item_face_value) {

    override fun convert(holder: BaseViewHolder, item: FaceValue) {

        val container = holder.getView<LinearLayout>(R.id.container_ll)
        val faceValueTv = holder.getView<CheckedTextView>(R.id.face_value_tv)
        val phpTv = holder.getView<CheckedTextView>(R.id.php_tv)

        faceValueTv.text = item.value.toString()

        val isSelected = item.isSelected
        faceValueTv.isChecked = isSelected
        phpTv.isChecked = isSelected


        if (isSelected) {
            container.setBackgroundResource(R.drawable.shape_blue_radius_8)
        }else {
            container.setBackgroundResource(R.drawable.shape_gray_radius_8)
        }


    }
}
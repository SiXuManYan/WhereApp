package com.jcs.where.features.bills.form

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.bills.FieldDetail


/**
 * Created by Wangsw  2022/6/9 10:40.
 * 字段列表
 */
class BillsFormAdapter : BaseQuickAdapter<FieldDetail, BaseViewHolder>(R.layout.item_form_bills) {

    override fun convert(holder: BaseViewHolder, item: FieldDetail) {
        val title = holder.getView<TextView>(R.id.title_tv)
        val form = holder.getView<AppCompatEditText>(R.id.content_et)

        title.text = item.Tag
        form.hint = item.Caption
        form.filters = arrayOf<InputFilter>(LengthFilter(item.Width))
        form.addTextChangedListener(
            afterTextChanged = {
                item.nativeUserInput = it.toString().trim()
            }
        )
    }
}
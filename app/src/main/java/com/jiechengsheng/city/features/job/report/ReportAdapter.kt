package com.jiechengsheng.city.features.job.report

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.Report

/**
 * Created by Wangsw  2022/10/28 9:53.
 * 举报原因
 */
class ReportAdapter : BaseQuickAdapter<Report, BaseViewHolder>(R.layout.item_report) {


    override fun convert(holder: BaseViewHolder, item: Report) {

        holder.setText(R.id.content_tv, item.title)
        val selected_iv = holder.getView<ImageView>(R.id.selected_iv)

        if (item.nativeIsSelected) {
            selected_iv.setImageResource(R.mipmap.ic_checked_purple)
        } else {
            selected_iv.setImageResource(R.mipmap.ic_un_checked)
        }

    }


}
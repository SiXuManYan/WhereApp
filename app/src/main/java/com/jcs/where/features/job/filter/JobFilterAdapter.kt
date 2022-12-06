package com.jcs.where.features.job.filter

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.FilterItem

/**
 * Created by Wangsw  2022/12/6 10:43.
 * 筛选项
 */
class JobFilterAdapter : BaseQuickAdapter<FilterItem , BaseViewHolder>(R.layout.item_job_filter_item) {

    override fun convert(holder: BaseViewHolder, item: FilterItem) {
        val filterItemView = holder.getView<CheckedTextView>(R.id.filter_item_ctv)
        filterItemView.text = item.name
        filterItemView.isChecked = item.nativeSelected

    }
}
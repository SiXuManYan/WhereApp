package com.jcs.where.features.feedback.record

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.feedback.FeedbackRecord

/**
 * Created by Wangsw  2022/10/14 14:27.
 *
 */
class FeedbackRecordAdapter : BaseQuickAdapter<FeedbackRecord, BaseViewHolder>(R.layout.item_feedback_record),LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: FeedbackRecord) {
        holder.setText(R.id.content_tv, item.content)
        holder.setText(R.id.time_tv, item.time)
    }
}
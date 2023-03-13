package com.jiechengsheng.city.features.feedback.home

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.feedback.FeedbackCategoryAndQuestion

/**
 * Created by Wangsw  2022/10/12 16:36.
 * 意见反馈  分类 + 问题列表
 */
class CategoryQuestionAdapter : BaseQuickAdapter<FeedbackCategoryAndQuestion, BaseViewHolder>(R.layout.item_feedback_category) {
    override fun convert(holder: BaseViewHolder, item: FeedbackCategoryAndQuestion) {
        holder.setText(R.id.name_tv, item.name)
    }
}
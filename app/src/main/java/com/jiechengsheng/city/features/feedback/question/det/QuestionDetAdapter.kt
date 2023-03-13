package com.jiechengsheng.city.features.feedback.question.det

import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ResourceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.feedback.FeedbackQuestionTab

/**
 * Created by Wangsw  2022/10/13 14:21.
 *
 */
class QuestionDetAdapter : BaseQuickAdapter<FeedbackQuestionTab, BaseViewHolder>(R.layout.item_question_tab) {

    override fun convert(holder: BaseViewHolder, item: FeedbackQuestionTab) {
        val parent_ll = holder.getView<LinearLayout>(R.id.parent_ll)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val handle_tv = holder.getView<CheckedTextView>(R.id.handle_tv)

        if (item.id == 0) {
            handle_tv.setText(R.string.solved)
        } else {
            handle_tv.setText(R.string.unsolved)
        }

        val nativeSelected = item.nativeSelected
        handle_tv.isChecked = nativeSelected
        if (nativeSelected) {
            parent_ll.background = ResourceUtils.getDrawable(R.drawable.stock_blue_radius_12)
            if (item.id == 0) {
                image_iv.setImageResource(R.mipmap.ic_question_solved_blue)
            } else {
                image_iv.setImageResource(R.mipmap.ic_question_unsolved_blue)
            }
        } else {
            parent_ll.background = ResourceUtils.getDrawable(R.drawable.shape_grey_radius_12)
            if (item.id == 0) {
                image_iv.setImageResource(R.mipmap.ic_question_solved_gray)
            } else {
                image_iv.setImageResource(R.mipmap.ic_question_unsolved_gray)
            }

        }


    }
}
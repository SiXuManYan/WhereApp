package com.jcs.where.widget.calendar

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.entity.SectionEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.widget.calendar.JcsCalendarAdapter.CalendarBean
import java.io.Serializable

class JcsCalendarAdapter(sectionHeadResId: Int, layoutResId: Int, data: List<CalendarBean>) : BaseSectionQuickAdapter<CalendarBean, BaseViewHolder>(sectionHeadResId, layoutResId, data.toMutableList()) {
    var isEndSelected = false

    override fun convertHeader(helper: BaseViewHolder, item: CalendarBean) {
        helper.setText(R.id.yearMonthTv, item.showYearMonthDate)
    }

    override fun convert(holder: BaseViewHolder, item: CalendarBean) {


        val dateView = holder.getView<View>( R.id.dateView)
        val leftView = holder.getView<View>( R.id.leftView)
        val rightView = holder.getView<View>( R.id.rightView)
        val actionTv = holder.getView<TextView>( R.id.actionTv)


        // day 为 0 ，说明是空白item
        if (item.day != 0) {
            holder.setText(R.id.dayTv, item.day.toString())
            val itemView = holder.itemView
            if (item.isStartDay) {
                if (isEndSelected) {
                    rightView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF))
                }
                dateView.setBackgroundResource(R.mipmap.start_date_selected)
                actionTv.setText(R.string.calendar_enter_stay)
            } else if (item.isEndDay) {
                leftView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF))
                dateView.setBackgroundResource(R.mipmap.end_date_selected)
                actionTv.setText(R.string.calendar_leave_stay)
            } else if (item.isSelected) {
                leftView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF))
                rightView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF))
                dateView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF))
                actionTv.text = ""
            } else {
                actionTv.text = ""
                dateView.setBackgroundResource(0)
                leftView.setBackgroundColor(Color.WHITE)
                rightView.setBackgroundColor(Color.WHITE)
            }
        } else {
            holder.setText(R.id.dayTv, "")
            dateView.setBackgroundResource(0)
            dateView.setBackgroundColor(Color.WHITE)
            leftView.setBackgroundColor(Color.WHITE)
            rightView.setBackgroundColor(Color.WHITE)
        }
    }

    class CalendarBean : SectionEntity, Serializable {
        var time: Long = 0

        /**
         * 中文：2021年10月
         * en: Oct 2021
         */
        var showYearMonthDate: String? = null

        /**
         * 中文：9月10日
         * en: Oct 22
         */
        var showMonthDayDate: String? = null

        /**
         * 中文：09-10
         * en: OCT 22
         */
        var showMonthDayDateWithSplit: String? = null

        /**
         * 中文：2021-01-10
         * en: 2021-10-11
         */
        var showYearMonthDayDateWithSplit: String = ""

        /**
         * 中英文：Fri
         */
        var showWeekday: String? = null
        var year = 0
        var month = 0
        var day = 0
        var isSelected = false
        var isStartDay = false
        var isEndDay = false
        var isToday = false
        var mIsHeader = false


        override val isHeader: Boolean
            get() = mIsHeader


//
//        override var itemType = 0
//
//        override var isHeader = false

    }
}
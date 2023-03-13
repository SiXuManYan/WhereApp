package com.jiechengsheng.city.features.job.time

import android.widget.CheckedTextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import java.util.*

/**
 * Created by Wangsw  2022/10/24 11:24.
 *
 */
class WorkTimeAdapter : BaseQuickAdapter<WorkTime, BaseViewHolder>(R.layout.item_year) {


    override fun convert(holder: BaseViewHolder, item: WorkTime) {
        val time_tv = holder.getView<CheckedTextView>(R.id.time_tv)

        time_tv.text = item.name
        time_tv.isChecked = item.isSelected
    }


}


class WorkTime {

    var name = ""
    var isSelected = false
    var monthIndex = 0

}

object WorkTimeUtil {

    /**
     * 获取所有月份
     */
    fun getAllMonth(): ArrayList<WorkTime> {
        val allMonth = ArrayList<WorkTime>()

        val month = StringUtils.getStringArray(R.array.month)

        month.forEachIndexed { index, s ->
            allMonth.add(WorkTime().apply {
                this.name = s
                this.monthIndex = index
            })
        }


        allMonth[0].isSelected = true

        return allMonth
    }

    /**
     * 获取所有年份，下限：当前年份倒推50年
     */
    fun getAllYear(): ArrayList<WorkTime> {
        val allYear = ArrayList<WorkTime>()
        val ca = Calendar.getInstance()
        var nowYear = ca[Calendar.YEAR]
        if (nowYear < 50) {
            nowYear = 2022
        }
        for (index in nowYear downTo (nowYear - 50)) {
            allYear.add(WorkTime().apply {
                this.name = index.toString()
            })
        }

        allYear[0].isSelected = true

        return allYear
    }

    public fun getDefaultMonth(): String {
        val ca = Calendar.getInstance()
        val nowMonth = ca[Calendar.MONTH]

        val month = StringUtils.getStringArray(R.array.month)
        return month[nowMonth]
    }


}
package com.jcs.where.features.job.filter

import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.FilterItem
import com.jcs.where.api.response.job.JobFilter

/**
 * Created by Wangsw  2022/12/1 14:58.
 *
 */
interface JobFilterView : BaseMvpView {
    fun bindFilterItem(response: JobFilter, salaryType: ArrayList<FilterItem>)

}

class JobFilterPresenter(private var view: JobFilterView) : BaseMvpPresenter(view) {


    fun getFilterItem() {
        requestApi(mRetrofit.filterItem, object : BaseMvpObserver<JobFilter>(view) {
            override fun onSuccess(response: JobFilter) {

                // 薪资类型
                val salaryType = ArrayList<FilterItem>()
                for (index in 0..3) {
                    val apply = FilterItem().apply {
                        id = index
                        nativeSelected = index == 0
                        name = when (index) {
                            0 -> StringUtils.getString(R.string.unlimited)
                            1 -> StringUtils.getString(R.string.monthly_salary)
                            2 -> StringUtils.getString(R.string.daily_salary)
                            3 -> StringUtils.getString(R.string.hourly_salary)
//                            4 -> StringUtils.getString(R.string.negotiable)
                            else -> ""
                        }
                    }
                    salaryType.add(apply)
                }

                val unlimited = FilterItem().apply {
                    id = 0
                    name = StringUtils.getString(R.string.unlimited)
                    nativeSelected = true
                }
                response.area.add(0, unlimited)
                response.companyType.add(0, unlimited)
                response.jobResumeEducationLevel.add(0, unlimited)
                response.experience.add(0, unlimited)

                view.bindFilterItem(response, salaryType)
            }
        })
    }


    /**
     * 处理多选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun handleMultipleChoice(position: Int, adapter: JobFilterAdapter, resultList: ArrayList<Int>) {
        val item = adapter.data[position]

        val itemId = item.id
        if (itemId == 0) {

            if (!item.nativeSelected) {
                // 处理点击全部
                resultList.clear()

                adapter.data.forEach {
                    it.nativeSelected = it.id == 0
                }
                adapter.notifyDataSetChanged()
            }

        } else {
            // 其他项

            if (item.nativeSelected) {
                // 取消选中
                adapter.data[position].nativeSelected = false
                adapter.notifyItemChanged(position)
                resultList.remove(resultList.firstOrNull { it == itemId })

                if (resultList.isEmpty()) {
                    adapter.data[0].nativeSelected = true
                    adapter.notifyItemChanged(0)
                }
            } else {
                // 选中
                adapter.data[0].nativeSelected = false
                adapter.data[position].nativeSelected = true

                adapter.notifyDataSetChanged()

                resultList.add(itemId)

            }
        }
    }

    fun setEditAble(minSalaryEt: AppCompatEditText, editAble: Boolean) {
        if (editAble) {
            minSalaryEt.isFocusable = true
            minSalaryEt.isFocusableInTouchMode = true
            minSalaryEt.isCursorVisible = true
            minSalaryEt.setBackgroundResource(R.drawable.stock_gray_radius_22)
        } else {
            minSalaryEt.isFocusable = false
            minSalaryEt.isFocusableInTouchMode = false
            minSalaryEt.isCursorVisible = false
            minSalaryEt.setBackgroundResource(R.drawable.stock_gray_radius_22_dark)
            minSalaryEt.setText("")
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun clearSelected(adapter: JobFilterAdapter){
        adapter.data.forEach {
            it.nativeSelected = it.id == 0
        }
        adapter.notifyDataSetChanged()
    }

}
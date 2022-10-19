package com.jcs.where.features.job.home

import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.features.job.detail.JobDetailActivity

/**
 * Created by Wangsw  2022/9/27 16:26.
 * 职位列表
 */
class JobHomeAdapter : BaseQuickAdapter<Job, BaseViewHolder>(R.layout.item_job_home), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: Job) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.salary_tv, item.salary)
        holder.setText(R.id.city_tv, item.city)
        holder.setText(R.id.create_time_tv, item.created_at)

        holder.getView<LinearLayout>(R.id.job_root_ll).setOnClickListener {
            JobDetailActivity.navigation(context, item.id)
        }
    }

}
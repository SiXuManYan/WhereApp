package com.jcs.where.features.job.home

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.Job

/**
 * Created by Wangsw  2022/9/27 16:26.
 *
 */
class JobHomeAdapter : BaseQuickAdapter<Job, BaseViewHolder>(R.layout.item_job_home), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: Job) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.salary_tv, item.salary)
        holder.setText(R.id.city_tv, item.city)
        holder.setText(R.id.create_time_tv, item.created_at)
    }

}
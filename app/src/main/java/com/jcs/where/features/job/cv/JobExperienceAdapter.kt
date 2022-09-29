package com.jcs.where.features.job.cv

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.JobExperience

/**
 * Created by Wangsw  2022/9/28 20:34.
 * 工作经历
 */
class JobExperienceAdapter : BaseQuickAdapter<JobExperience, BaseViewHolder>(R.layout.item_job_experience) {


    override fun convert(holder: BaseViewHolder, item: JobExperience) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.create_time_tv, item.start_date + "-" + item.end_date)
        holder.setText(R.id.desc_tv, item.job_desc)
    }


}
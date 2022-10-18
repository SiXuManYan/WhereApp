package com.jcs.where.features.job.cv

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
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


/**
 * Created by Wangsw  2022/10/18 15:20.
 * 工作经历、教育背景
 */
class JobExperienceEduAdapter : BaseMultiItemQuickAdapter<JobExperience ,BaseViewHolder >(){

    init{
        addItemType(JobExperience.TYPE_JOB_EXPERIENCE, R.layout.item_job_experience)
        addItemType(JobExperience.TYPE_EDU_BACKGROUND, R.layout.item_job_edu_background)
        addItemType(JobExperience.TYPE_TITLE, R.layout.item_job_title)
    }


    override fun convert(holder: BaseViewHolder, item: JobExperience) {
        when (holder.itemViewType) {
            JobExperience.TYPE_JOB_EXPERIENCE  -> bindJobExperience(holder, item)
            JobExperience.TYPE_EDU_BACKGROUND  -> bindEduBackground(holder, item)
            JobExperience.TYPE_TITLE  -> bindTitle(holder, item)
        }
    }



    private fun bindJobExperience(holder: BaseViewHolder, item: JobExperience) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.create_time_tv, item.start_date + "-" + item.end_date)
        holder.setText(R.id.desc_tv, item.job_desc)
    }


    private fun bindEduBackground(holder: BaseViewHolder, item: JobExperience) {
        holder.setText(R.id.school_tv, item.educational_level)
        holder.setText(R.id.span_tv, item.extend_title)

    }

    private fun bindTitle(holder: BaseViewHolder, item: JobExperience) {
        holder.setText(R.id.title_tv, item.nativeTitleValue)
    }




}
package com.jcs.where.features.job.home

import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.features.job.detail.JobDetailActivity
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform

/**
 * Created by Wangsw  2022/9/27 16:26.
 * 职位列表
 */
class JobHomeAdapter : BaseQuickAdapter<Job, BaseViewHolder>(R.layout.item_job_home), LoadMoreModule {

    /**
     * 0 简历列表
     * 1 收藏列表
     */
    var type = 0

    override fun convert(holder: BaseViewHolder, item: Job) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.salary_tv, item.salary)
        holder.setText(R.id.city_tv, item.city)
        holder.setText(R.id.create_time_tv, item.created_at)

        val logoIv = holder.getView<ImageView>(R.id.logo_iv)
        GlideUtil.load(context, item.logo, logoIv,24 , GlideRoundedCornersTransform.CornerType.ALL ,R.mipmap.ic_company_default_logo )

        holder.getView<LinearLayout>(R.id.job_root_ll).setOnClickListener {
            val jobId = if (type == 0) {
                item.id
            } else {
                item.job_id
            }
            JobDetailActivity.navigation(context, jobId)
        }
    }

}
package com.jcs.where.features.job.home

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.features.job.detail.JobDetailActivity
import com.jcs.where.features.job.home.tag.JobTagAdapter
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.view.MyLayoutManager

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
        val tag_rv = holder.getView<RecyclerView>(R.id.tag_rv)

        val logoIv = holder.getView<ImageView>(R.id.logo_iv)
        GlideUtil.load(context, item.logo, logoIv, 24, GlideRoundedCornersTransform.CornerType.ALL, R.mipmap.ic_company_default_logo)

        holder.getView<LinearLayout>(R.id.job_root_ll).setOnClickListener {

            if (type == 0) {
                JobDetailActivity.navigation(context, item.id)
            } else {
                if (item.status == 1) {
                    JobDetailActivity.navigation(context, item.job_id)
                }
            }

        }

        val mTagAdapter = JobTagAdapter()
        tag_rv.apply {
            adapter = mTagAdapter
            layoutManager = MyLayoutManager()
        }
        val nativeTag = item.tag

        if (nativeTag.isNullOrEmpty()) {
            tag_rv.visibility = View.GONE
        } else {
            tag_rv.visibility = View.VISIBLE
        }
        mTagAdapter.setNewInstance(nativeTag)


    }

}
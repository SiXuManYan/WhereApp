package com.jcs.where.features.job.home

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
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

    companion object {

        /** 简历列表 */
        val TYPE_COMMON_JOB = 0

        /** 收藏列表 */
        val TYPE_COLLETION_JOB = 1

        /** 职位正常 */
        val STATUS_NORMAL = 1

        /** 职位关闭 */
        val STATUS_CLOSED = 0
    }

    /**
     * 0 简历列表
     * 1 收藏列表
     */
    var type = TYPE_COMMON_JOB

    override fun convert(holder: BaseViewHolder, item: Job) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.salary_tv, item.salary)
        holder.setText(R.id.city_tv, item.city)
        holder.setText(R.id.create_time_tv, item.created_at)
        val tag_rv = holder.getView<RecyclerView>(R.id.tag_rv)

        val logoIv = holder.getView<ImageView>(R.id.logo_iv)
        GlideUtil.load(context, item.logo, logoIv, 24, GlideRoundedCornersTransform.CornerType.ALL, R.mipmap.ic_company_default_logo)

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

        // 收藏列表
        when (type) {
            TYPE_COMMON_JOB -> {
                convertJobStyle(holder, item)
            }
            TYPE_COLLETION_JOB -> {
                convertCollectionStyle(holder, item)
            }
        }

    }


    private fun convertJobStyle(holder: BaseViewHolder, item: Job) {
        val jobClosedTv= holder.getView<TextView>(R.id.job_closed_tv)
        jobClosedTv.visibility = View.GONE
        val jobRoot = holder.getView<LinearLayout>(R.id.job_root_ll)
        jobRoot.setOnClickListener {
            JobDetailActivity.navigation(context, item.id)
        }

    }


    private fun convertCollectionStyle(holder: BaseViewHolder, item: Job) {

        val companyTv = holder.getView<TextView>(R.id.company_tv)
        val jobTv = holder.getView<TextView>(R.id.job_tv)
        val salaryTv = holder.getView<TextView>(R.id.salary_tv)
        val cityTv = holder.getView<TextView>(R.id.city_tv)
        val jobClosedTv= holder.getView<TextView>(R.id.job_closed_tv)
        val jobRoot = holder.getView<LinearLayout>(R.id.job_root_ll)

        jobClosedTv.visibility = View.VISIBLE

        val status = item.status
        jobRoot.setOnClickListener {
            if (status == STATUS_NORMAL) {
                JobDetailActivity.navigation(context, item.job_id)
            }
            if (status == STATUS_CLOSED) {
                ToastUtils.showShort(R.string.job_closed)
            }
        }

        when (status) {
            STATUS_NORMAL -> {
                companyTv.setTextColor(ColorUtils.getColor(R.color.black_333333))
                jobTv.setTextColor(ColorUtils.getColor(R.color.color_1c1380))
                salaryTv.setTextColor(ColorUtils.getColor(R.color.black_333333))
                cityTv.setTextColor(ColorUtils.getColor(R.color.grey_666666))
            }
            STATUS_CLOSED -> {
                companyTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
                jobTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
                salaryTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
                cityTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
            }
        }


    }

}
package com.jcs.where.features.job.home

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.features.job.detail.JobDetailActivity
import com.jcs.where.features.job.home.tag.JobTagAdapter
import com.jcs.where.features.job.record.result.JobAppliedResultActivity
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.view.MyLayoutManager

/**
 * Created by Wangsw  2022/9/27 16:26.
 * 职位列表
 */
class JobHomeAdapter : BaseMultiItemQuickAdapter<Job, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(Job.TYPE_COMMON_JOB, R.layout.item_job_home)
        addItemType(Job.TYPE_COLLETION_JOB, R.layout.item_job_home)
        addItemType(Job.TYPE_RECORD_APPLIED, R.layout.item_job_home)
        addItemType(Job.TYPE_RECORD_INTERVIEWS, R.layout.item_job_home)
        addItemType(Job.TYPE_TITLE, R.layout.item_foot_print_title)
    }


    override fun convert(holder: BaseViewHolder, item: Job) {

        if (holder.itemViewType != Job.TYPE_TITLE) {
            holder.setText(R.id.company_tv, item.company)
            holder.setText(R.id.job_tv, item.job_title)
            holder.setText(R.id.salary_tv, item.salary)
            holder.setText(R.id.city_tv, item.city)
            holder.setText(R.id.create_time_tv, item.created_at)
            val tag_rv = holder.getView<RecyclerView>(R.id.tag_rv)

            val logoIv = holder.getView<ImageView>(R.id.logo_iv)
            GlideUtil.load(context,
                item.logo,
                logoIv,
                24,
                GlideRoundedCornersTransform.CornerType.ALL,
                R.mipmap.ic_company_default_logo)

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

        // 收藏列表
        when (holder.itemViewType) {
            Job.TYPE_COMMON_JOB -> convertJobStyle(holder, item)
            Job.TYPE_COLLETION_JOB -> convertCollectionStyle(holder, item)
            Job.TYPE_RECORD_APPLIED,
            Job.TYPE_RECORD_INTERVIEWS,
            -> convertApplied(holder, item)
            Job.TYPE_TITLE -> {
                convertTitle(holder, item)
            }

        }

    }


    private fun convertJobStyle(holder: BaseViewHolder, item: Job) {
        val jobClosedTv = holder.getView<TextView>(R.id.job_closed_tv)
        jobClosedTv.visibility = View.GONE
        val jobRoot = holder.getView<LinearLayout>(R.id.job_root_ll)
        jobRoot.setOnClickListener {
            JobDetailActivity.navigation(context, item.id, true)
        }

    }


    private fun convertCollectionStyle(holder: BaseViewHolder, item: Job) {

        val companyTv = holder.getView<TextView>(R.id.company_tv)
        val jobTv = holder.getView<TextView>(R.id.job_tv)
        val salaryTv = holder.getView<TextView>(R.id.salary_tv)
        val cityTv = holder.getView<TextView>(R.id.city_tv)
        val jobClosedTv = holder.getView<TextView>(R.id.job_closed_tv)
        val jobRoot = holder.getView<LinearLayout>(R.id.job_root_ll)


        val status = item.status
        jobRoot.setOnClickListener {
            if (status == Job.STATUS_NORMAL) {
                JobDetailActivity.navigation(context, item.job_id)
            }
            if (status == Job.STATUS_CLOSED) {
                ToastUtils.showShort(R.string.job_closed)
            }
        }

        when (status) {
            Job.STATUS_NORMAL -> {
                jobClosedTv.visibility = View.GONE
                companyTv.setTextColor(ColorUtils.getColor(R.color.black_333333))
                jobTv.setTextColor(ColorUtils.getColor(R.color.color_1c1380))
                salaryTv.setTextColor(ColorUtils.getColor(R.color.black_333333))
                cityTv.setTextColor(ColorUtils.getColor(R.color.grey_666666))
            }
            Job.STATUS_CLOSED -> {
                jobClosedTv.visibility = View.VISIBLE
                companyTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
                jobTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
                salaryTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
                cityTv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
            }
        }

    }


    private fun convertApplied(holder: BaseViewHolder, item: Job) {
        val jobRoot = holder.getView<LinearLayout>(R.id.job_root_ll)
        val statusRl = holder.getView<RelativeLayout>(R.id.applied_status_rl)
        val statusIv = holder.getView<ImageView>(R.id.applied_status_iv)
        val statusTv = holder.getView<TextView>(R.id.applied_status_tv)
        jobRoot.setBackgroundColor(ColorUtils.getColor(R.color.white))
        statusRl.visibility = View.VISIBLE

        /**
         * 1已申请 2申请失败 3待面试 4面试成功 5面试失败
         */
        val status = item.status

        when (status) {
            Job.STATUS_APPLIED -> {
                statusRl.setBackgroundResource(R.drawable.gradient_job_blue)
                statusIv.setImageResource(R.mipmap.ic_job_blue_applied)
                statusTv.setText(R.string.job_status_applied)
                statusTv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
            }
            Job.STATUS_APPLIED_FAILED -> {
                statusRl.setBackgroundResource(R.drawable.gradient_job_red)
                statusIv.setImageResource(R.mipmap.ic_job_failed)
                statusTv.setText(R.string.job_status_applied_failed)
                statusTv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
            }
            Job.STATUS_TO_INTERVIEWS -> {
                statusRl.setBackgroundResource(R.drawable.gradient_job_blue)
                statusIv.setImageResource(R.mipmap.ic_job_blue)
                statusTv.setText(R.string.job_status_to_interviews)
                statusTv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
            }
            Job.STATUS_INTERVIEWS_SUCCEED -> {
                statusRl.setBackgroundResource(R.drawable.gradient_job_green)
                statusIv.setImageResource(R.mipmap.ic_job_green)
                statusTv.setText(R.string.job_status_interviews_succeed)
                statusTv.setTextColor(ColorUtils.getColor(R.color.color_green_2FD878))
            }
            Job.STATUS_INTERVIEWS_FAILED -> {
                statusRl.setBackgroundResource(R.drawable.gradient_job_red)
                statusIv.setImageResource(R.mipmap.ic_job_failed)
                statusTv.setText(R.string.job_status_interviews_failed)
                statusTv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
            }
        }

        jobRoot.setOnClickListener {
            JobAppliedResultActivity.navigation(context, status)
        }
    }


    private fun convertTitle(holder: BaseViewHolder, item: Job) {
        holder.setText(R.id.title_tv, item.created_at)
    }


}
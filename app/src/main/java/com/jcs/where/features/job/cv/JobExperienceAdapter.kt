package com.jcs.where.features.job.cv

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.features.job.form.certificate.CertificateFromActivity
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.list.DividerDecoration

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
class JobExperienceEduAdapter : BaseMultiItemQuickAdapter<JobExperience, BaseViewHolder>() {

    init {
        addItemType(JobExperience.TYPE_TITLE, R.layout.item_job_title)
        addItemType(JobExperience.TYPE_JOB_EXPERIENCE, R.layout.item_job_experience)
        addItemType(JobExperience.TYPE_EDU_BACKGROUND, R.layout.item_job_edu_background)
        addItemType(JobExperience.TYPE_CERTIFICATION, R.layout.item_job_certification)
    }


    override fun convert(holder: BaseViewHolder, item: JobExperience) {
        when (holder.itemViewType) {
            JobExperience.TYPE_TITLE -> bindTitle(holder, item)
            JobExperience.TYPE_JOB_EXPERIENCE -> bindJobExperience(holder, item)
            JobExperience.TYPE_EDU_BACKGROUND -> bindEduBackground(holder, item)
            JobExperience.TYPE_CERTIFICATION -> bindCertification(holder, item)
        }
    }


    private fun bindTitle(holder: BaseViewHolder, item: JobExperience) {
        holder.setText(R.id.title_tv, item.nativeTitleValue)
    }


    private fun bindJobExperience(holder: BaseViewHolder, item: JobExperience) {

        holder.setText(R.id.company_tv, item.company)
        holder.setText(R.id.job_tv, item.job_title)
        holder.setText(R.id.create_time_tv, item.start_date + "-" + item.end_date)
        holder.setText(R.id.desc_tv, item.job_desc)
    }


    private fun bindEduBackground(holder: BaseViewHolder, item: JobExperience) {
        holder.setText(R.id.school_tv, item.educational_attainment)
        holder.setText(R.id.degree_tv, item.educational_level)

        val courseTv = holder.getView<TextView>(R.id.course_tv)

        val course = item.vocational_course
        if (course.isNotBlank()) {
            courseTv.visibility = View.VISIBLE
            courseTv.text = course
        } else {
            courseTv.visibility = View.GONE
        }
    }

    private fun bindCertification(holder: BaseViewHolder, item: JobExperience) {

        val imageAdapter = CertificationPhotoAdapter()

        imageAdapter.setOnItemClickListener { _, _, _ ->
            CertificateFromActivity.navigation(context, item)
        }

        holder.setText(R.id.title_tv, item.title)
        val certificateRv = holder.getView<RecyclerView>(R.id.certificate_rv)

        certificateRv.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f)))
        }
        imageAdapter.setNewInstance(item.images)

    }

}

class CertificationPhotoAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_job_certification_photo) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val imageIv = holder.getView<ImageView>(R.id.image_iv)
        GlideUtil.load(context, item, imageIv, 5)

    }

}
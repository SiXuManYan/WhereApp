package com.jcs.where.features.job.pdf

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.api.response.job.ProfileDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.cv.JobExperienceEduAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.time.TimeUtil
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_cv_pdf_preview.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2023/2/23 14:27.
 * pdf预览
 */
class CvPdfActivity : BaseMvpActivity<CvPdfPresenter>(), CvPdfView {

    /** 个人信息 */
    private var profileDetail: ProfileDetail? = null
    private var jobData = ArrayList<JobExperience>()
    private lateinit var header: PdfHeader
    private lateinit var mAdapter: JobExperienceEduAdapter
    private var degreeDialog: BottomSheetDialog? = null

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_pdf_preview

    override fun initView() {
        initExtra()
        initContent()
    }


    private fun initExtra() {
        intent?.extras?.let {
            profileDetail = it.getParcelable<ProfileDetail>(Constant.PARAM_DATA)
            val cvInfo = it.getParcelableArrayList<JobExperience>(Constant.PARAM_CV)
            val toJson = Gson().toJson(cvInfo)
            Log.d("json", toJson)

            if (!cvInfo.isNullOrEmpty()) {
                jobData.addAll(cvInfo)
            }
        }

        jobData.forEach {
            // 更改列表类型
            when (it.nativeItemViewType) {
                JobExperience.TYPE_TITLE ->
                    it.nativeItemViewType = JobExperience.TYPE_TITLE_PDF

                JobExperience.TYPE_JOB_EXPERIENCE ->
                    it.nativeItemViewType = JobExperience.TYPE_JOB_EXPERIENCE_PDF

                JobExperience.TYPE_EDU_BACKGROUND ->
                    it.nativeItemViewType = JobExperience.TYPE_EDU_BACKGROUND_PDF

                JobExperience.TYPE_CERTIFICATION ->
                    it.nativeItemViewType = JobExperience.TYPE_CERTIFICATION_PDF
                else -> {}
            }

        }


    }

    private fun initContent() {

        header = PdfHeader(this)
        mAdapter = JobExperienceEduAdapter()
        mAdapter.addHeaderView(header)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5),
                1,
                0, 0))
        }

        // 个人信息
        profileDetail?.let {
            header.apply {

                GlideUtil.load(this@CvPdfActivity, it.avatar, avatar, 48)

                (it.first_name + it.last_name).also { name.text = it }

                try {
                    val split = it.birthday.split(".")

                    val ageStr = TimeUtil.getAge(split[0].toInt(), split[1].toInt(), split[2].toInt())
                    age.text = getString(R.string.age_format, ageStr)

                } catch (e: Exception) {

                }

                val gender = when (it.gender) {
                    1 -> getString(R.string.male)
                    2 -> getString(R.string.female)
                    else -> ""
                }

                val marry = when (it.civil_status) {
                    0 -> getString(R.string.single)
                    1 -> getString(R.string.married)
                    else -> ""
                }

                (gender + " | " + it.city + " | " + marry).also { info.text = it }
                phone.text = it.contact_number
                email.text = it.email

            }
        }

        // 简历
        mAdapter.setNewInstance(jobData)

    }

    override fun initData() {
        presenter = CvPdfPresenter(this)

    }

    override fun bindListener() {
        apply_tv.setOnClickListener {
            presenter.generatePdf()
        }
    }

    override fun generateSuccess() {
        if (degreeDialog != null) {
            degreeDialog?.show()
        } else {
            showDegree()
        }
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_CV_CERTIFICATE))
    }

    private fun showDegree() {
        val timeDialog = BottomSheetDialog(this)
        this.degreeDialog = timeDialog
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_pdf_generate, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            timeDialog.dismiss()
        }
        view.findViewById<Button>(R.id.ok).setOnClickListener {
            timeDialog.dismiss()
            finish()
        }
        timeDialog.show()
    }

}
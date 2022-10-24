package com.jcs.where.features.job.form.job

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.job.CreateJobExperience
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.CvFormPresenter
import com.jcs.where.features.job.form.CvFormView
import com.jcs.where.features.job.time.WorkTimeAdapter
import com.jcs.where.features.job.time.WorkTimeUtil
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.OnWorkTimeSelected
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_cv_experience.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2022/9/29 15:00.
 *  简历-工作经历表单
 */
class CvFormJobExperienceActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView {


    /**
     * 个人信息id， 不为0时为修改
     */
    private var draftId = 0
    private var draftData: JobExperience? = null
    private var requiredEdit = ArrayList<AppCompatEditText>()

    private var workTimeDialog: BottomSheetDialog? = null


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_experience

    override fun initView() {
        draftData = intent.getParcelableExtra(Constant.PARAM_DATA)
        initDraft()


    }

    private fun initDraft() {
        draftData?.let {
            draftId = it.id
            company_name_et.setText(it.company)
            job_title_et.setText(it.job_title)
            job_desc_et.setText(it.job_desc)
            start_date_tv.text = it.start_date
            end_date_tv.text = it.end_date
            new_experience_iv.visibility = View.VISIBLE
        }

    }

    override fun initData() {
        presenter = CvFormPresenter(this)
        requiredEdit.add(company_name_et)
        requiredEdit.add(job_title_et)
        requiredEdit.add(job_desc_et)

    }

    override fun bindListener() {
        start_date_tv.setOnClickListener {
            BusinessUtils.showWorkDialog2(this,object : OnWorkTimeSelected {
                override fun onWorkTimeSelected(string: String) {
                    start_date_tv.text = string
                }
            })
        }

        end_date_tv.setOnClickListener {

            BusinessUtils.showWorkDialog2(this,object : OnWorkTimeSelected {
                override fun onWorkTimeSelected(string: String) {
                    end_date_tv.text = string
                }
            })
        }

        save_tv.setOnClickListener {

            requiredEdit.forEach {
                if (it.text.isNullOrBlank()) {
                    ToastUtils.showShort(R.string.please_enter)
                    return@setOnClickListener
                }
            }

            if (start_date_tv.text.isNullOrBlank() || end_date_tv.text.isNullOrBlank()) {
                ToastUtils.showShort("Please select date")
                return@setOnClickListener
            }


            val apply = CreateJobExperience().apply {
                company = company_name_et.text.toString().trim()
                job_title = job_title_et.text.toString().trim()
                start_date = start_date_tv.text.toString().trim()
                end_date = end_date_tv.text.toString().trim()
                job_desc = job_desc_et.text.toString().trim()

            }

            presenter.handleExperiences(draftId, apply)
        }

        new_experience_iv.setOnClickListener {
            // 添加工作经历
            startActivity(CvFormJobExperienceActivity::class.java)
        }

    }


    private fun selectBirthday(textView: TextView) {
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog =
            DatePickerDialog(this, R.style.DatePickerDialogTheme, { _, year, month, dayOfMonth ->
                textView.text = getString(R.string.date_format, year, (month + 1), dayOfMonth)
            }, mYear, mMonth, mDay)

        // 设置日期范围
        val datePicker = datePickerDialog.datePicker
        // 上限
        datePicker.maxDate = ca.timeInMillis


        datePickerDialog.show()
    }

    override fun handleSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_CV_EXPERIENCE))
        finish()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_CV_EXPERIENCE -> finish()
            else -> {}
        }
    }





}
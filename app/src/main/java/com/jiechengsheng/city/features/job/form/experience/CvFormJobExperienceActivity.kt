package com.jiechengsheng.city.features.job.form.experience

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.CreateJobExperience
import com.jiechengsheng.city.api.response.job.JobExperience
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.job.form.CvFormPresenter
import com.jiechengsheng.city.features.job.form.CvFormView
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.OnWorkTimeSelected
import kotlinx.android.synthetic.main.activity_job_cv_experience.*
import me.shaohui.bottomdialog.BottomDialog
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2022/9/29 15:00.
 *  简历-工作经历表单
 */
class CvFormJobExperienceActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView {


    /**
     * 工作经历id， 不为0时为修改
     */
    private var draftExperienceId = 0
    private var draftData: JobExperience? = null
    private var requiredEdit = ArrayList<AppCompatEditText>()

    private var startDialog: BottomDialog? = null
    private var endDialog: BottomDialog? = null


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_experience

    override fun initView() {
        draftData = intent.getParcelableExtra(Constant.PARAM_DATA)
        initDraft()

    }

    private fun initDraft() {
        draftData?.let {
            draftExperienceId = it.id
            company_name_et.setText(it.company)
            job_title_et.setText(it.job_title)
            job_desc_et.setText(it.job_desc)
            start_date_tv.text = it.start_date
            end_date_tv.text = it.end_date
            new_experience_iv.visibility = View.VISIBLE
            if (draftExperienceId > 0) {
                delete_tv.visibility = View.VISIBLE
            }
        }

    }

    override fun initData() {
        presenter = CvFormPresenter(this)
        requiredEdit.add(company_name_et)
        requiredEdit.add(job_title_et)
        requiredEdit.add(job_desc_et)

        startDialog = BusinessUtils.showWorkDialog(this, object : OnWorkTimeSelected {
            override fun onWorkTimeSelected(string: String) {
                start_date_tv.text = string
            }
        })

        endDialog = BusinessUtils.showWorkDialog(this, object : OnWorkTimeSelected {
            override fun onWorkTimeSelected(string: String) {
                end_date_tv.text = string
            }
        })


    }

    override fun bindListener() {

        start_date_tv.setOnClickListener {
            startDialog?.show()
        }

        end_date_tv.setOnClickListener {
            endDialog?.show()
        }


        save_tv.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {

            override fun onDebouncingClick(v: View?) {

                requiredEdit.forEach {
                    if (it.text.isNullOrBlank()) {
                        ToastUtils.showShort(R.string.please_enter)
                        return
                    }
                }

                val startDate = start_date_tv.text.toString().trim()
                val endDate = end_date_tv.text.toString().trim()
                if (startDate.isBlank() || endDate.isBlank()) {
                    ToastUtils.showShort("Please select date")
                    return
                }

                if (!BusinessUtils.checkDateLegal(startDate, endDate,"yyyy-MM")) {
                    ToastUtils.showShort(R.string.legal_time)
                    return
                }
                val apply = CreateJobExperience().apply {
                    company = company_name_et.text.toString().trim()
                    job_title = job_title_et.text.toString().trim()
                    start_date = startDate
                    end_date = endDate
                    job_desc = job_desc_et.text.toString().trim()
                }

                save_tv.isClickable = false
                Handler(Looper.getMainLooper()).postDelayed({
                    save_tv.isClickable = true
                }, 500)

                presenter.handleExperiences(draftExperienceId, apply)

            }

        })


        new_experience_iv.setOnClickListener {
            startActivity(CvFormJobExperienceActivity::class.java)
        }


        delete_tv.setOnClickListener {
            AlertDialog.Builder(this, R.style.JobAlertDialogTheme)
                .setCancelable(false)
                .setTitle(R.string.hint)
                .setMessage(R.string.delete_hint)
                .setPositiveButton(R.string.confirm) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    presenter.deleteJobExperience(draftExperienceId)
                }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show()
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

    override fun deleteJobExperienceSuccess() {
        EventBus.getDefault().post(BaseEvent(EventCode.EVENT_DELETE_CV_EXPERIENCE, draftExperienceId))
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
package com.jcs.where.features.job.form

import android.app.DatePickerDialog
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.CityPickerResponse
import com.jcs.where.api.response.job.CreateJobExperience
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.city.CvCityFragment
import com.jcs.where.features.job.form.city.OnSelectedCity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_job_cv_experience.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2022/9/29 15:00.
 *  简历-工作经历表单
 */
class CvFormJobExperienceActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView, OnSelectedCity {


    /**
     * 个人信息id， 不为0时为修改
     */
    private var draftId = 0
    private var draftData: JobExperience? = null


    private var cityId = 0
    private lateinit var cityDialog: CvCityFragment
    private var mCityData = ArrayList<CityPickerResponse.CityChild>()
    private var requiredEdit = ArrayList<AppCompatEditText>()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_experience

    override fun initView() {
        draftData = intent.getParcelableExtra(Constant.PARAM_DATA)
        cityDialog = CvCityFragment().apply {
            onSelectedCity = this@CvFormJobExperienceActivity
        }


        initDraft()
    }

    private fun initDraft() {
        draftData?.let {
            draftId = it.id
            company_name_et.setText(it.company)
            job_title_et.setText(it.job_title)
            job_desc_et.setText(it.job_desc)
            city_tv.text = it.city
            cityId = it.city_id
            start_date_tv.text = it.start_date
            end_date_tv.text = it.end_date
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
            selectBirthday(start_date_tv)
        }

        end_date_tv.setOnClickListener {
            selectBirthday(end_date_tv)
        }

        city_tv.setOnClickListener {
            cityDialog.lastCityData = mCityData
            cityDialog.lastSelectedCityId = cityId
            cityDialog.show(supportFragmentManager, cityDialog.tag)
        }

        save_tv.setOnClickListener {

            requiredEdit.forEach {
                if (it.text.isNullOrBlank()) {
                    ToastUtils.showShort(R.string.please_enter)
                    return@setOnClickListener
                }
            }

            if (city_tv.text.isNullOrBlank() || cityId == 0) {
                ToastUtils.showShort("Please select Work City")
                return@setOnClickListener
            }

            if (start_date_tv.text.isNullOrBlank() || end_date_tv.text.isNullOrBlank()) {
                ToastUtils.showShort("Please select date")
                return@setOnClickListener
            }


            val apply = CreateJobExperience().apply {
                company = company_name_et.text.toString().trim()
                job_title = job_title_et.text.toString().trim()

                city_id = cityId
                start_date = start_date_tv.text.toString().trim()
                end_date = end_date_tv.text.toString().trim()
                job_desc = job_desc_et.text.toString().trim()

            }

            presenter.handleExperiences(draftId, apply)

        }

    }

    override fun onSelectedCity(cityChild: CityPickerResponse.CityChild, allData: MutableList<CityPickerResponse.CityChild>) {
        mCityData.clear()
        mCityData.addAll(allData)
        cityId = cityChild.id.toInt()
        city_tv.text = cityChild.name
    }


    private fun selectBirthday(textView: TextView) {
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog =
            DatePickerDialog(this, R.style.DatePickerDialogTheme, { _, year, month, dayOfMonth ->
                textView.text = getString(R.string.birthday_format, year, (month + 1), dayOfMonth)
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    override fun handleSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_CV_EXPERIENCE))
        finish()
    }


}
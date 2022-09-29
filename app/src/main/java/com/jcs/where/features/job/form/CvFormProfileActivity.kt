package com.jcs.where.features.job.form

import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.CityPickerResponse
import com.jcs.where.api.response.job.CreateProfileDetail
import com.jcs.where.api.response.job.ProfileDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.city.CvCityFragment
import com.jcs.where.features.job.form.city.OnSelectedCity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_job_cv_profile.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/9/29 14:58.
 * 简历-个人信息表单
 */
class CvFormProfileActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView, OnSelectedCity {

    private var draftData: ProfileDetail? = null
    private var lastProfileId = 0
    private var userGender = 0
    private var cityId = 0

    private var requiredEdit = ArrayList<AppCompatEditText>()

    private lateinit var cityDialog: CvCityFragment


    private var mCityData = ArrayList<CityPickerResponse.CityChild>()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_profile

    override fun initView() {
        draftData = intent.getParcelableExtra(Constant.PARAM_DATA)
        cityDialog = CvCityFragment().apply {
            onSelectedCity = this@CvFormProfileActivity
        }


        initDraft()
    }

    private fun initDraft() {

        draftData?.let {
            lastProfileId = it.id
            cityId = it.city_id
            first_name_et.setText(it.first_name)
            last_name_et.setText(it.last_name)

            when (it.gender) {
                0 -> man_rb.isChecked = true
                1 -> woman_rb.isChecked = true
            }
            city_tv.text = it.city
            email_et.setText(it.email)
            contact_number_et.setText(it.contact_number)
            school_et.setText(it.school)
            major_et.setText(it.major)
            education_et.setText(it.education)
        }
    }

    override fun initData() {
        presenter = CvFormPresenter(this)
        requiredEdit.add(first_name_et)
        requiredEdit.add(last_name_et)
        requiredEdit.add(email_et)
        requiredEdit.add(contact_number_et)
        requiredEdit.add(school_et)
        requiredEdit.add(education_et)
    }

    override fun bindListener() {

        gender_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.man_rb -> {
                    userGender = 1
                }
                R.id.woman_rb -> {
                    userGender = 2
                }
                else -> {
                }
            }

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
            if (!man_rb.isChecked && !woman_rb.isChecked) {
                ToastUtils.showShort("Please select Gender")
                return@setOnClickListener
            }

            if (city_tv.text.isNullOrBlank() || cityId == 0) {
                ToastUtils.showShort("Please select Work City")
                return@setOnClickListener
            }

            val apply = CreateProfileDetail().apply {
                first_name = first_name_et.text.toString().trim()
                last_name = last_name_et.text.toString().trim()

                gender = userGender
                city_id = cityId
                email = email_et.text.toString().trim()
                contact_number = contact_number_et.text.toString().trim()
                school = school_et.text.toString().trim()
                val majorStr = major_et.text.toString().trim()
                if (majorStr.isNotBlank()) {
                    major = majorStr
                }
                education = education_et.text.toString().trim()
            }
            presenter.handleProfile(lastProfileId, apply)

        }
    }


    override fun handleSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_CV_PROFILE))
        finish()
    }


    override fun onSelectedCity(cityChild: CityPickerResponse.CityChild, allData: MutableList<CityPickerResponse.CityChild>) {
        mCityData.clear()
        mCityData.addAll(allData)
        cityId = cityChild.id.toInt()
        city_tv.text = cityChild.name

    }


}
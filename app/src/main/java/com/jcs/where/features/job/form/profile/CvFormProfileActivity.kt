package com.jcs.where.features.job.form.profile

import android.app.DatePickerDialog
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.CityPickerResponse
import com.jcs.where.api.response.job.CreateProfileDetail
import com.jcs.where.api.response.job.ProfileDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.CvFormPresenter
import com.jcs.where.features.job.form.CvFormView
import com.jcs.where.features.job.form.city.CvCityFragment
import com.jcs.where.features.job.form.city.OnSelectedCity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.OnBottomSelectedIndex
import kotlinx.android.synthetic.main.activity_job_cv_profile.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2022/9/29 14:58.
 * 简历-个人信息表单
 */
class CvFormProfileActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView, OnSelectedCity {

    private var draftData: ProfileDetail? = null

    /**
     * 个人信息id， 不为0时为修改
     */
    private var draftProfileId = 0

    /** 性别（0-未知，1-男，2-女） */
    private var userGender = 0

    /** 婚姻状况（0-未婚，1-已婚） */
    private var civilStatus = 0


    private var cityId = 0

    private var requiredEdit = ArrayList<AppCompatEditText>()

    private lateinit var cityDialog: CvCityFragment


    private var genderData = StringUtils.getStringArray(R.array.gender)
    private var civilData = StringUtils.getStringArray(R.array.civil)

    private var mCityData = ArrayList<CityPickerResponse.CityChild>()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_profile

    override fun initView() {
        draftData = intent.getParcelableExtra(Constant.PARAM_DATA)
        cityDialog = CvCityFragment().apply {
            isCancelable = false
            onSelectedCity = this@CvFormProfileActivity
        }
        initDraft()
    }

    private fun initDraft() {

        draftData?.let {
            draftProfileId = it.id
            cityId = it.city_id
            first_name_et.setText(it.first_name)
            last_name_et.setText(it.last_name)

            userGender = it.gender
            when (userGender) {
                1 -> {
                    sex_tv.text = getString(R.string.male)
                }
                2 -> sex_tv.text = getString(R.string.female)
            }

            city_tv.text = it.city
            email_et.setText(it.email)
            contact_number_et.setText(it.contact_number)
            birth_tv.text = it.birthday

            civilStatus = it.civil_status
            when (civilStatus) {
                0 -> {
                    civil_status_tv.text = getString(R.string.single)
                }
                1 -> civil_status_tv.text = getString(R.string.married)
            }


        }
    }

    override fun initData() {
        presenter = CvFormPresenter(this)
        requiredEdit.add(first_name_et)
        requiredEdit.add(last_name_et)
        requiredEdit.add(email_et)
        requiredEdit.add(contact_number_et)

    }

    override fun bindListener() {


        sex_tv.setOnClickListener {

            val index = when (userGender) {
                2 -> 1
                else -> 0
            }

            BusinessUtils.createBottomDialog(this, index, genderData, object : OnBottomSelectedIndex {
                override fun onIndexSelect(selectedIndex: Int) {
                    userGender = selectedIndex + 1
                    sex_tv.text = genderData[selectedIndex]
                }
            })
        }


        civil_status_tv.setOnClickListener {
            BusinessUtils.createBottomDialog(this, civilStatus, civilData, object : OnBottomSelectedIndex {
                override fun onIndexSelect(selectedIndex: Int) {
                    civilStatus = selectedIndex
                    civil_status_tv.text = civilData[selectedIndex]
                }
            })
        }

        birth_tv.setOnClickListener {
            selectBirthday(birth_tv)
        }


        city_tv.setOnClickListener {
            cityDialog.lastCityData = mCityData
            cityDialog.lastSelectedCityId = cityId
            cityDialog.show(supportFragmentManager, cityDialog.tag)
        }

        save_tv.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {

            override fun onDebouncingClick(v: View?) {
                save_tv.isClickable = false

                requiredEdit.forEach {
                    if (it.text.isNullOrBlank()) {
                        ToastUtils.showShort(R.string.please_enter)
                        return
                    }
                }

                if (birth_tv.text.isNullOrBlank()) {
                    ToastUtils.showShort("Please select Date of Birth")
                    return
                }

                if (userGender == 0) {
                    ToastUtils.showShort("Please select Date of Gender")
                    return
                }

                if (civil_status_tv.text.isNullOrBlank()) {
                    ToastUtils.showShort("Please select Civil Status")
                    return
                }


                if (city_tv.text.isNullOrBlank() || cityId == 0) {
                    ToastUtils.showShort("Please select Work City")
                    return
                }

                val apply = CreateProfileDetail().apply {
                    first_name = first_name_et.text.toString().trim()
                    last_name = last_name_et.text.toString().trim()

                    gender = userGender
                    city_id = cityId
                    email = email_et.text.toString().trim()
                    contact_number = contact_number_et.text.toString().trim()
                    birthday = birth_tv.text.toString().trim()
                    civil_status = civilStatus

                }
                presenter.handleProfile(draftProfileId, apply)

                Handler(Looper.getMainLooper()).postDelayed({
                    save_tv.isClickable = true
                }, 500)
            }

        })


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

    override fun onDismiss() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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


}
package com.jcs.where.features.job.employer

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.job.EmployerRequest
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_employer.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/10/25 13:55.
 * 雇主注册
 */
class EmployerActivity : BaseMvpActivity<EmployerPresenter>(), EmployerView {

    private var allEditView: ArrayList<AppCompatEditText> = ArrayList()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_employer

    override fun initView() {
        BarUtils.setStatusBarColor(this,Color.WHITE)
        val isSendEmployer = intent.getBooleanExtra(Constant.PARAM_STATUS, false)
        if (isSendEmployer) {
            form_nsv.visibility = View.GONE
            already_commit_ll.visibility = View.VISIBLE
        }else {
            form_nsv.visibility = View.VISIBLE
            already_commit_ll.visibility = View.GONE
        }
    }

    override fun initData() {
        presenter = EmployerPresenter(this)

    }

    override fun bindListener() {
        for (index in 0 until container_ll.childCount) {
            val child = container_ll.getChildAt(index)
            if (child is AppCompatEditText) {
                allEditView.add(child)
            }
        }
        allEditView.forEach {
            it.addTextChangedListener(
                afterTextChanged = {
                    val result = it.toString().trim()
                    if (result.isBlank()) {
                        BusinessUtils.setViewClickable(false, commit_tv)
                    } else {
                        val isBlank = BusinessUtils.checkEditBlank(allEditView)
                        BusinessUtils.setViewClickable(!isBlank, commit_tv)
                    }
                }
            )
        }

        commit_tv.setOnClickListener {
            val first = first_name_et.text.toString().trim()
            val middle = middle_name_et.text.toString().trim()
            val last = last_name_et.text.toString().trim()
            val number = contact_number_et.text.toString().trim()
            val email = email_et.text.toString().trim()
            val password = password_et.text.toString().trim()

            val apply = EmployerRequest().apply {
                first_name = first
                middle_name = middle
                last_name = last
                contact_number = number
                this.email = email
                init_pwd = password
            }
            presenter.applyEmployer(apply)
        }

    }

    override fun applySuccess() {
        ToastUtils.showShort(R.string.submit_success)
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_EMPLOYER_SUBMIT))
        finish()
    }

}
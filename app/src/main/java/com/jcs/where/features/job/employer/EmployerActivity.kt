package com.jcs.where.features.job.employer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
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
        BarUtils.setStatusBarColor(this, Color.WHITE)
        val isSendEmployer = intent.getBooleanExtra(Constant.PARAM_STATUS, false)
        switchContent(isSendEmployer)
    }

    private fun switchContent(isSendEmployer: Boolean) {
        if (isSendEmployer) {
            form_nsv.visibility = View.GONE
            already_commit_ll.visibility = View.VISIBLE
        } else {
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
            if (child is AppCompatEditText && child.id != R.id.middle_name_et) {
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
            val company = company_name_et.text.toString().trim()

            val apply = EmployerRequest().apply {
                first_name = first
                middle_name = middle
                last_name = last
                contact_number = number
                this.email = email
                init_pwd = password
                company_title = company
            }
            showConfirmDialog(apply)
        }

    }


    private fun showConfirmDialog(apply: EmployerRequest) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.dialog_employer_confirm, null)

        val cancelTv = view.findViewById<TextView>(R.id.cancel_tv)
        val confirmTv = view.findViewById<TextView>(R.id.confirm_tv)
        val contentTv = view.findViewById<TextView>(R.id.content_tv)
        contentTv.text = apply.email


        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


        val window: Window? = alertDialog.window
        if (window != null) {

            // 更改背景
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setContentView(view)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 更改默认宽度
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window.attributes)
            lp.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(80f)
            window.attributes = lp

        }
        cancelTv.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmTv.setOnClickListener {
            presenter.applyEmployer(apply)

            alertDialog.dismiss()
        }

    }

    override fun applySuccess() {
        // ToastUtils.showShort(R.string.submit_success)
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_EMPLOYER_SUBMIT))
        startActivity(EmployerResultActivity::class.java)
        finish()
    }


}
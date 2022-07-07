package com.jcs.where.features.refund.add.verify

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.widget.verify.VerificationCodeView
import kotlinx.android.synthetic.main.activity_verify_by_code_4_refund.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/4/26 14:46.
 * 短信验证后， 直接绑定绑定退款方式
 */
class RefundBindVerifyActivity : BaseMvpActivity<RefundBindVerifyPresenter>(), RefundBindVerifyView {

    /** 渠道名 */
    private var channelName = ""

    /**  用户名 */
    private var userName = ""

    /** 账号 */
    private var accountNumber = ""


    private var channelCode = ""
    private var channelCategory = ""

    private var verifyCode = ""

    companion object {

        fun navigation(
            context: Context,
            channelName: String,
            userName: String,
            accountNumber: String,
            channelCode: String,
            channelCategory: String,
        ) {
            val bundle = Bundle().apply {
                putString(Constant.PARAM_CHANNEL_NAME, channelName)
                putString(Constant.PARAM_REFUND_CHANNEL_CODE, channelCode)
                putString(Constant.PARAM_REFUND_CHANNEL_CATEGORY, channelCategory)
                putString(Constant.PARAM_USER_NAME, userName)
                putString(Constant.PARAM_ACCOUNT, accountNumber)
            }

            val intent = if (User.isLogon()) {
                Intent(context, RefundBindVerifyActivity::class.java).putExtras(bundle)
            } else {
                Intent(context, LoginActivity::class.java)
            }

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_verify_by_code_4_refund

    override fun initView() {
        initExtra()
        captcha_view.onCodeFinishListener = object : VerificationCodeView.OnCodeFinishListener {
            override fun onTextChange(view: View?, content: String) = Unit

            override fun onComplete(view: View?, content: String) {
                verifyCode = content
            }

        }
    }

    private fun initExtra() {
        intent.extras?.let {
            channelName = it.getString(Constant.PARAM_CHANNEL_NAME, "")
            userName = it.getString(Constant.PARAM_USER_NAME, "")
            accountNumber = it.getString(Constant.PARAM_ACCOUNT, "")
            channelCode = it.getString(Constant.PARAM_REFUND_CHANNEL_CODE, "")
            channelCategory = it.getString(Constant.PARAM_REFUND_CHANNEL_CATEGORY, "")
        }

    }

    override fun initData() {
        presenter = RefundBindVerifyPresenter(this)
        presenter.getVerifyCode(resend_tv, target_tv)
    }


    override fun bindListener() {
        confirm_tv.setOnClickListener {
            if (verifyCode.isBlank()) {
                ToastUtils.showShort(R.string.verify_code_hint)
                return@setOnClickListener
            }
            presenter.chekVerifyCode(verifyCode)
        }
    }


    override fun verified() {
        presenter.bindRefundMethod(channelName, userName, accountNumber, channelCode, channelCategory)
    }

    override fun bindSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFUND_METHOD_ADD_SUCCESS))
        finish()
    }


}
package com.jcs.where.features.refund.add.verify

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.payment.WebPayActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2022/4/26 14:46.
 * 短信验证后， 直接绑定绑定退款方式
 */
class RefundBindVerifyActivity :BaseMvpActivity<RefundBindVerifyPresenter>(),RefundBindVerifyView {

    /** 渠道名 */
    private var channelName = ""

    /** 银行(渠道名是银行 添银行缩写，第三方传空) */
    private var bankShortName = ""

    /**  用户名 */
    private var userName = ""

    /** 账号 */
    private var accountNumber = ""


    companion object {

        fun navigation(context: Context,channelName:String,userName:String,accountNumber:String , bankShortName:String? = null ) {
            val bundle = Bundle().apply {


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

    override fun getLayoutId() =  R.layout.activity_verify_by_code_4_refund

    override fun initView() {
        initExtra()
    }

    private fun initExtra() {


    }

    override fun initData() {
        presenter = RefundBindVerifyPresenter(this)
    }

    override fun bindListener() {

    }

}
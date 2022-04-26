package com.jcs.where.features.refund.add.edit.bank

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.refund.add.edit.bank.list.BankListActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_channel_bank_edit.*

/**
 * Created by Wangsw  2022/4/26 9:37.
 * 绑定退款银行卡
 */
class BankChannelEditActivity : BaseActivity() {

    /** 渠道名 */
    private var channelName = ""

    /** 银行(渠道名是银行 添银行缩写，第三方传空) */
    private var bankShortName = ""

    /**  用户名 */
    private var userName = ""

    /** 账号 */
    private var accountNumber = ""


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bundle = it.data?.extras ?: return@registerForActivityResult
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                bankShortName = bundle.getString(Constant.PARAM_BANK_SHORT_NAME, "")
                val bankName = bundle.getString(Constant.PARAM_BANK_NAME, "")
                select_bank_tv.text = bankName
            }

        }

    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_channel_bank_edit

    override fun initView() {
        initExtra()
        user_name_et.addTextChangedListener(
            afterTextChanged = {
                userName = it.toString().trim()
                handleAlpha()
            }
        )
        bank_account_et.addTextChangedListener(
            afterTextChanged = {
                accountNumber = it.toString().trim()
                handleAlpha()
            }
        )

    }

    private fun initExtra() {
        intent.extras?.let {
            channelName = it.getString(Constant.PARAM_REFUND_CHANNEL_NAME, "")
        }

    }

    private fun handleAlpha() {
        if (userName.isNotBlank() && accountNumber.isNotBlank() && bankShortName.isNotBlank()) {
            next_tv.alpha = 1.0f
        } else {
            next_tv.alpha = 0.5f
        }

    }

    override fun initData() = Unit

    override fun bindListener() {
        select_bank_tv.setOnClickListener {
            launcher.launch(Intent(this, BankListActivity::class.java).putExtra(Constant.PARAM_BANK_SHORT_NAME, bankShortName))
        }

        next_tv.setOnClickListener {
            if (bankShortName.isBlank()) {
                ToastUtils.showShort(R.string.please_selected_bank_hint)
                return@setOnClickListener
            }
            if (userName.isBlank()) {
                ToastUtils.showShort(R.string.edit_account_name_hint)
                return@setOnClickListener
            }
            if (accountNumber.isBlank()) {
                ToastUtils.showShort(R.string.bank_account_number_hint)
                return@setOnClickListener
            }
            // 跳转至短信验证


        }
    }
}
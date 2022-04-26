package com.jcs.where.features.refund.add.form.bank

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.features.refund.add.form.bank.child.BankListActivity
import com.jcs.where.features.refund.add.verify.RefundBindVerifyActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_channel_bank_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Wangsw  2022/4/26 9:37.
 * 绑定银行卡退款渠道
 */
class BankChannelFormActivity : BaseActivity() {

    /** 渠道名 */
    private var channelName = ""

    /** 银行缩写(第三方传空) */
    private var bankShortName = ""

    /** 银行全名 */
    private var bankAllName = ""

    /**  用户名 */
    private var userName = ""

    /** 账号 */
    private var accountNumber = ""


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bundle = it.data?.extras ?: return@registerForActivityResult
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                bankShortName = bundle.getString(Constant.PARAM_BANK_SHORT_NAME, "")
                bankAllName = bundle.getString(Constant.PARAM_BANK_ALL_NAME, "")
                select_bank_tv.text = bankAllName
            }

        }

    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_channel_bank_form

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
        val eventBus = EventBus.getDefault()
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }

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
            RefundBindVerifyActivity.navigation(this, channelName, userName, accountNumber, bankShortName, bankAllName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventReceived(baseEvent: BaseEvent<*>) {
        when (baseEvent.code) {
            EventCode.EVENT_REFUND_METHOD_ADD_SUCCESS -> {
                finish()
            }
        }
    }

}
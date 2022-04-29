package com.jcs.where.features.refund.add.form.third

import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.features.refund.add.verify.RefundBindVerifyActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_channel_third_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Wangsw  2022/4/26 9:37.
 * 绑定第三方退款渠道
 */
class ThirdChannelFormActivity : BaseActivity() {

    /** 渠道名 */
    private var channelName = ""

    /**  用户名 */
    private var userName = ""

    /** 账号 */
    private var accountNumber = ""

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_channel_third_form

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
        if (userName.isNotBlank() && accountNumber.isNotBlank()) {
            next_tv.alpha = 1.0f
        } else {
            next_tv.alpha = 0.5f
        }

    }

    override fun initData() = Unit

    override fun bindListener() {


        next_tv.setOnClickListener {

            if (userName.isBlank()) {
                ToastUtils.showShort(R.string.edit_account_name_hint)
                return@setOnClickListener
            }
            if (accountNumber.isBlank()) {
                ToastUtils.showShort(R.string.enter_account_number_hint)
                return@setOnClickListener
            }
            // 跳转至短信验证
            RefundBindVerifyActivity.navigation(this,channelName,userName , accountNumber)
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
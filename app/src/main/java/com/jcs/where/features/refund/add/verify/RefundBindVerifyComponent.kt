package com.jcs.where.features.refund.add.verify

import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.SendCodeRequest
import com.jcs.where.api.response.mall.RefundBindRequest
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Wangsw  2022/4/26 14:48.
 *
 */
interface RefundBindVerifyView : BaseMvpView {

    /** 验证通过 */
    fun verified()

    /** 绑定成功 */
    fun bindSuccess()

}

class RefundBindVerifyPresenter(private var view: RefundBindVerifyView) : BaseMvpPresenter(view) {


    fun getVerifyCode(getVerifyTv: TextView, target_tv: TextView) {
        val user = User.getInstance()
        val apply = SendCodeRequest().apply {
            phone = user.phone
            countryCode = user.countryCode
            type = Constant.VERIFY_CODE_TYPE_5
        }

        requestApi(mRetrofit.getVerifyCode(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                getVerifyTv.isClickable = false
                countdown(getVerifyTv, StringUtils.getString(R.string.get_again))
                ToastUtils.showShort(R.string.verify_code_send_success)
                target_tv.text = StringUtils.getString(R.string.verify_code_send_to_format, user.phone)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
                getVerifyTv.isClickable = true
            }

        })

    }

    /**
     * 倒计时
     *
     * @param countdownView 倒计时显示的view
     * @param defaultStr    默认显示的文字
     */
    private fun countdown(countdownView: TextView, defaultStr: String) {
        Flowable.intervalRange(0, Constant.WAIT_DELAYS, 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { aLong: Long ->
                val string =
                    StringUtils.getString(R.string.resend_format, Constant.WAIT_DELAYS - aLong)
                countdownView.text = string
            }
            .doOnComplete {
                countdownView.text = defaultStr
                countdownView.isClickable = true
            }.subscribe()
    }

    fun chekVerifyCode(verifyCode: String) {
        val user = User.getInstance()

        requestApi(mRetrofit.checkVerifyCode(1, verifyCode, user.phone, null), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.verified()
            }
        })
    }

    fun bindRefundMethod(channelName: String, userName: String, accountNumber: String, bankShortName: String? = null, bankAllName: String? = null) {

        val apply = RefundBindRequest().apply {
            channel_name = channelName
            user_name = userName
            account = accountNumber
            bank_name = bankShortName
            bank_all_name = bankAllName
        }
        requestApi(mRetrofit.bindRefundInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.bindSuccess()
            }
        })

    }


}
package com.jiechengsheng.city.features.refund.add.verify

import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.SendCodeRequest
import com.jiechengsheng.city.api.response.mall.RefundBindRequest
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
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

    fun bindRefundMethod(channelName: String, userName: String, accountNumber: String, channelCode: String , channelCategory: String) {

        val apply = RefundBindRequest().apply {
            name = channelName
            user_name = userName
            account = accountNumber
            channel_code = channelCode
            channel_category = channelCategory
        }
        requestApi(mRetrofit.bindRefundInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.bindSuccess()
            }
        })

    }


}
package com.jcs.where.features.account.bind

import android.text.TextUtils
import com.jcs.where.api.network.BaseMvpPresenter
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.api.request.SendCodeRequest
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import com.jcs.where.api.request.account.BindPhoneRequest
import com.jcs.where.api.response.LoginResponse
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.SPKey
import java.util.concurrent.TimeUnit

/**
 * Created by Wangsw  2021/2/1 16:14.
 */
class BindPhonePresenter(private val mView: BindPhoneView) : BaseMvpPresenter(mView) {
    /**
     * 获取验证码
     *
     * @param account
     * @param getVerifyTv
     */
    fun getVerifyCode(prefix: String?, account: String?, getVerifyTv: TextView) {
        if (FeaturesUtil.isWrongPhoneNumber(prefix, account)) {
            return
        }
        val request = SendCodeRequest().apply {
            phone = account
            countryCode = prefix
            type = Constant.VERIFY_CODE_TYPE_1_LOGIN
        }

        requestApi(mRetrofit.getVerifyCode(request), object : BaseMvpObserver<JsonElement>(mView) {
            override fun onSuccess(response: JsonElement) {
                getVerifyTv.isClickable = false
                countdown(getVerifyTv, StringUtils.getString(R.string.get_again))
                ToastUtils.showShort(R.string.verify_code_send_success)
            }

            override fun onError(errorResponse: ErrorResponse) {
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
                val string = StringUtils.getString(R.string.count_down_format, Constant.WAIT_DELAYS - aLong)
                countdownView.text = string
            }
            .doOnComplete {
                countdownView.text = defaultStr
                countdownView.isClickable = true
            }.subscribe()
    }

    /**
     * 绑定手机号
     *
     * @param countryCode 国家码
     * @param account     账号
     * @param verifyCode  验证码
     * @param password    密码
     * @param userName    第三方返回用户昵称
     * @param userId      第三方返回用户id
     * @param userIcon    第三方返回用户头像
     * @param loginType
     */
    fun bindPhone(
        countryCode: String?,
        account: String?,
        verifyCode: String?,
        password: String?,
        userName: String?,
        userId: String?,
        userIcon: String?,
        loginType: Int
    ) {
        if (FeaturesUtil.isWrongPhoneNumber(countryCode, account)) {
            return
        }
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showShort(StringUtils.getString(R.string.verify_code_hint))
            return
        }
        if (FeaturesUtil.isWrongPasswordFormat(password)) {
            return
        }

        val inviteCode = SPUtils.getInstance().getString(SPKey.K_INVITE_CODE, null)

        val request = BindPhoneRequest().apply {
            type = loginType
            nickname = userName
            open_id = userId
            avatar = userIcon
            phone = account
            this.password = password
            verification_code = verifyCode
            country_code = countryCode
            invite_code = inviteCode
        }
        requestApi(mRetrofit.bindPhone(request), object : BaseMvpObserver<LoginResponse>(mView) {

            override fun onSuccess(response: LoginResponse) {
                val token = response.token
                CacheUtil.saveToken(token)

                // 清空邀请码
                SPUtils.getInstance().put(SPKey.K_INVITE_CODE , "")

                mView.bindSuccess()
            }
        })
    }
}
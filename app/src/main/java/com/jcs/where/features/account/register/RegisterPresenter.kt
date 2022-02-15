package com.jcs.where.features.account.register

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.account.RegisterRequest
import com.jcs.where.api.response.LoginResponse
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.SPKey

/**
 * Created by Wangsw  2021/1/29 16:51.
 */
class RegisterPresenter(private val mView: RegisterView) : BaseMvpPresenter(mView) {
    /**
     * 注册
     * @param account     账号
     * @param verifyCode  验证码
     * @param countryCode 国家码
     */
    fun register(account: String?, verifyCode: String?, countryCode: String?, password: String, passwordConfirm: String) {



        if (FeaturesUtil.isWrongPhoneNumber(countryCode, account)) {
            return
        }
        if (FeaturesUtil.isWrongPasswordFormat(password)) {
            return
        }
        if (TextUtils.isEmpty(passwordConfirm) || password != passwordConfirm) {
            ToastUtils.showShort(R.string.password_not_same_hint)
            return
        }

        val inviteCode = SPUtils.getInstance().getString(SPKey.K_INVITE_CODE, null)


        val build = RegisterRequest().apply {
            phone = account
            verification_code = verifyCode
            this.password = password
            country_code = countryCode
            invite_code = inviteCode
        }

        requestApi(mRetrofit.register(build), object : BaseMvpObserver<LoginResponse>(mView) {

            override fun onSuccess(response: LoginResponse) {
                val token = response.token
                CacheUtil.saveToken(token)

                // 清空邀请码
                SPUtils.getInstance().put(SPKey.K_INVITE_CODE , "")

                mView.registerSuccess()
            }
        })
    }
}
package com.jcs.where.features.account.register

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.jcs.where.BaseApplication
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.account.RegisterRequest
import com.jcs.where.api.response.LoginResponse
import com.jcs.where.api.response.UserInfoResponse
import com.jcs.where.storage.entity.User
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

                handleRegisterSuccess(response.token)

                mView.registerSuccess()
            }


        })
    }

    private fun handleRegisterSuccess(token: String) {
        CacheUtil.saveToken(token)
        // 清空邀请码
        SPUtils.getInstance().put(SPKey.K_INVITE_CODE, "")
        getUserInfo()
    }


    /**
     * 获取用户信息
     */
    private fun getUserInfo() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.userInfo, object : BaseMvpObserver<UserInfoResponse>(mView) {
            override fun onSuccess(response: UserInfoResponse) {

                // 邀请链接
                SPUtils.getInstance().put(SPKey.K_INVITE_LINK, response.invite_link)

                // 保存用户信息
                val user = User.Builder.anUser()
                    .id(response.id)
                    .nickName(response.nickname)
                    .phone(response.phone)
                    .email(response.email)
                    .avatar(response.avatar)
                    .balance(response.balance)
                    .createdAt(response.createdAt)
                    .name(response.name)
                    .type(response.type)
                    .countryCode(response.countryCode)
                    .merchantApplyStatus(response.merchantApplyStatus)
                    .faceBookBindStatus(response.facebookBindStatus)
                    .googleBindStatus(response.googleBindStatus)
                    .twitterBindStatus(response.twitterBindStatus)
                    .signStatus(response.signStatus)
                    .integral(response.integral)
                    .rongData(response.rongData).build()


                val whereApp = Utils.getApp() as BaseApplication
                whereApp.database.userDao().addUser(user)
                User.update()

                // 刷新融云用户信息
                whereApp.refreshRongUserInfoCache(User.getInstance().rongData.uuid)

                // 连接融云
                whereApp.connectRongCloud()
            }
        })
    }


}
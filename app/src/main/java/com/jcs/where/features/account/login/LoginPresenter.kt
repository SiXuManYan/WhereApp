package com.jcs.where.features.account.login

import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import cn.sharesdk.facebook.Facebook
import cn.sharesdk.framework.PlatformDb
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.google.GooglePlus
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.google.gson.JsonElement
import com.jcs.where.BaseApplication
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.SendCodeRequest
import com.jcs.where.api.request.account.LoginRequest
import com.jcs.where.api.request.account.ThreePartyLoginRequest
import com.jcs.where.api.response.LoginResponse
import com.jcs.where.api.response.UserInfoResponse
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Wangsw  2021/1/28 16:43.
 */
class LoginPresenter(private val mView: LoginView) : BaseMvpPresenter(mView) {
    /**
     * 处理登录
     *
     * @param isVerifyMode 是否是验证码模式
     * @param prefix       账号号前缀
     * @param account      账号
     * @param verifyCode   验证码
     * @param password     密码
     */
    fun handleLogin(isVerifyMode: Boolean, prefix: String?, account: String?, verifyCode: String?, password: String?) {
        if (FeaturesUtil.isWrongPhoneNumber(prefix, account)) {
            return
        }
        if (isVerifyMode) {
            // 验证码
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort(StringUtils.getString(R.string.verify_code_hint))
                return
            }
            login(Constant.LOGIN_TYPE_VERIFY_CODE, account, verifyCode, password)
        } else {
            // 密码
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort(StringUtils.getString(R.string.input_password_hint))
                return
            }
            login(Constant.LOGIN_TYPE_PASSWORD, account, verifyCode, password)
        }
    }

    /**
     * 验证码登录
     * 当登录接口返回404时，注册接口
     *
     * @param loginType  登录类型 （1：手机验证码，2：手机密码）
     * @param account    账号
     * @param verifyCode 验证码
     * @param password   密码
     */
    fun login(loginType: Int, account: String?, verifyCode: String?, password: String?) {
        val loginRequest = LoginRequest()
        loginRequest.phone = account
        loginRequest.type = loginType
        when (loginType) {
            Constant.LOGIN_TYPE_VERIFY_CODE -> loginRequest.verificationCode = verifyCode
            Constant.LOGIN_TYPE_PASSWORD -> loginRequest.password = password
            else -> {}
        }
        requestApi(mRetrofit.login(loginRequest), object : BaseMvpObserver<LoginResponse>(mView) {
            override fun onSuccess(response: LoginResponse) {
                handleLoginSuccess(response.token)
            }

            override fun onError(errorResponse: ErrorResponse) {
                // 验证码登录，返回404时是新用户，需要走注册接口
                if (loginType == Constant.LOGIN_TYPE_VERIFY_CODE && errorResponse.errCode == 404) {
                    mView.guideRegister(account, verifyCode)
                } else {
                    super.onError(errorResponse)
                }
            }
        })
    }

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
        val request = SendCodeRequest()
        request.setPhone(account)
        request.setCountryCode(prefix)
        request.setType(Constant.VERIFY_CODE_TYPE_1_LOGIN)
        requestApi(mRetrofit.getVerifyCode(request), object : BaseMvpObserver<JsonElement>(mView) {
            protected override fun onSuccess(response: JsonElement) {
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
     * 保存token
     *
     * @param token
     */
    private fun handleLoginSuccess(token: String) {
        CacheUtil.saveToken(token)

        getUserInfo()
    }

    /**
     * 第三方授权
     *
     * @param platformName 平台名称
     * @see Facebook
     *
     * @see cn.sharesdk.google.GooglePlus
     */
    fun threePartyAuthorize(platformName: String) {
        MobUtil.authorize(ShareSDK.getPlatform(platformName)) { db: PlatformDb ->
            // 授权成功
            val userName = db.userName
            val userId = db.userId
            val userIcon = db.userIcon
            Log.d("第三方登录", "userName == $userName")
            Log.d("第三方登录", "userId == $userId")
            Log.d("第三方登录", "userIcon == $userIcon")
            threePartyLogin(platformName, db)
        }
    }

    /**
     * 第三方登录
     */
    private fun threePartyLogin(platformName: String, db: PlatformDb) {
        var loginType = 0
        if (platformName == Facebook.NAME) {
            loginType = ThreePartyLoginRequest.TYPE_FACEBOOK
        }
        if (platformName == GooglePlus.NAME) {
            loginType = ThreePartyLoginRequest.TYPE_GOOGLE
        }
        val request = ThreePartyLoginRequest.Builder.aThreePartyLoginRequest()
            .type(loginType)
            .nickname(db.userName)
            .open_id(db.userId)
            .avatar(db.userIcon).build()
        val finalLoginType = loginType

        requestApi(mRetrofit.threePartyLogin(request), object : BaseMvpObserver<LoginResponse>(mView) {
            override fun onSuccess(response: LoginResponse) {
                handleLoginSuccess(response.token)

            }

            override fun onError(errorResponse: ErrorResponse) {
                val errCode = errorResponse.getErrCode()

                // 404：账号不存在，需要跳转绑定手机号界面
                if (errCode == 404) {
                    mView.guideToAccountBind(db, finalLoginType)
                }
            }
        })
    }


    /**
     * 获取用户信息
     */
    private fun getUserInfo() {

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

                // 登录成功
                mView.LoginSuccess()
            }
        })
    }


}
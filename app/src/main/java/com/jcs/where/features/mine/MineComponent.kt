package com.jcs.where.features.mine

import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.jcs.where.BaseApplication
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.MerchantSettledInfoResponse
import com.jcs.where.api.response.UnReadMessage
import com.jcs.where.api.response.UserInfoResponse
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.SPKey
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.RongIMClient.*

/**
 * Created by Wangsw  2021/8/13 14:14.
 *
 */
interface MineView : BaseMvpView {
    fun bindUnreadMessageCount(totalCount: Int)
    fun bindUserInfo(response: UserInfoResponse)
    fun handleMerchant(response: MerchantSettledInfoResponse)
}

class MinePresenter(var view: MineView) : BaseMvpPresenter(view) {

    /**
     * 获取未读消息总数
     */
    fun getUnreadMessageCount() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.unreadMessageCount, object : BaseMvpObserver<UnReadMessage>(view) {
            override fun onSuccess(response: UnReadMessage) {
                val apiUnreadMessageCount = response.count

                RongIMClient.getInstance()
                    .getTotalUnreadCount(object : RongIMClient.ResultCallback<Int?>() {
                        override fun onSuccess(rongMessageCount: Int?) {

                            var rongCount = 0
                            rongMessageCount?.let {
                                rongCount = it
                            }

                            val totalCount = apiUnreadMessageCount + rongCount
                            view.bindUnreadMessageCount(totalCount)

                        }

                        override fun onError(errorCode: RongIMClient.ErrorCode) = Unit
                    })

            }

        })


    }

    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.userInfo, object : BaseMvpObserver<UserInfoResponse>(view) {
            override fun onSuccess(response: UserInfoResponse) {
                view.bindUserInfo(response)
                saveData(response)
            }
        })
    }

    var alreadyConnectRongCloud = false


    /**
     * 保存用户数据
     *
     */
    private fun saveData(response: UserInfoResponse) {
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


        val app = Utils.getApp() as BaseApplication
        val database = app.database
        database.userDao().addUser(user)
        User.update()

        SPUtils.getInstance().put(SPKey.K_INVITE_LINK, response.invite_link)

        connectRongCloud(response.rongData.token)
    }

    /**
     * 连接融云
     */
    fun connectRongCloud(token: String) {
        Log.e("融云", "alreadyConnectRongCloud == " + alreadyConnectRongCloud)
        if (!User.isLogon()) {
            return
        }
        if (alreadyConnectRongCloud) {
            return
        }
        RongIM.connect(token, object : ConnectCallback() {
            override fun onDatabaseOpened(code: DatabaseOpenStatus) {
                //消息数据库打开，可以进入到主页面
                Log.e("融云", "onDatabaseOpened")
            }

            override fun onSuccess(s: String) {
                //连接成功
                alreadyConnectRongCloud = true
                Log.e("融云", "链接成功")
            }

            override fun onError(errorCode: ConnectionErrorCode) {
                Log.e("融云", "链接失败 errorCode == $errorCode")
                if (errorCode == ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT) {
                    //从 APP 服务获取新 token，并重连
                    alreadyConnectRongCloud = false
                } else {
                    //无法连接 IM 服务器，请根据相应的错误码作出对应处理
                }
            }
        })
    }

    /**
     * 获取商家入驻信息
     */
    fun getMerchantSettledInfo() {

        requestApi(mRetrofit.merchantSettledInfo, object : BaseMvpObserver<MerchantSettledInfoResponse>(view) {
            override fun onSuccess(response: MerchantSettledInfoResponse) {
                view.handleMerchant(response)

            }


        })
    }


}

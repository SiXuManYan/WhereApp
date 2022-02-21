package com.jcs.where.features.mine

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
import io.rong.imlib.RongIMClient

/**
 * Created by Wangsw  2021/8/13 14:14.
 *
 */
interface MineView : BaseMvpView {
    fun bindUnreadMessageCount(totalCount: Int)
    fun bindUserInfo(nickname: String, createdAt: String, avatar: String)
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
                view.bindUserInfo(response.nickname, response.createdAt, response.avatar)
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

        // 邀请链接
        SPUtils.getInstance().put(SPKey.K_INVITE_LINK, response.invite_link)

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

    }


}

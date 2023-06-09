package com.jiechengsheng.city.features.mine

import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.jiechengsheng.city.BaseApplication
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.UnReadMessage
import com.jiechengsheng.city.api.response.UserInfoResponse
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.SPKey
import io.rong.imlib.RongIMClient

/**
 * Created by Wangsw  2021/8/13 14:14.
 *
 */
interface MineView : BaseMvpView {
    fun bindUnreadMessageCount(totalCount: Int, systemCount: Int)
    fun bindUserInfo( response: UserInfoResponse)
}

class MinePresenter(var view: MineView) : BaseMvpPresenter(view) {

    /**
     * 获取未读消息总数
     */
    fun getUnreadMessageCount() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.unreadMessageCount, object : BaseMvpObserver<UnReadMessage>(view,false) {
            override fun onSuccess(response: UnReadMessage) {
                val apiUnreadMessageCount = response.count

                try {
                    // 捕获融云SKD 5.2.3.3 获取消息数量时， 在Android 12 上出现的 UnsatisfiedLinkError 异常
                    RongIMClient.getInstance().getTotalUnreadCount(object : RongIMClient.ResultCallback<Int?>() {
                        override fun onSuccess(rongMessageCount: Int?) {

                            var rongCount = 0
                            rongMessageCount?.let {
                                rongCount = it
                            }

                            val totalCount = apiUnreadMessageCount + rongCount
                            view.bindUnreadMessageCount(totalCount,apiUnreadMessageCount)

                        }

                        override fun onError(errorCode: RongIMClient.ErrorCode) {
                            view.bindUnreadMessageCount(apiUnreadMessageCount,apiUnreadMessageCount)
                        }
                    })
                } catch (e: Exception) {
                    view.bindUnreadMessageCount(apiUnreadMessageCount,apiUnreadMessageCount)
                }


            }

            override fun onError(e: Throwable) {
                view.bindUnreadMessageCount(0,0)
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

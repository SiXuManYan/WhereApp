package com.jiechengsheng.city.features.daily.sign

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.SignListResponse
import com.jiechengsheng.city.api.response.UserInfoResponse

/**
 * Created by Wangsw  2021/11/24 16:33.
 *
 */
interface SignInView : BaseMvpView {
    fun bindSignInList(response: SignListResponse)
    fun bindUserIntegral( isSigned: Boolean)
    fun signInSuccess()
}

class SignInPresenter(private var view: SignInView) : BaseMvpPresenter(view) {

    /**
     * 获取用户信息(获取积分)
     *
     * @param
     */
    open fun getUserInfo() {
        requestApi(mRetrofit.userInfo, object : BaseMvpObserver<UserInfoResponse>(view) {
            override fun onSuccess(response: UserInfoResponse) {
                val signStatus = response.signStatus
                view.bindUserIntegral( signStatus == 1)
            }

        })
    }


    /**
     * 签到列表
     */
    fun getSignInList() {
        requestApi(mRetrofit.signList, object : BaseMvpObserver<SignListResponse>(view) {
            override fun onSuccess(response: SignListResponse) {
                view.bindSignInList(response)
            }

        })
    }

    /**
     * 立即签到
     */
    fun signIn() {
        requestApi(mRetrofit.signIn(), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.signInSuccess()
            }
        })
    }


}
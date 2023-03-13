package com.jiechengsheng.city.features.integral.activitys

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.UserInfoResponse
import com.jiechengsheng.city.api.response.integral.IntegralTag
import com.jiechengsheng.city.storage.entity.User

/**
 * Created by Wangsw  2022/9/21 10:08.
 *
 */
interface ActivityCenterView : BaseMvpView {
    fun bindTab(response: ArrayList<IntegralTag>)
    fun bindIntegral(integral: String)

}

class ActivityCenterPresenter(private var view: ActivityCenterView) : BaseMvpPresenter(view) {


    fun getTab() {
        requestApi(mRetrofit.activityCenterTab(), object : BaseMvpObserver<ArrayList<IntegralTag>>(view) {
            override fun onSuccess(response: ArrayList<IntegralTag>) {
                response.forEachIndexed { index, integralTag ->
                    if (index == 0) {
                        integralTag.nativeIsSelected = true
                    }
                }
                view.bindTab(response)
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
                view.bindIntegral(response.integral)

            }
        })
    }


}
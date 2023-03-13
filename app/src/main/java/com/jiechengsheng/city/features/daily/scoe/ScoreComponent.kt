package com.jiechengsheng.city.features.daily.scoe

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.IntegralDetailResponse
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.UserInfoResponse

/**
 * Created by Wangsw  2021/11/25 18:25.
 *
 */
interface ScoreView : BaseMvpView {
    fun bindUserIntegral(integral: String)
    fun bindDetailData(data: List<IntegralDetailResponse>, lastPage: Boolean)

}


class ScorePresenter(private var view: ScoreView) : BaseMvpPresenter(view) {


    /**
     * 获取用户信息(获取积分)
     *
     * @param
     */
     fun getUserInfo() {
        requestApi(mRetrofit.userInfo, object : BaseMvpObserver<UserInfoResponse>(view) {
            override fun onSuccess(response: UserInfoResponse) {
                view.bindUserIntegral(response.integral.toString())
            }
        })
    }


    /**
     * 积分明细列表
     */
    fun getIntegralDetailList(page: Int) {
        requestApi(mRetrofit.getIntegralsDetailList(page), object : BaseMvpObserver<PageResponse<IntegralDetailResponse>>(view,page) {
            override fun onSuccess(response: PageResponse<IntegralDetailResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindDetailData(data, isLastPage)
            }
        })
    }
}
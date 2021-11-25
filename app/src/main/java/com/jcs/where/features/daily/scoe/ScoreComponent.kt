package com.jcs.where.features.daily.scoe

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.IntegralDetailResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.UserInfoResponse

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
        requestApi(mRetrofit.getIntegralsDetailList(page), object : BaseMvpObserver<PageResponse<IntegralDetailResponse>>(view) {
            override fun onSuccess(response: PageResponse<IntegralDetailResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindDetailData(data, isLastPage)
            }
        })
    }
}
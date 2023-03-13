package com.jiechengsheng.city.features.coupon.center

import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.Coupon
import com.jiechengsheng.city.api.response.GetCouponResult
import com.jiechengsheng.city.api.response.PageResponse

/**
 * Created by Wangsw  2022/3/5 14:42.
 *
 */

interface CouponCenterView : BaseMvpView, OnItemChildClickListener {
    fun bindData(data: MutableList<Coupon>, lastPage: Boolean)
    fun getCouponResult(message: String)

}

class CouponCenterPresenter(private var view: CouponCenterView) : BaseMvpPresenter(view) {

    fun getData(page: Int) {

        requestApi(mRetrofit.couponCenter(page), object : BaseMvpObserver<PageResponse<Coupon>>(view,page) {
            override fun onSuccess(response: PageResponse<Coupon>) {

                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })

    }


    fun getCoupon(couponId: Int, couponType: Int) {

        requestApi(mRetrofit.getCoupon(couponId,couponType), object : BaseMvpObserver<GetCouponResult>(view) {
            override fun onSuccess(response: GetCouponResult) {
                view.getCouponResult(response.message)
            }

        })
    }

}
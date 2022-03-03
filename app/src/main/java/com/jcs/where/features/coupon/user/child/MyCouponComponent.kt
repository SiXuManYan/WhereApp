package com.jcs.where.features.coupon.user.child

import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.UserCoupon
import com.jcs.where.api.response.collection.MyCollection

/**
 * Created by Wangsw  2022/3/2 15:11.
 *
 */

interface MyCouponView : BaseMvpView, OnItemChildClickListener {
    fun bindData(data: MutableList<UserCoupon>, lastPage: Boolean)
}


class MyCouponPresenter(private var view: MyCouponView) : BaseMvpPresenter(view) {
    fun getData(page: Int, type: Int) {

        requestApi(mRetrofit.userCoupon(page, type), object : BaseMvpObserver<PageResponse<UserCoupon>>(view) {

            override fun onSuccess(response: PageResponse<UserCoupon>) {

                val isLastPage = response.lastPage == page
                val data = response.data
                data.forEach {

                    it.nativeType = type
                }

                view.bindData(data.toMutableList(), isLastPage)
            }
        })

    }

}
package com.jcs.where.features.coupon.user.child

import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.UserCoupon

/**
 * Created by Wangsw  2022/3/2 15:11.
 *
 */

interface MyCouponView : BaseMvpView, OnItemChildClickListener {
    fun bindData(data: MutableList<UserCoupon>, lastPage: Boolean)
}


class MyCouponPresenter(private var view: MyCouponView) : BaseMvpPresenter(view) {
    fun getData(page: Int, type: Int) {

        requestApi(mRetrofit.couponUser(page, type), object : BaseMvpObserver<PageResponse<UserCoupon>>(view) {

            override fun onSuccess(response: PageResponse<UserCoupon>) {

                val isLastPage = response.lastPage == page
                val data = response.data
                data.forEach {
                    it.nativeType = type
                }
                addTitle(data)
                view.bindData(data.toMutableList(), isLastPage)
            }
        })

    }

    /** 商家券按照商家名称分组 */
    private fun addTitle(data: MutableList<UserCoupon>) {
        val groupBy = data.groupBy { it.shop_name }

        groupBy.forEach { group ->

            if (group.key.isNotBlank()) {
                val titleEntity = UserCoupon().apply {
                    shop_name = group.key
                    this.nativeListType = UserCoupon.TYPE_TITLE
                }

                val indexOfFirst = data.indexOfFirst {
                    it.shop_name == group.key
                }

                data.add(indexOfFirst, titleEntity)
            }


        }


    }

}
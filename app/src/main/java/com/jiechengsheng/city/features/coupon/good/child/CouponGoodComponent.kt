package com.jiechengsheng.city.features.coupon.good.child

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.mall.MallGood
import com.jiechengsheng.city.api.response.mall.request.MallGoodListRequest

/**
 * Created by Wangsw  2022/3/4 14:43.
 *
 */

interface CouponGoodView : BaseMvpView {
    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)
}


class CouponGoodPresenter(private var view: CouponGoodView) : BaseMvpPresenter(view) {


    fun getMallList(request: MallGoodListRequest) {
        requestApi(mRetrofit.getMallGoodList(
            request.page,
            request.order?.name,
            request.title,
            request.categoryId,
            request.startPrice,
            request.endPrice,
            request.sold?.name,
            request.shopId,
            request.shop_categoryId,
            request.recommend,
            request.coupon_id
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view, request.page) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == request.page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })

    }

}
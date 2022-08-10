package com.jcs.where.features.mall.shop.home.good

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.request.MallGoodListRequest

/**
 * Created by Wangsw  2022/1/24 10:22.
 *
 */
interface MallShopGoodView : BaseMvpView {

    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)

}

class MallShopGoodPresenter(private var view: MallShopGoodView) : BaseMvpPresenter(view) {


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
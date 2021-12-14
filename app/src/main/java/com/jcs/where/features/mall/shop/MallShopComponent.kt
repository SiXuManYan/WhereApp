package com.jcs.where.features.mall.shop

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood

/**
 * Created by Wangsw  2021/12/14 10:37.
 *
 */
interface MallShopView : BaseMvpView {
    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)
}

class MallShopPresenter(private var view: MallShopView):BaseMvpPresenter(view){

    fun getMallList(page: Int, shopId:Int) {
        requestApi(mRetrofit.getMallGoodList(
            page,
            null,
            null,
            null,
            null,
            null,
            null,
            shopId
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })
    }

}
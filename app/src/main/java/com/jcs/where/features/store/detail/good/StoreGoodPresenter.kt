package com.jcs.where.features.store.detail.good

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.store.StoreGoods

/**
 * Created by Wangsw  2021/6/16 15:27.
 *
 */
class StoreGoodPresenter(private var view: StoreGoodView) : BaseMvpPresenter(view) {

    fun getGood(shop_id: String, page: Int) {
        requestApi(mRetrofit.getStoreGoodList(page, shop_id), object : BaseMvpObserver<PageResponse<StoreGoods>>(view) {
            override fun onSuccess(response: PageResponse<StoreGoods>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage)
            }
        })


    }


}
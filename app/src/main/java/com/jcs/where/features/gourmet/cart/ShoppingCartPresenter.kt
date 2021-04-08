package com.jcs.where.features.gourmet.cart

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse

/**
 * Created by Wangsw  2021/4/7 14:47.
 */
class ShoppingCartPresenter(val view: ShoppingCartView) : BaseMvpPresenter(view) {


    fun getData(page: Int) {
        requestApi(mRetrofit.getShoppingCartList(page), object : BaseMvpObserver<PageResponse<ShoppingCartResponse>>(view) {
            override fun onSuccess(response: PageResponse<ShoppingCartResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()

                view.bindList(toMutableList, isLastPage)
            }
        })
    }


}
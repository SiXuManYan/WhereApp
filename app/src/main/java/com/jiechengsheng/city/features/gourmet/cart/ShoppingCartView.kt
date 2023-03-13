package com.jiechengsheng.city.features.gourmet.cart

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.gourmet.cart.ShoppingCartResponse

/**
 * Created by Wangsw  2021/4/7 14:45.
 *
 */
interface ShoppingCartView : BaseMvpView {
    fun bindList(data: MutableList<ShoppingCartResponse>, lastPage: Boolean)
    fun deleteSuccess()


}
package com.jcs.where.features.gourmet.cart

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse

/**
 * Created by Wangsw  2021/4/7 14:45.
 *
 */
interface ShoppingCartView : BaseMvpView {
    fun bindList(data: MutableList<ShoppingCartResponse>, lastPage: Boolean)
    fun deleteSuccess()


}
package com.jiechengsheng.city.features.gourmet.restaurant.packages

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.AddCartRequest
import com.jiechengsheng.city.api.response.gourmet.dish.DishDetailResponse
import com.jiechengsheng.city.api.response.other.CartNumberResponse
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.widget.NumberView2


/**
 * Created by Wangsw  2021/4/6 14:36.
 */
interface SetMealView : BaseMvpView, NumberView2.OnValueChangeListener {
    fun bindData(data: DishDetailResponse, inventoryValue: Int)
    fun bindCartCount(nums: Int)
}

/**
 * Created by Wangsw  2021/4/6 14:37.
 */
class SetMealPresenter(private val view: SetMealView) : BaseMvpPresenter(view) {
    fun getDetail(eatInFoodId: Int) {
        requestApi(mRetrofit.getDishDetail(eatInFoodId), object : BaseMvpObserver<DishDetailResponse>(view) {
            override fun onSuccess(response: DishDetailResponse) {
                view.bindData(response, BusinessUtils.getSafeStock(response.inventory))
            }
        })
    }

    fun addCart(request: AddCartRequest?) {
        requestApi(mRetrofit.addCartNumber(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                ToastUtils.showShort("add success")
                getCartCount()
            }
        })
    }

    fun getCartCount(){
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.foodCartCount, object : BaseMvpObserver<CartNumberResponse>(view) {
            override fun onSuccess(response: CartNumberResponse) {
                view.bindCartCount(response.nums)
            }
        })
    }
}
package com.jcs.where.features.gourmet.takeaway

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.CollectionRestaurantRequest
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.gourmet.comment.CommentResponse
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/25 10:05.
 *
 */
class TakeawayPresenter(val view: TakeawayView) : BaseMvpPresenter(view) {


    /**
     * 获取详情页数据
     */
    fun getDetailData(restaurantId: String) {

        requestApi(mRetrofit.takeawayDetail(restaurantId), object : BaseMvpObserver<TakeawayDetailResponse>(view) {
            override fun onSuccess(response: TakeawayDetailResponse?) {
                response?.let {
                    view.bindData(it)

                }
            }

        })

    }

    /**
     * 外卖菜品列表
     */
    fun getDishList(restaurantId: String) {

        requestApi(mRetrofit.takeawayGoodList(1, restaurantId), object : BaseMvpObserver<PageResponse<DishTakeawayResponse>>(view) {
            override fun onSuccess(response: PageResponse<DishTakeawayResponse>?) {
                if (response == null || response.data.isEmpty()) {
                    return
                }
                view.bindDishList(response.data.toMutableList())
            }
        })
    }

    /**
     * 计算价格
     */
    fun handlePrice(adapter: TakeawayAdapter): BigDecimal {
        var totalPrice: BigDecimal = BigDecimal.ZERO

        adapter.data.forEach {
            if (it.nativeSelectCount > 0) {
                val price = it.price
                val goodNum = it.nativeSelectCount
                val currentItemPrice = BigDecimalUtil.mul(price, BigDecimal(goodNum))
                totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
            }
        }
        return totalPrice
    }

    fun getTotalSelectCount(adapter: TakeawayAdapter): Int {
        var totalCount = 0

        adapter.data.forEach {
            if (it.nativeSelectCount > 0) {
                totalCount += it.nativeSelectCount
            }
        }
        return totalCount
    }

    fun getSelectedList(adapter: TakeawayAdapter): ArrayList<DishTakeawayResponse> {

        val list = ArrayList<DishTakeawayResponse>()

        adapter.data.forEach {
            if (it.nativeSelectCount > 0) {
                list.add(it)
            }
        }
        return list

    }

    fun collection(restaurantId: String) {
        val request = CollectionRestaurantRequest().apply {
            this.restaurant_id = restaurantId
        }
        requestApi(mRetrofit.collectsRestaurant(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.collectionSuccess()
            }
        })

    }

    fun unCollection(restaurantId: String) {
        val request = CollectionRestaurantRequest().apply {
            this.restaurant_id = restaurantId
        }
        requestApi(mRetrofit.unCollectsRestaurant(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.unCollectionSuccess()
            }

        })

    }

    /**
     * 评论列表
     */
    fun getCommentList(restaurantId: String) {
        requestApi(mRetrofit.getCommentList(1, 0, restaurantId), object : BaseMvpObserver<PageResponse<CommentResponse>>(view) {
            override fun onSuccess(response: PageResponse<CommentResponse>) {
                val data = response.data
                if (!data.isNullOrEmpty()) {
                    view.bindCommentData(data)
                }
            }
        })
    }


}
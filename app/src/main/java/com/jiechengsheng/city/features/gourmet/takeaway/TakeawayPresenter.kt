package com.jiechengsheng.city.features.gourmet.takeaway

import android.annotation.SuppressLint
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.gourmet.dish.DishResponse
import com.jiechengsheng.city.api.response.gourmet.takeaway.TakeawayDetailResponse
import com.jiechengsheng.city.utils.BigDecimalUtil
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

    fun getSelectedList(adapter: TakeawayAdapter): ArrayList<DishResponse> {

        val list = ArrayList<DishResponse>()

        adapter.data.forEach {
            if (it.nativeSelectCount > 0) {
                list.add(it)
            }
        }
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearCart(adapter: TakeawayAdapter){
        val list = ArrayList<DishResponse>()

        adapter.data.forEach {
           it.nativeSelectCount = 0
        }
        adapter.notifyDataSetChanged()
    }


}
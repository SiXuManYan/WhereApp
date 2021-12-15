package com.jcs.where.features.mall.cart

import android.annotation.SuppressLint
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.store.cart.StoreCartGroup
import com.jcs.where.features.store.cart.child.OnChildSelectClick
import com.jcs.where.features.store.cart.child.OnGroupSelectClick
import com.jcs.where.features.store.cart.child.StoreCartAdapter
import com.jcs.where.features.store.cart.child.StoreCartValueChangeListener
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/14 17:49.
 *
 */

interface MallCartView : BaseMvpView ,  SwipeRefreshLayout.OnRefreshListener,
    StoreCartValueChangeListener, OnChildSelectClick, OnGroupSelectClick {
    fun bindData(data: MutableList<MallCartGroup>, lastPage: Boolean)

}

class MallCartPresenter(private var view: MallCartView) : BaseMvpPresenter(view) {

    fun getData(page: Int) {
        requestApi(mRetrofit.mallCartList(page), object :BaseMvpObserver<PageResponse<MallCartGroup>>(view){
            override fun onSuccess(response: PageResponse<MallCartGroup>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }

    /**
     * 处理全选和非全选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun handleSelectAll(adapter: MallCartAdapter, select: Boolean) {

        adapter.data.forEach {
            it.nativeIsSelect = select
            it.gwc.forEach { child ->
                child.nativeIsSelect = select
            }
        }
        adapter.notifyDataSetChanged()

    }

    fun getSelectedCount(adapter: MallCartAdapter): Int {
        var count = 0
        adapter.data.forEach {

            it.gwc.forEach { child ->
                if (child.nativeIsSelect) {
                    count += 1
                }
            }
        }
        return count
    }



    fun getSelectedData(adapter: MallCartAdapter): ArrayList<MallCartGroup> {

        val selectData: ArrayList<MallCartGroup> = ArrayList()

        adapter.data.forEach {


            val group = MallCartGroup().apply {

                shop_id = it.shop_id
                title = it.title
                nativeIsSelect = true
            }

            it.gwc.forEach { child ->
                if (child.nativeIsSelect) {
                    group.gwc.add(child)
                }
            }

            if (group.gwc.isNotEmpty()) {
                selectData.add(group)
            }

        }
        return selectData
    }


    /**
     * 处理价格
     */
    fun handlePrice(adapter: MallCartAdapter): BigDecimal {

        var totalPrice: BigDecimal = BigDecimal.ZERO
        adapter.data.forEachIndexed { _, data ->
            data.gwc.forEach {
                if (it.nativeIsSelect) {

                    val price = it.specs_info!!.price


                    val goodNum = it.good_num
                    val currentItemPrice = BigDecimalUtil.mul(price, BigDecimal(goodNum))
                    totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
                }
            }
        }
        return totalPrice
    }

    /**
     * 修改购物车商品数量
     */
    fun changeCartNumber(cartId: Int, number: Int) {

        requestApi(mRetrofit.changeMallCartNumber(cartId, number), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }

        })
    }

    fun clearStoreCart() {

    }

    fun deleteCart(mAdapter: MallCartAdapter) {

    }


}
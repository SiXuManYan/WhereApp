package com.jiechengsheng.city.features.mall.cart

import android.annotation.SuppressLint
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.CartDeleteRequest
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.mall.MallCartGroup
import com.jiechengsheng.city.api.response.mall.MallExpired
import com.jiechengsheng.city.api.response.mall.MallSpecs
import com.jiechengsheng.city.features.store.cart.child.OnChildReselectSkuClick
import com.jiechengsheng.city.features.store.cart.child.OnChildSelectClick
import com.jiechengsheng.city.features.store.cart.child.OnGroupSelectClick
import com.jiechengsheng.city.features.store.cart.child.StoreCartValueChangeListener
import com.jiechengsheng.city.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/14 17:49.
 *
 */

interface MallCartView : BaseMvpView,
    SwipeRefreshLayout.OnRefreshListener,
    StoreCartValueChangeListener,
    OnChildSelectClick,
    OnGroupSelectClick,
    OnChildReselectSkuClick {

    fun bindData(data: MutableList<MallCartGroup>, lastPage: Boolean)
    fun changeNumberSuccess()
    fun deleteSuccess()

    /** sku 更新成功 */
    fun changeSkuSuccess(mallSpecs: MallSpecs, number: Int)

    /** 清空购物车 */
    fun clearStoreCartSuccess()

    /** 处理失效商品 */
    fun bindExpired(apply: MallCartGroup)

    /** 无失效商品 */
    fun bindExpiredEmpty()

}

class MallCartPresenter(private var view: MallCartView) : BaseMvpPresenter(view) {

    fun getData(page: Int) {
        requestApi(mRetrofit.mallCartList(page), object : BaseMvpObserver<PageResponse<MallCartGroup>>(view,page) {
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


        val editMode = adapter.isEditMode



        adapter.data.forEachIndexed { index, it ->

            if (adapter.getItemViewType(index) == 1) {
                return@forEachIndexed
            }


            if (editMode) {
                it.nativeIsSelectEdit = select
            } else {
                it.nativeIsSelect = select
            }

            it.gwc.forEach { child ->

                if (editMode) {
                    child.nativeIsSelectEdit = select
                } else {
                    child.nativeIsSelect = select
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 用于结算
     */
    fun getSelectedCount(adapter: MallCartAdapter): Int {
        var count = 0



        adapter.data.forEachIndexed { index, it ->

            if (adapter.getItemViewType(index) == 1) {
                return@forEachIndexed
            }


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

        adapter.data.forEachIndexed { index, it ->

            if (adapter.getItemViewType(index) == 1) {
                return@forEachIndexed
            }

            val group = MallCartGroup().apply {
                shop_id = it.shop_id
                title = it.title
                nativeIsSelect = true
                delivery_fee = it.delivery_fee
                nativeExpiredData =  ArrayList<MallExpired>()
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
        adapter.data.forEachIndexed { index, data ->

            if (adapter.getItemViewType(index) == 1) {
                return@forEachIndexed
            }

            data.gwc.forEach {
                if (it.nativeIsSelect && it.nativeEnable) {

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
                view.changeNumberSuccess()
            }

        })
    }


    /**
     * 顶级是否全部选中
     */
    fun checkSelectAll(adapter: MallCartAdapter): Boolean {

        val editMode = adapter.isEditMode

        val result = ArrayList<Boolean>()
        result.clear()
        adapter.data.forEachIndexed { index, data ->

//            result.add(data.nativeIsSelect)
            if (adapter.getItemViewType(index) == 1) {
                return@forEachIndexed
            }

            data.gwc.forEach {
                if (it.nativeEnable) {
                    if (editMode) {
                        result.add(it.nativeIsSelectEdit)
                    } else {
                        result.add(it.nativeIsSelect)
                    }
                }
            }
        }
        return !result.contains(false)
    }

    /**
     * 清空购物车
     * @param clear 0清空正常商品 1清空失效商品
     */
    fun clearStoreCart(clear:Int) {
        requestApi(mRetrofit.clearMallCart(clear), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
                view.clearStoreCartSuccess()
            }
        })
    }

    /**
     * 删除购物车
     */
    fun deleteCart(adapter: MallCartAdapter) {
        if (adapter.data.isEmpty()) {
            return
        }
        val delete = ArrayList<Int>()
        adapter.data.forEachIndexed { index, it ->

            if (adapter.getItemViewType(index) == 1) {
                return@forEachIndexed
            }

            it.gwc.forEach { child ->
                if (child.nativeIsSelectEdit) {
                    delete.add(child.cart_id)
                }
            }
        }
        if (delete.isEmpty()) {
            return
        }
        val toJson = Gson().toJson(delete)
        val apply = CartDeleteRequest().apply {
            cart_id = toJson
        }

        requestApi(mRetrofit.deleteMallCart(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.deleteSuccess()
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }
        })
    }


    fun changeSku(cartId: Int, specsId: Int, number: Int, mallSpecs: MallSpecs) {
        requestApi(mRetrofit.changeMallSku(cartId, specsId, number), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.changeSkuSuccess(mallSpecs, number)
            }

        })
    }

    fun getExpiredGoods() {
        requestApi(mRetrofit.expiredGoods(), object : BaseMvpObserver<ArrayList<MallExpired>>(view) {
            override fun onSuccess(response: ArrayList<MallExpired>) {
                if (response.isEmpty()) {
                    view.bindExpiredEmpty()
                    return
                }
                val apply = MallCartGroup().apply {
                    nativeIsNormalType = 1
                    nativeExpiredData.addAll(response)
                }
                view.bindExpired(apply)
            }

        })

    }


}
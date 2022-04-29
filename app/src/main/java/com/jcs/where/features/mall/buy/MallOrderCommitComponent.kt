package com.jcs.where.features.mall.buy

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.GeCouponDefault
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.request.*
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.BusinessUtils
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/15 13:57.
 *
 */
interface MallOrderCommitView : BaseMvpView {
    fun commitSuccess(response: MallCommitResponse)

    fun bindTotalDelivery(totalServiceDeliveryFee: BigDecimal?)

    /**
     * 设置优惠
     * @param platformCouponId          平台优惠券id
     * @param platformCouponTotalMoney  平台优惠券总金额
     * @param shopCouponTotalMoney      所有店铺优惠券的总金额
     */
    fun bindDefaultCoupon(platformCouponId: Int, platformCouponTotalMoney: BigDecimal, shopCouponTotalMoney: BigDecimal)

}


class MallOrderCommitPresenter(private var view: MallOrderCommitView) : BaseMvpPresenter(view) {


    /**
     * 计算最终支付价格
     * @param totalServiceDeliveryFee 总配送费
     * @param totalPlatformCouponMoney 平台优惠券总金额
     * @param totalShopCouponMoney 店铺优惠券总金额
     *
     */
    fun handlePrice(
        adapter: MallOrderCommitAdapter, totalServiceDeliveryFee: BigDecimal?,
        totalPlatformCouponMoney: BigDecimal?,
        totalShopCouponMoney: BigDecimal,
    ): BigDecimal {


        // 所有商品价格
        val allGoodPrice = getAllGoodPrice(adapter)

        // 所有店铺配送费
        val allDeliveryFee = getTotalDeliveryFee(adapter)

        // 原价（商品+配送费）
        val oldPrice = BigDecimalUtil.add(allGoodPrice, allDeliveryFee)

        // 所有优惠券总金额
        val couponMoney = BigDecimalUtil.add(totalPlatformCouponMoney, totalShopCouponMoney)

        // 最终价格（原价-所有优惠价格）
        val finalPrice = BigDecimalUtil.sub(oldPrice, couponMoney)

        return finalPrice
    }


    /** 获取所有商品的价格（除去配送费） */
    fun getAllGoodPrice(adapter: MallOrderCommitAdapter): BigDecimal {


        // 所有商品价格
        var allGoodPrice = BigDecimal.ZERO

        adapter.data.forEach { shop ->

            // allDeliveryFee = BigDecimalUtil.add(allDeliveryFee, shop.delivery_fee)

            shop.gwc.forEach { good ->
                // 商品价格
                val goodPrice = BigDecimalUtil.mul(good.specs_info!!.price, BigDecimal(good.good_num))
                allGoodPrice = BigDecimalUtil.add(allGoodPrice, goodPrice)
            }
        }

        return allGoodPrice
    }

    /**
     * 订单提交
     */
    fun orderCommit(data: ArrayList<MallCartGroup>, addressId: String?, couponId: Int?) {

        val bean = MallOrderCommit().apply {
            address_id = addressId
            specsIds = getSpecsIdsJsonString(data)
            goods = getGoodsJsonString(data)

            if (couponId != 0) {
                platform_coupon_id = couponId
            }
        }

        requestApi(mRetrofit.mallOrderCommit(bean), object : BaseMvpObserver<MallCommitResponse>(view) {
            override fun onSuccess(response: MallCommitResponse) {
                view.commitSuccess(response)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }

        })
    }


    private fun getSpecsIdsJsonString(data: ArrayList<MallCartGroup>): String {

        val specsIdsArray = ArrayList<String>()

        data.forEach { group ->

            val itemGroup = MallOrderCommitGoodGroup().apply {
                remark = group.nativeRemark
                shop_id = group.shop_id
            }

            group.gwc.forEach { gwc ->

                specsIdsArray.add(gwc.specs_id.toString() + "," + gwc.good_num.toShort())

                val item = MallOrderCommitGoodItem().apply {
                    good_id = gwc.good_id
                    num = gwc.good_num
                    specs_id = gwc.specs_id
                    cart_id = gwc.cart_id
                }

                itemGroup.goods.add(item)
            }
        }

        return Gson().toJson(specsIdsArray)
    }

    fun getGoodsJsonString(data: ArrayList<MallCartGroup>): String {

        val goodsGroup = ArrayList<MallOrderCommitGoodGroup>()
        data.forEach { group ->

            val itemGroup = MallOrderCommitGoodGroup().apply {
                remark = group.nativeRemark
                shop_id = group.shop_id
                coupon_id = group.nativeShopCouponId
            }

            group.gwc.forEach { gwc ->


                val item = MallOrderCommitGoodItem().apply {
                    good_id = gwc.good_id
                    num = gwc.good_num
                    specs_id = gwc.specs_id
                    cart_id = gwc.cart_id
                }

                itemGroup.goods.add(item)
            }
            goodsGroup.add(itemGroup)
        }
        return Gson().toJson(goodsGroup)
    }

    /** 具体店铺的 商品json */
    fun getShopGoodsJsonString(data: ArrayList<MallCartGroup>, shopId: Int): String {

        val goodsGroup = ArrayList<MallOrderCommitGoodGroup>()
        data.forEach { group ->

            if (group.shop_id == shopId) {
                val itemGroup = MallOrderCommitGoodGroup().apply {
                    remark = group.nativeRemark
                    shop_id = group.shop_id
                    coupon_id = group.nativeShopCouponId
                }

                group.gwc.forEach { gwc ->


                    val item = MallOrderCommitGoodItem().apply {
                        good_id = gwc.good_id
                        num = gwc.good_num
                        specs_id = gwc.specs_id
                        cart_id = gwc.cart_id
                    }

                    itemGroup.goods.add(item)
                }
                goodsGroup.add(itemGroup)

                return@forEach
            }


        }
        return Gson().toJson(goodsGroup)
    }


    /**
     * 用户选中新的店铺券后，刷新列表中的店铺券 id
     */
    fun updateItemShopCouponId(adapter: MallOrderCommitAdapter, shopId: Int, newShopCouponId: Int) {
        var needRefresh = false
        adapter.data.forEach {
            if (it.shop_id == shopId && it.nativeShopCouponId != newShopCouponId) {
                it.nativeShopCouponId = newShopCouponId
                needRefresh = true
                return@forEach
            }
        }
        if (needRefresh) {
            val index = adapter.data.indexOfFirst { it.shop_id == shopId }
            adapter.notifyItemChanged(index)
        }

    }


    /**
     * 通过城市id获取店铺配送费
     */
    fun getDelivery(adapter: MallOrderCommitAdapter, cityId: Int) {

        val data = adapter.data
        if (data.isEmpty()) {
            return
        }

        val apply = MallDeliveryRequest().apply {
            city_id = cityId
        }

        val goods = ArrayList<ArrayList<MallDeliverItem>>()

        data.forEach { shop ->
            val allGoods = ArrayList<MallDeliverItem>()
            shop.gwc.forEach { gwc ->

                val item = MallDeliverItem().apply {
                    shop_id = shop.shop_id
                    goods_id = gwc.good_id
                    count = gwc.good_num
                }
                allGoods.add(item)
            }
            goods.add(allGoods)
        }
        apply.goods = Gson().toJson(goods)

        requestApi(mRetrofit.shopDelivery(apply), object : BaseMvpObserver<MallDeliveryResponse>(view) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(response: MallDeliveryResponse) {
                val deliveryFeeData = response.delivery_fee

                // 刷新列表中的配送费
                adapter.data.forEach {
                    val shopIdKey = it.shop_id.toString()
                    if (deliveryFeeData.containsKey(shopIdKey)) {
//                        it.nativeShopDelivery = deliveryFeeData[shopIdKey]
                        it.delivery_fee = deliveryFeeData[shopIdKey]!!

                    }
                }
                adapter.notifyDataSetChanged()

                // 获取总配送费
                var totalServiceDeliveryFee = BigDecimal.ZERO
                if (deliveryFeeData.containsKey("total")) {
                    totalServiceDeliveryFee = deliveryFeeData["total"] as BigDecimal
//                    totalServiceDeliveryFee = BigDecimal(100) // test
                }

                view.bindTotalDelivery(totalServiceDeliveryFee)


            }
        })


    }


    /**
     * 获取总配送费
     */
    fun getTotalDeliveryFee(adapter: MallOrderCommitAdapter): BigDecimal {

        var allDeliveryFee = BigDecimal.ZERO

        adapter.data.forEach { shop ->
            allDeliveryFee = BigDecimalUtil.add(allDeliveryFee, shop.delivery_fee)

        }

        return allDeliveryFee
    }

    /**
     * 获取默认优惠券,刷新各个店铺商品对应的优惠金额
     * @param currentPlatformCouponId 当前最新平台优惠券id
     */
    @SuppressLint("NotifyDataSetChanged")
    fun getDefaultCoupon(
        adapter: MallOrderCommitAdapter,
        data: ArrayList<MallCartGroup>,
        currentPlatformCouponId: Int? = null,
        isFirstRequest: Boolean? = false,
    ) {

        val apply = MallOrderDefaultCoupon().apply {
            goods = getGoodsJsonString(data)

            platform_coupon_id = if (currentPlatformCouponId == 0) {
                null
            } else {
                currentPlatformCouponId
            }

            isFirstRequest?.let {
                status = if (isFirstRequest) {
                    null
                } else {
                    1
                }


            }
        }

        requestApi(mRetrofit.getDefaultCoupon(apply), object : BaseMvpObserver<GeCouponDefault>(view) {

            override fun onSuccess(response: GeCouponDefault) {


                if (response.shop_coupon.isNotEmpty()) {

                    // 刷新列表中的各个店铺优惠券金额
                    response.shop_coupon.forEachIndexed { index, child ->
                        adapter.data.forEach {
                            val shopIdKey = it.shop_id

                            if (child.shop_id == shopIdKey) {
                                it.nativeShopCouponPrice = BusinessUtils.getSafeBigDecimal(child.money)
                                it.nativeShopCouponId = BusinessUtils.getSafeInt(child.coupon_id)
                            }
                        }
                    }

                    adapter.notifyDataSetChanged()
                }


                view.bindDefaultCoupon(
                    platformCouponId = BusinessUtils.getSafeInt(response.order_coupon.coupon_id),
                    platformCouponTotalMoney = BusinessUtils.getSafeBigDecimal(response.order_coupon.money),
                    shopCouponTotalMoney = BusinessUtils.getSafeBigDecimal(response.shop_total_coupon)
                )
            }

            override fun onError(errorResponse: ErrorResponse?) = Unit

        })

    }


}
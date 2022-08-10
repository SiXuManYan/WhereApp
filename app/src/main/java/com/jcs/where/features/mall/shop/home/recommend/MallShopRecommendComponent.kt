package com.jcs.where.features.mall.shop.home.recommend

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.Coupon
import com.jcs.where.api.response.GetCouponResult
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.MallShopRecommend
import com.jcs.where.api.response.mall.ShopRecommend
import com.jcs.where.api.response.mall.request.MallGoodListRequest

/**
 * Created by Wangsw  2022/2/24 14:49.
 *
 */

interface ShopRecommendView : BaseMvpView {
    /** 优惠券 */
    fun bindCoupon(response: MutableList<Coupon>)

    /** 店铺推荐 */
    fun bindRecommend(response: ArrayList<ShopRecommend>)

    /** 商品列表 */
    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)

    /** 领取优惠券 */
    fun getCouponResult(message: String)
}

class ShopRecommendPresenter(private var view: ShopRecommendView) : BaseMvpPresenter(view) {

    /** 获取商户优惠券 */
    fun requestShopCoupon(shopId: Int) {

        requestApi(mRetrofit.mallShopCoupon(shopId), object : BaseMvpObserver<ArrayList<Coupon>>(view) {

            override fun onSuccess(response: ArrayList<Coupon>) {
                response.forEach {
                    it.nativeListType = Coupon.TYPE_FOR_SHOP_HOME_PAGE
                }
                view.bindCoupon(response.toMutableList())
            }

        })
    }


    /** 领取商户优惠券 */
    fun getShopCoupon(couponId: Int, couponType: Int) {

        requestApi(mRetrofit.getCoupon(couponId, couponType), object : BaseMvpObserver<GetCouponResult>(view) {
            override fun onSuccess(response: GetCouponResult) {
                view.getCouponResult(response.message)
            }

        })
    }


    fun getRecommend(shopId: Int) {

        requestApi(mRetrofit.recommendMallShop(shopId), object : BaseMvpObserver<ArrayList<ArrayList<MallShopRecommend>>>(view) {

            override fun onSuccess(response: ArrayList<ArrayList<MallShopRecommend>>) {

                val headerData = ArrayList<ShopRecommend>()

                response.forEachIndexed { index, arrayList ->
                    val apply = ShopRecommend().apply {
                        recommend.addAll(arrayList)
                    }
                    headerData.add(apply)
                }
                view.bindRecommend(headerData)

            }

        })
    }


    fun getMallList(request: MallGoodListRequest) {
        requestApi(mRetrofit.getMallGoodList(
            request.page,
            request.order?.name,
            request.title,
            request.categoryId,
            request.startPrice,
            request.endPrice,
            request.sold?.name,
            request.shopId,
            request.shop_categoryId,
            request.recommend,
            request.coupon_id
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view, request.page) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == request.page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })
    }

}
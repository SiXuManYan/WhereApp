package com.jcs.where.features.mall.shop.home.recommend

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.MallShopRecommend
import com.jcs.where.api.response.mall.ShopRecommend
import com.jcs.where.api.response.mall.request.MallGoodListRequest
import kotlin.collections.ArrayList

/**
 * Created by Wangsw  2022/2/24 14:49.
 *
 */

interface ShopRecommendView : BaseMvpView {
    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)
    fun bindRecommend(response: ArrayList<ShopRecommend>)
}

class ShopRecommendPresenter(private var view: ShopRecommendView) : BaseMvpPresenter(view) {


    fun getRecommend(shopId: Int) {

        requestApi(mRetrofit.recommendMallShop(shopId), object : BaseMvpObserver<ArrayList<ArrayList<MallShopRecommend>>>(view) {

            override fun onSuccess(response: ArrayList<ArrayList<MallShopRecommend>>) {

                val headerData  = ArrayList<ShopRecommend>()

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
            request.recommend
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == request.page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })
    }

}
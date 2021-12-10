package com.jcs.where.features.mall.second

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.request.MallGoodListRequest
import java.util.*

/**
 * Created by Wangsw  2021/12/3 15:30.
 *
 */
interface MallSecondView : BaseMvpView {
    fun bindThirdCategory(response: ArrayList<MallCategory>)
    fun bindData(toMutableList: MutableList<MallGood>, lastPage: Boolean)

}

class MallSecondPresenter(private var view: MallSecondView) : BaseMvpPresenter(view) {

    fun getThirdCategory(secondCategoryId: Int) {

        requestApi(mRetrofit.getMallThirdCategory(secondCategoryId), object : BaseMvpObserver<ArrayList<MallCategory>>(view) {
            override fun onSuccess(response: ArrayList<MallCategory>) {
                if (response.isNotEmpty()) {
                    response[0].nativeIsSelected = true
                }
                view.bindThirdCategory(response)
            }

        })
    }


    fun getMallList(request: MallGoodListRequest) {
        requestApi(mRetrofit.getMallGoodList(
            request.page,
            request.order?.name,
            request.title,
            request. categoryId,
            request.startPrice,
            request. endPrice,
            request.sold?. name,
            request.shopId
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == request.page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })
    }


}


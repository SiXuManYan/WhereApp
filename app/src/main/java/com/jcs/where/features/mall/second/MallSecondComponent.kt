package com.jcs.where.features.mall.second

import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
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
                // 手动添加二级分类
                response.add(0,
                    MallCategory().apply {
                        id = secondCategoryId
                        name = StringUtils.getString(R.string.all)
                        icon = ""
                        second_level = ArrayList<MallCategory>()
                        nativeIsSelected = true
                    }
                )

                view.bindThirdCategory(response)
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


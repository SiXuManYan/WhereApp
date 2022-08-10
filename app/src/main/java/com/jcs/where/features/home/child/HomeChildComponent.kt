package com.jcs.where.features.home.child

import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.SPKey
import com.jcs.where.utils.SPUtil

/**
 * Created by Wangsw  2022/6/21 14:26.
 *
 */

interface HomeChildView : BaseMvpView {
    fun bindComplexRecommend(data: MutableList<HomeRecommendResponse>, lastPage: Boolean) {

    }

    fun bindMallRecommend(toMutableList: MutableList<MallGood>, lastPage: Boolean) {

    }

}

class HomeChildPresenter(private var view: HomeChildView) : BaseMvpPresenter(view) {


    /**
     * 推荐列表
     */
    fun getComplexRecommend(page: Int, categoryId: Int? = null) {
        val areaId = SPUtil.getInstance().getString(SPKey.SELECT_AREA_ID)

        val selectLatLng = CacheUtil.getSafeSelectLatLng()


        requestApi(
            mRetrofit.getRecommends(page, selectLatLng.latitude.toString(), selectLatLng.longitude.toString(), areaId, categoryId),
            object : BaseMvpObserver<PageResponse<HomeRecommendResponse>>(view, page) {
                override fun onSuccess(response: PageResponse<HomeRecommendResponse>) {
                    val isLastPage = response.lastPage == page
                    val data = response.data.toMutableList()

                    view.bindComplexRecommend(data, isLastPage)
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    super.onError(errorResponse)
                }
            })
    }


    fun getMallRecommend(categoryId: Int, page: Int) {

        requestApi(mRetrofit.getMallRecommendGood(1, page, categoryId), object : BaseMvpObserver<PageResponse<MallGood>>(view,page) {
            override fun onSuccess(response: PageResponse<MallGood>) {

                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindMallRecommend(data.toMutableList(), isLastPage)
            }
        })
    }

}
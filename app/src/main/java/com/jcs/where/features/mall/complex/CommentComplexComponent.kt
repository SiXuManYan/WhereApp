package com.jcs.where.features.mall.complex

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.hotel.HotelComment
import com.jcs.where.api.response.mall.MallCommentCount

/**
 * Created by Wangsw  2022/5/20 11:54.
 *
 */
interface CommentComplexView : BaseMvpView, SwipeRefreshLayout.OnRefreshListener,
    OnLoadMoreListener,
    MallCommentHeader.CommentHeaderClickListener {
    fun bindData(data: MutableList<HotelComment>, lastPage: Boolean)
    fun bindCount(response: MallCommentCount)

}

class CommentComplexPresenter(private var view: CommentComplexView) : BaseMvpPresenter(view) {


    fun getCommentList(businessId: Int, page: Int, type: Int, useType: Int) {

        when (useType) {
            0 -> {
                requestApi(mRetrofit.getMallCommentList(businessId, type, page),
                    object : BaseMvpObserver<PageResponse<HotelComment>>(view, page) {

                        override fun onSuccess(response: PageResponse<HotelComment>) {
                            val isLastPage = response.lastPage == page
                            val data = response.data
                            view.bindData(data.toMutableList(), isLastPage)
                        }
                    })
            }
            1 -> {
                requestApi(mRetrofit.getHotelCommentList(businessId, type, page),
                    object : BaseMvpObserver<PageResponse<HotelComment>>(view, page) {

                        override fun onSuccess(response: PageResponse<HotelComment>) {
                            val isLastPage = response.lastPage == page
                            val data = response.data
                            view.bindData(data.toMutableList(), isLastPage)
                        }

                    })

            }
            else -> {}
        }


    }

    fun loadCount(businessId: Int, useType: Int) {

        when (useType) {

            0 -> {
                requestApi(mRetrofit.mallCommentCount(businessId), object : BaseMvpObserver<MallCommentCount>(view) {
                    override fun onSuccess(response: MallCommentCount) {
                        view.bindCount(response)
                    }

                })

            }
            else -> {
                requestApi(mRetrofit.hotelCommentCount(businessId, "newest"), object : BaseMvpObserver<MallCommentCount>(view) {
                    override fun onSuccess(response: MallCommentCount) {
                        view.bindCount(response)
                    }

                })

            }
        }


    }


}
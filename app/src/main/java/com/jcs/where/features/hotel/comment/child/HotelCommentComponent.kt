package com.jcs.where.features.hotel.comment.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.hotel.HotelComment

/**
 * Created by Wangsw  2021/8/17 10:58.
 *
 */
interface HotelCommentView : BaseMvpView {
    fun bindData(data: MutableList<HotelComment>, lastPage: Boolean)

}

class HotelCommentPresenter(private var view: HotelCommentView) : BaseMvpPresenter(view) {


    fun getStoreCommentList(hotel_id: Int, page: Int, type: Int) {

        requestApi(mRetrofit.getHotelCommentList(hotel_id ,type, page ), object : BaseMvpObserver<PageResponse<HotelComment>>(view) {

            override fun onSuccess(response: PageResponse<HotelComment>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList() , isLastPage)
            }

        })
    }


}

package com.jcs.where.features.gourmet.comment

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.gourmet.comment.CommentResponse
import com.jcs.where.api.response.hotel.HotelComment

/**
 * Created by Wangsw  2021/5/27 16:01.
 *
 */
class FoodCommentPresenter(val view: FoodCommentView) : BaseMvpPresenter(view) {

    /**
     * 美食评论列表
     */
    fun getFoodCommentList(restaurantId: String, page: Int, type: Int) {
        requestApi(mRetrofit.getFoodCommentList(page, type, restaurantId), object : BaseMvpObserver<PageResponse<HotelComment>>(view) {
            override fun onSuccess(response: PageResponse<HotelComment>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindCommentData(data.toMutableList(), isLastPage)
            }
        })
    }




}
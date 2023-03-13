package com.jiechengsheng.city.features.gourmet.comment

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.hotel.HotelComment

/**
 * Created by Wangsw  2021/5/27 16:01.
 *
 */
class FoodCommentPresenter(val view: FoodCommentView) : BaseMvpPresenter(view) {

    /**
     * 美食评论列表
     */
    fun getFoodCommentList(restaurantId: String, page: Int, type: Int) {
        requestApi(mRetrofit.getFoodCommentList(page, type, restaurantId),
            object : BaseMvpObserver<PageResponse<HotelComment>>(view, page) {
                override fun onSuccess(response: PageResponse<HotelComment>) {
                    val isLastPage = response.lastPage == page
                    val data = response.data
                    view.bindCommentData(data.toMutableList(), isLastPage)
                }
            })
    }


}
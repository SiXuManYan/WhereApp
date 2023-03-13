package com.jiechengsheng.city.features.store.detail.comment.chiild

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.hotel.HotelComment

/**
 * Created by Wangsw  2021/7/16 15:25.
 *
 */

interface StoreCommentView : BaseMvpView {
    fun bindCommentData(data: MutableList<HotelComment>, isLastPage: Boolean, total: Int)
}


class StoreCommentPresenter(private var view: StoreCommentView) : BaseMvpPresenter(view) {

    /**
     * 商城评论列表
     */
    fun getStoreCommentList(shop_id: Int, page: Int, showAll: Boolean, type: Int) {

        requestApi(mRetrofit.getStoreCommentList2(page, shop_id.toString(), type), object : BaseMvpObserver<PageResponse<HotelComment>>(view,page) {
            override fun onSuccess(response: PageResponse<HotelComment>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                if (!showAll && data.size > 2) {
                    view.bindCommentData(data.subList(0, 2).toMutableList(), isLastPage,response.total)
                } else {
                    view.bindCommentData(data.toMutableList(), isLastPage,response.total)
                }

            }
        })
    }

}
package com.jcs.where.features.store.detail.comment

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.gourmet.comment.CommentResponse

/**
 * Created by Wangsw  2021/7/16 15:25.
 *
 */

interface StoreCommentView : BaseMvpView {
    fun bindCommentData(data: MutableList<CommentResponse>, isLastPage: Boolean)
}


class StoreCommentPresenter(private var view: StoreCommentView) : BaseMvpPresenter(view) {

    /**
     * 商城评论列表
     */
    fun getStoreCommentList(shop_id: Int, page: Int, showAll: Boolean, type: Int) {

        requestApi(mRetrofit.getStoreCommentList(page, shop_id.toString(), type), object : BaseMvpObserver<PageResponse<CommentResponse>>(view) {
            override fun onSuccess(response: PageResponse<CommentResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data


                if (!showAll && data.size > 2) {
                    view.bindCommentData(data.subList(0, 2).toMutableList(), isLastPage)
                } else {
                    view.bindCommentData(data.toMutableList(), isLastPage)
                }

            }
        })
    }

}
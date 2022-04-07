package com.jcs.where.features.store.comment.detail

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.comment.StoreCommentDetail

/**
 * Created by Wangsw  2021/7/15 15:55.
 *
 */

interface StoreCommentDetailView : BaseMvpView {

    fun bingDetail(response: StoreCommentDetail)
}


class StoreCommentDetailPresenter(private var view: StoreCommentDetailView) : BaseMvpPresenter(view) {


    fun getStoreComment(orderId: Int) {

        requestApi(mRetrofit.storeCommentDetail(orderId), object : BaseMvpObserver<StoreCommentDetail>(view) {

            override fun onSuccess(response: StoreCommentDetail) {
                view.bingDetail(response)
            }
        })
    }


    fun getMallComment(commentId: Int) {
        requestApi(mRetrofit.mallCommentDetail(commentId), object : BaseMvpObserver<StoreCommentDetail>(view) {
            override fun onSuccess(response: StoreCommentDetail) {
                view.bingDetail(response)
            }
        })

    }

}
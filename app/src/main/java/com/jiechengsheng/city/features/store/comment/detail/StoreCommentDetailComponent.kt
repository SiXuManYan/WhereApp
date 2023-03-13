package com.jiechengsheng.city.features.store.comment.detail

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.comment.StoreCommentDetail

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
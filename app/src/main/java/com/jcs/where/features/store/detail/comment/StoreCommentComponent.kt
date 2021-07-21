package com.jcs.where.features.store.detail.comment

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.comment.StoreCommentCount

/**
 * Created by Wangsw  2021/7/21 16:25.
 *
 */

interface StoreCommentListView : BaseMvpView {
    fun bindCount(response: StoreCommentCount)

}

class StoreCommentListPresenter(private var view: StoreCommentListView) : BaseMvpPresenter(view) {

    fun getCount(shopId: Int) {

        requestApi(mRetrofit.getStoreCommentCount(shopId), object : BaseMvpObserver<StoreCommentCount>(view) {
            override fun onSuccess(response: StoreCommentCount) {
                view.bindCount(response)
            }

        })
    }
}


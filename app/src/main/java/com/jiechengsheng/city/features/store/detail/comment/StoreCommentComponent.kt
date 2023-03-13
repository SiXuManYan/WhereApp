package com.jiechengsheng.city.features.store.detail.comment

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.comment.StoreCommentCount

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


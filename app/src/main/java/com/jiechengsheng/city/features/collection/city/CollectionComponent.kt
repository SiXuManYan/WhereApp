package com.jiechengsheng.city.features.collection.city

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.collection.MyCollection

/**
 * Created by Wangsw  2021/11/16 16:13.
 *
 */
interface CollectionView : BaseMvpView {

    fun bindData(data: MutableList<MyCollection>, lastPage: Boolean)

}

class CollectionPresenter(private var view: CollectionView) : BaseMvpPresenter(view) {


    fun getData(page: Int, type: Int) {
        requestApi(mRetrofit.getCollection(page, type), object : BaseMvpObserver<PageResponse<MyCollection>>(view,page) {
            override fun onSuccess(response: PageResponse<MyCollection>) {

                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }


}

package com.jiechengsheng.city.features.search.result

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.search.SearchResultResponse

/**
 * Created by Wangsw  2021/8/23 15:15.
 *
 */
interface SearchAllResultView : BaseMvpView {
    fun bindSearchResult(toMutableList: MutableList<SearchResultResponse>)

}

class SearchAllResultPresenter(private var view: SearchAllResultView) : BaseMvpPresenter(view) {


    /**
     * 搜索数据
     *
     * @param finalInput
     */
    fun search(finalInput: String?) {
        requestApi(mRetrofit.getSearchResult(finalInput), object : BaseMvpObserver<List<SearchResultResponse>>(view) {
            override fun onSuccess(response: List<SearchResultResponse>) {

                view.bindSearchResult(response.toMutableList())
            }

        })
    }
}
package com.jcs.where.features.search.result

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.search.SearchResultResponse

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
package com.jcs.where.features.footprint.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.footprint.Footprint

/**
 * Created by Wangsw  2021/11/18 16:41.
 *
 */
interface FootprintView : BaseMvpView {
    fun bindData(data: MutableList<Footprint>, lastPage: Boolean)

}

class FootprintPresenter(private var view: FootprintView) : BaseMvpPresenter(view) {


    fun getData(page: Int, type: Int) {


        if (type == 0) {
            requestApi(mRetrofit.getFootprint(page), object : BaseMvpObserver<PageResponse<Footprint>>(view, page) {
                override fun onSuccess(response: PageResponse<Footprint>) {

                    val isLastPage = response.lastPage == page
                    val data = response.data

                    addTitle(data)

                    view.bindData(data.toMutableList(), isLastPage)
                }
            })
        } else {
            requestApi(mRetrofit.getGoodFootprint(page), object : BaseMvpObserver<PageResponse<Footprint>>(view, page) {
                override fun onSuccess(response: PageResponse<Footprint>) {

                    val isLastPage = response.lastPage == page
                    val data = response.data
                    addTitle(data)
                    view.bindData(data.toMutableList(), isLastPage)
                }
            })
        }

    }

    private fun addTitle(data: MutableList<Footprint>) {

        val groupBy = data.groupBy { it.date }

        groupBy.forEach { group ->

            val titleEntity = Footprint().apply {
                date = group.key
                this.type = Footprint.TYPE_TITLE
            }


            val indexOfFirst = data.indexOfFirst {
                it.date == group.key
            }

            data.add(indexOfFirst, titleEntity)
        }
    }

}
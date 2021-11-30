package com.jcs.where.features.mall.home

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category

/**
 * Created by Wangsw  2021/11/30 16:22.
 *
 */
interface MallHomeView : BaseMvpView {
    fun bindCategory(response: ArrayList<Category>, titles: ArrayList<String>)

}


class MallHomePresenter(private var view: MallHomeView) : BaseMvpPresenter(view) {


    fun getFirstCategory() {
        requestApi(mRetrofit.storeCategoryFirst, object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {
                val titles: ArrayList<String> = ArrayList()
                response.forEach {
                    titles.add(it.name)
                }


                view.bindCategory(response,titles)
            }
        })
    }

}
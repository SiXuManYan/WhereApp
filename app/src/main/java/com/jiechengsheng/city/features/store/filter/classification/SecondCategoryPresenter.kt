package com.jiechengsheng.city.features.store.filter.classification

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.category.Category
import java.util.*

/**
 * Created by Wangsw  2021/6/9 15:03.
 *
 */
class SecondCategoryPresenter(val view: SecondCategoryView) : BaseMvpPresenter(view) {


    fun getSecondCategory(pid: Int) {

        requestApi(mRetrofit.getStoreCategoryNext(pid), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {
                view.bindData(response)
            }

        })
    }


}
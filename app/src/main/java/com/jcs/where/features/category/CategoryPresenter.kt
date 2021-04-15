package com.jcs.where.features.category

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/15 15:12.
 *
 */
class CategoryPresenter(val view: CategoryView) : BaseMvpPresenter(view) {

    fun getCategoryList() {
        requestApi(mRetrofit.categoryList,object :BaseMvpObserver<ArrayList<Category>>(view){
            override fun onSuccess(response: ArrayList<Category>?) {
                if (!response.isNullOrEmpty()) {
                    view.bindData(response)
                }
            }
        })

    }


}
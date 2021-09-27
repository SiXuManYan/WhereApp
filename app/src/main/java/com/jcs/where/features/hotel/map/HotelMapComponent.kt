package com.jcs.where.features.hotel.map

import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/9/27 14:07.
 *
 */

interface HotelMapView : BaseMvpView {

    fun bindCategory(response: ArrayList<Category>)
}

class HotelMapPresenter(private var view: HotelMapView) : BaseMvpPresenter(view) {

    fun getHotelChildCategory(categoryId: Int) {

        requestApi(mRetrofit.getCategoriesList(3, categoryId.toString(), 2), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {

                // 当前二级分类全部
                val all = Category().apply {
                    name = StringUtils.getString(R.string.all)
                    id = categoryId
                    has_children = 1
                    nativeIsSelected = true
                }
                response.add(0, all)

                view.bindCategory(response)
            }

        })
    }


}
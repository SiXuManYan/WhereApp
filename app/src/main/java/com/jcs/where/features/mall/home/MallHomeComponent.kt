package com.jcs.where.features.mall.home

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.category.UserCategory
import com.jcs.where.utils.MD5Tool

/**
 * Created by Wangsw  2021/11/30 16:22.
 *
 */
interface MallHomeView : BaseMvpView {
    fun bindCategory(response: ArrayList<Category>, titles: ArrayList<String>)

}


class MallHomePresenter(private var view: MallHomeView) : BaseMvpPresenter(view) {


    fun getFirstCategory() {

//        requestApi(mRetrofit.storeCategoryFirst, object : BaseMvpObserver<ArrayList<Category>>(view) {
//            override fun onSuccess(response: ArrayList<Category>) {
//                val titles: ArrayList<String> = ArrayList()
//                response.forEach {
//                    titles.add(it.name)
//                }
//
//
//                view.bindCategory(response,titles)
//            }
//        })


        // 拿全部分类暂时替代
        val deviceId = MD5Tool.getFakerAndroidUuid()
        requestApi(mRetrofit.getEditableCategory(deviceId), object : BaseMvpObserver<UserCategory>(view) {
            override fun onSuccess(response: UserCategory) {

                val titles: ArrayList<String> = ArrayList()
                response.follow.forEach {
                    titles.add(it.name)
                }

                view.bindCategory( response.follow,titles)
            }
        })

    }



}
package com.jcs.where.features.mall.shop.home.category

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallShopCategory
import java.util.ArrayList

/**
 * Created by Wangsw  2022/1/24 10:31.
 *
 */
interface MallShopCategoryView : BaseMvpView {
    fun bindCategory(response: ArrayList<MallShopCategory>)
}

class MallShopCategoryPresenter(private var view: MallShopCategoryView) : BaseMvpPresenter(view) {

    fun getCategory(shopId :Int){
        requestApi(mRetrofit.mallShopCategory(shopId),object :BaseMvpObserver<ArrayList<MallShopCategory>>(view){
            override fun onSuccess(response: ArrayList<MallShopCategory>) {
                view.bindCategory(response)
            }

        })

    }


}
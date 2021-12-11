package com.jcs.where.features.mall.detail

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallGoodDetail

/**
 * Created by Wangsw  2021/12/10 14:57.
 *
 */
interface MallDetailView : BaseMvpView {
    fun bindDetail(response: MallGoodDetail)
}

class MallDetailPresenter(private var view: MallDetailView) : BaseMvpPresenter(view) {

    fun getDetail(goodId:Int){

        requestApi(mRetrofit.getMallGoodDetail(19),object :BaseMvpObserver<MallGoodDetail>(view){
            override fun onSuccess(response: MallGoodDetail) {
                view.bindDetail(response)
            }
        })
    }
}
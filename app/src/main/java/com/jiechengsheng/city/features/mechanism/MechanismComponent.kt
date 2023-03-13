package com.jiechengsheng.city.features.mechanism

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.MechanismDetailResponse

/**
 * Created by Wangsw  2021/9/4 15:08.
 *
 */
interface MechanismView : BaseMvpView {
    fun bindDetail(response: MechanismDetailResponse)
    fun collectionHandleSuccess(collectionStatus: Int)
}


class MechanismPresenter(private var view: MechanismView) : BaseMvpPresenter(view) {


    fun getDetail(infoId: Int) {

        requestApi(mRetrofit.getMechanismDetailById(infoId), object : BaseMvpObserver<MechanismDetailResponse>(view) {
            override fun onSuccess(response: MechanismDetailResponse) {
                view.bindDetail(response)
            }

        })
    }

    fun collection(infoId: Int) {
        requestApi(mRetrofit.postCollectMechanism(infoId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(2)
            }
        })
    }

    fun unCollection(infoId: Int) {

        requestApi(mRetrofit.delCollectMechanism(infoId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(1)
            }
        })
    }

}
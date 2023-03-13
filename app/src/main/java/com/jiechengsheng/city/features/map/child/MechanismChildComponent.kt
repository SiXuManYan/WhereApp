package com.jiechengsheng.city.features.map.child

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.MechanismResponse
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.utils.CacheUtil

/**
 * Created by Wangsw  2021/8/26 14:27.
 *
 */
interface MechanismChildView : BaseMvpView {

    fun bindData(response: MutableList<MechanismResponse>, isLastPage: Boolean, total: Int)

}


class MechanismChildPresenter(private var view: MechanismChildView) : BaseMvpPresenter(view) {

    fun getData(page: Int, categoryId: String, search: String?) {

        val safeSelectLatLng = CacheUtil.getSafeSelectLatLng()


        requestApi(mRetrofit.getMechanismList(
            page,
            categoryId,
            null,
            search,
            safeSelectLatLng.latitude,
            safeSelectLatLng.longitude
        ), object : BaseMvpObserver<PageResponse<MechanismResponse>>(view,page) {
            override fun onSuccess(response: PageResponse<MechanismResponse>) {
                val total = response.total
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage, total)
            }
        })
    }
}
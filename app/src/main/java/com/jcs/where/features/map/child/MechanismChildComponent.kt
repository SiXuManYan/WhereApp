package com.jcs.where.features.map.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.utils.Constant
import com.jcs.where.utils.SPKey
import com.jcs.where.utils.SPUtil

/**
 * Created by Wangsw  2021/8/26 14:27.
 *
 */
interface MechanismChildView : BaseMvpView {

    fun bindData(response: MutableList<MechanismResponse>, isLastPage: Boolean, total: Int)

}


class MechanismChildPresenter(private var view: MechanismChildView) : BaseMvpPresenter(view) {

    fun getData(page: Int, categoryId: String, search: String) {
        val areaIdStr = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID)

        requestApi(mRetrofit.getMechanismList(
            page,
            categoryId,
            areaIdStr,
            search,
            Constant.LAT,
            Constant.LNG
        ), object : BaseMvpObserver<PageResponse<MechanismResponse>>(view) {
            override fun onSuccess(response: PageResponse<MechanismResponse>) {
                val total = response.total
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage,total)
            }
        })
    }
}
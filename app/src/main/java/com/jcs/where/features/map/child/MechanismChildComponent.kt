package com.jcs.where.features.map.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.utils.Constant
import com.jcs.where.utils.SPKey
import com.jcs.where.utils.SPUtil

/**
 * Created by Wangsw  2021/8/26 14:27.
 *
 */
interface MechanismChildView : BaseMvpView {

    fun bindData(response: MutableList<MechanismResponse>)

}


class MechanismChildPresenter(private var view: MechanismChildView) : BaseMvpPresenter(view) {

    fun getData(categoryId: String, search: String) {
        val areaIdStr = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID)

        val areaId: Int = try {
            areaIdStr.toInt()
        } catch (e: Exception) {
            0
        }

        requestApi(mRetrofit.getMechanismListForMap(
            categoryId,
            areaId,
            search,
            Constant.LAT,
            Constant.LNG
        ), object : BaseMvpObserver<List<MechanismResponse>>(view) {
            override fun onSuccess(response: List<MechanismResponse>) {
                view.bindData(response.toMutableList())
            }

        })
    }
}
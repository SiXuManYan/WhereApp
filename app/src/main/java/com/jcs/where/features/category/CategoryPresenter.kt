package com.jcs.where.features.category

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.category.UserCategory
import com.jcs.where.utils.MD5Tool

/**
 * Created by Wangsw  2021/4/15 15:12.
 *
 */
class CategoryPresenter(val view: CategoryView) : BaseMvpPresenter(view) {

    fun getCategoryList() {
        val deviceId = MD5Tool.getFakerAndroidUuid()
        requestApi(mRetrofit.getEditableCategory(deviceId), object : BaseMvpObserver<UserCategory>(view) {
            override fun onSuccess(response: UserCategory?) {
                if (response == null) {
                    return
                }
                view.bindData(response.follow)
            }
        })

    }


}
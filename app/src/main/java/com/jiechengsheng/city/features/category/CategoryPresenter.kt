package com.jiechengsheng.city.features.category

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.category.UserCategory
import com.jiechengsheng.city.utils.MD5Tool

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
package com.jiechengsheng.city.features.category.edit

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.api.response.category.UserCategory
import com.jiechengsheng.city.bean.FollowCategoryRequest
import com.jiechengsheng.city.utils.MD5Tool
import java.util.*

/**
 * Created by Wangsw  2021/4/16 10:45.
 *
 */
class CategoryEditPresenter(private var view: CategoryEditView) : BaseMvpPresenter(view) {


    fun getEditableCategory() {
        val deviceId = MD5Tool.getFakerAndroidUuid()
        requestApi(mRetrofit.getEditableCategory(deviceId), object : BaseMvpObserver<UserCategory>(view) {
            override fun onSuccess(response: UserCategory?) {
                if (response == null) {
                    return
                }
                view.bindFollowData(response.follow)
                view.bindUnFollowData(response.unfollow)

            }
        })


    }

    /**
     * 关注分类
     */
    fun followCategory(newFollow: MutableList<Category>) {

        val ids = ArrayList<Int>()

        newFollow.forEach {
            ids.add(it.id)
        }

        val request = FollowCategoryRequest().apply {
            device_id = MD5Tool.getFakerAndroidUuid()
            category_ids = ids.toString()
        }
        requestApi(mRetrofit.followCategory(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
              view.followSuccess()
            }
        })

    }


}
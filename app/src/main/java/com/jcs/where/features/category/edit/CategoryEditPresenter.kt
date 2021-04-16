package com.jcs.where.features.category.edit

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.category.UserCategory
import com.jcs.where.bean.FollowCategoryRequest
import com.jcs.where.utils.MD5Tool
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
    fun followCategory(newFollow: ArrayList<Category>) {

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
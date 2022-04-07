package com.jcs.where.features.category.edit

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/16 10:45.
 *
 */
interface CategoryEditView :BaseMvpView{

    /**
     * 未关注分类
     */
    fun bindFollowData(follow: ArrayList<Category>)

    /**
     * 已关注的分类
     */
    fun bindUnFollowData(unFollow: ArrayList<Category>)

    /**
     * 关注成功
     */
    fun followSuccess()


}
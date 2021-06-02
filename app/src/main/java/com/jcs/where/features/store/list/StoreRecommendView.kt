package com.jcs.where.features.store.list

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.store.StoreRecommend
import java.util.ArrayList

/**
 * Created by Wangsw  2021/6/1 10:32.
 *
 */
interface StoreRecommendView : BaseMvpView {
    fun bindBanner(response: ArrayList<Category>)
    fun bindRecommend(response: ArrayList<StoreRecommend>)
}
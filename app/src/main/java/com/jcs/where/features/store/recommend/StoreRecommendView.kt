package com.jcs.where.features.store.recommend

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.StoryBannerCategory
import com.jcs.where.api.response.store.StoreRecommend
import java.util.ArrayList

/**
 * Created by Wangsw  2021/6/1 10:32.
 *
 */
interface StoreRecommendView : BaseMvpView {
    fun bindRecommend(response: ArrayList<StoreRecommend>)
    fun bindBannerData(result: ArrayList<StoryBannerCategory>)
}
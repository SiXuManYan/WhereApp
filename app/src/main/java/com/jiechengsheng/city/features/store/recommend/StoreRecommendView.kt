package com.jiechengsheng.city.features.store.recommend

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.category.StoryBannerCategory
import com.jiechengsheng.city.api.response.store.StoreRecommend
import java.util.ArrayList

/**
 * Created by Wangsw  2021/6/1 10:32.
 *
 */
interface StoreRecommendView : BaseMvpView {
    fun bindRecommend(response: ArrayList<StoreRecommend>)
    fun bindBannerData(result: ArrayList<StoryBannerCategory>)
}
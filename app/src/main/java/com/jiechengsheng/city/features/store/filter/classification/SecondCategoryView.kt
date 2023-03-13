package com.jiechengsheng.city.features.store.filter.classification

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/6/9 15:02.
 *
 */
interface SecondCategoryView :BaseMvpView {
    fun bindData(response: ArrayList<Category>)
}
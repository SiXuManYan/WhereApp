package com.jcs.where.features.store.filter.classification

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/6/9 15:02.
 *
 */
interface SecondCategoryView :BaseMvpView {
    fun bindData(response: ArrayList<Category>)
}
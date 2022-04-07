package com.jcs.where.features.category

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/15 15:12.
 *
 */
interface CategoryView :BaseMvpView {
    fun bindData(response: ArrayList<Category>)
}
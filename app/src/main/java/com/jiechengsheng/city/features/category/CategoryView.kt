package com.jiechengsheng.city.features.category

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.category.Category
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/15 15:12.
 *
 */
interface CategoryView :BaseMvpView {
    fun bindData(response: ArrayList<Category>)
}
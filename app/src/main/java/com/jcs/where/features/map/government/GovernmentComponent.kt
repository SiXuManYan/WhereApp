package com.jcs.where.features.map.government

import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import java.util.*

/**
 * Created by Wangsw  2021/8/24 17:04.
 *
 */
interface GovernmentView : BaseMvpView {

    fun bindGovernmentChildCategory(response: ArrayList<Category>)

}

open class GovernmentPresenter(private val view: GovernmentView) : BaseMvpPresenter(view) {


    private val ID_GOVERNMENT = 1

    /**
     * 获取"政府"的子分类
     */
    fun getGovernmentChildCategory() {
        requestApi(mRetrofit.getCategoriesList(2, ID_GOVERNMENT,2), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {

                // 当前二级分类全部
                val secondAll = Category().apply {
                    name = StringUtils.getString(R.string.all)
                    id = ID_GOVERNMENT
                    has_children = 1
                    nativeIsSelected = true
                }
                response.add(0, secondAll)

                response.forEach {

                    // 三级分类手动添加全部
                    if (it.has_children == 2 && it.child_categories.isNotEmpty()) {
                        val thirdAll = Category().apply {
                            name = StringUtils.getString(R.string.all)
                            id = it.id
                            has_children = 1
                            nativeIsSelected = true
                        }
                        it.child_categories.add(0, thirdAll)
                    }

                }
                view.bindGovernmentChildCategory(response)
            }

        })


    }


}

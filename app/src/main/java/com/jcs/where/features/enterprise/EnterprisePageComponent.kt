package com.jcs.where.features.enterprise

import com.blankj.utilcode.util.StringUtils
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category

/**
 * Created by Wangsw  2022/9/13 16:21.
 *
 */
interface EnterprisePageView : BaseMvpView {
    fun bindCategory(response: java.util.ArrayList<Category>)

}

class EnterprisePagePresenter(private var view: EnterprisePageView) : BaseMvpPresenter(view) {


    /**
     * 获取企业黄页内的所有分类
     * @param firstCategoryIds 企业所有黄页一级分类id
     *
     * @param currentCategoryId 用户当前选中二级分类id（用于高亮）
     */
    fun getAllCategory(firstCategoryIds: ArrayList<Int>, currentCategoryId: Int) {
        if (firstCategoryIds.isNullOrEmpty()) {
            return
        }
        val toJson = Gson().toJson(firstCategoryIds)

        requestApi(mRetrofit.getCategoriesList(1, toJson, 2), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {


                // 一级分类增加全部
                val firstAll = Category().apply {
                    name = StringUtils.getString(R.string.all)
                    id = 0
                    has_children = 1
                    nativeIsSelected = true
                }
                response.add(0, firstAll)


                // 二级增加全部
                response.forEach { first ->


                    if (first.has_children == 2 && first.child_categories.isNotEmpty()) {

                        // 所有二级分类，增加全部
                        val secondAll = Category().apply {
                            name = StringUtils.getString(R.string.all)
                            id = first.id // 分类使用当前一级分类id
                            has_children = 1
                        }
                        first.child_categories.add(0, secondAll)
                    }
                }

                // 三级增加全部
                response.forEach { first ->

                    if (first.has_children == 2 && first.child_categories.isNotEmpty()) {

                        first.child_categories.forEach { second ->

                            // 匹配高亮
                            if (second.id == currentCategoryId) {
                                second.nativeIsSelected = true
                            }
                            if (second.has_children == 2 &&  second.child_categories.isNotEmpty()){

                                val thirdAll = Category().apply {
                                    name = StringUtils.getString(R.string.all)
                                    id = second.id // 分类使用当前二级分类id
                                    has_children = 1
                                    nativeIsSelected = true
                                }
                                second.child_categories.add(0, thirdAll)
                            }
                        }
                    }

                }


                view.bindCategory(response)
            }
        })
    }

}
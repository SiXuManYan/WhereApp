package com.jcs.where.features.mall.detail.sku

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallAttributeValue
import com.jcs.where.api.response.mall.MallSpecs
import com.jcs.where.api.response.mall.SkuDataSource

/**
 * Created by Wangsw  2021/12/13 14:46.
 *
 */
interface MallSkuView : BaseMvpView {

}

interface MallSkuSelectResult {
    fun selectResult(mallSpecs: MallSpecs, goodNum: Int)

}

class MallSkuPresenter(private var view: MallSkuView) : BaseMvpPresenter(view) {


    fun filterTargetSpecsList2(source: SkuDataSource, userSelect: ArrayList<MallAttributeValue>): ArrayList<MallSpecs> {

        // 1.筛选出符合目标的结果集

        val specs = ArrayList<MallSpecs>()
        source.specs.forEach { ms ->

            if (userSelect.isEmpty()) {
                // 用户为选择任何，全集都可用
                specs.add(ms)
            } else {
                if (ms.specs.values.containsAll(getUserSelectedValues(userSelect))) {
                    specs.add(ms)
                }
            }

        }
        return specs
    }


    private fun getUserSelectedValues(userSelect: ArrayList<MallAttributeValue>): ArrayList<String> {
        val userSelectedValue = ArrayList<String>()
        userSelect.forEach {
            userSelectedValue.add(it.name)
        }
        return userSelectedValue

    }

    /**
     * 获取所有用户选中的项目
     */
    fun getAllUserSelectedAttributeValue(mAdapter: SkuFirstAdapter): ArrayList<MallAttributeValue> {

        val value = ArrayList<MallAttributeValue>()

        mAdapter.data.forEach { group ->
            group.value.forEach { item ->
                if (item.nativeIsSelected == 1) {
                    value.add(item)
                }
            }
        }
        return value
    }
}
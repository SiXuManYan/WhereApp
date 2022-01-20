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


    /**
     * 获取所有用户选中的项目
     * 颜色：黑
     * 容量：45
     */
    fun getAllSelectedValue(mAdapter: SkuFirstAdapter): ArrayList<MallAttributeValue> {
        val value = ArrayList<MallAttributeValue>()
        mAdapter.data.forEachIndexed { index, group ->

            // 每一组
            group.value.forEach { item ->

                if (item.nativeIsSelected == 1) {
                    value.add(item)
                }
            }
        }

        return value
    }


    /**
     * 筛选出复合条件的SKU集合
     */
    fun getTargetResult(source: SkuDataSource, alreadySelect: ArrayList<MallAttributeValue>): ArrayList<MallSpecs> {

        val specs = ArrayList<MallSpecs>()

        source.specs.forEach { ms ->

            if (alreadySelect.isEmpty()) {
                // 用户未来选择任何，全集（有库存时）都可用
                if (ms.stock > 0) {
                    specs.add(ms)
                }
            } else {
                // h 45  k65    ==  黑色 45
                if (ms.specs.values.containsAll(getUserSelectedValues(alreadySelect))) {
                    if (ms.stock > 0) {
                        specs.add(ms)
                    }
                }
            }
        }
        return specs
    }

    fun getDefaultResult(source: SkuDataSource): ArrayList<MallSpecs> {
        val specs = ArrayList<MallSpecs>()
        source.specs.forEach { ms ->
            // 用户未来选择任何，全集（有库存时）都可用
            if (ms.stock > 0) {
                specs.add(ms)
            }
        }
        return specs
    }

    fun getDefaultResultValue(source: SkuDataSource): ArrayList<String> {
       return getResultValue( getDefaultResult(source))
    }

    fun getResultValue(matchSpecsList: ArrayList<MallSpecs>): ArrayList<String> {

        // 所有符合条件的属性值
        val targetAttrs = ArrayList<String>()
        targetAttrs.clear()
        matchSpecsList.forEach { ms ->
            val values = ms.specs.values
            values.forEach {
                targetAttrs.add(it)
            }
        }

        return targetAttrs
    }


    /**
     * 所有用户选中的值
     */
    private fun getUserSelectedValues(userSelect: ArrayList<MallAttributeValue>): ArrayList<String> {
        val userSelectedValue = ArrayList<String>()
        userSelect.forEach {
            userSelectedValue.add(it.name)
        }
        return userSelectedValue

    }


}
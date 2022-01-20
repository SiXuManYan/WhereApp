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
    fun filterTargetSpecsList(source: SkuDataSource, alreadySelect: ArrayList<MallAttributeValue>): ArrayList<MallSpecs> {

        val specs = ArrayList<MallSpecs>()

        source.specs.forEach { ms ->

            if (alreadySelect.isEmpty()) {
                // 用户未来选择任何，全集（有库存时）都可用
                if (ms.stock > 0) {
                    specs.add(ms)
                }
            } else {
                if (ms.specs.values.containsAll(getUserSelectedValues(alreadySelect))) {
                    if (ms.stock > 0) {
                        specs.add(ms)
                    }
                }
            }
        }
        return specs
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
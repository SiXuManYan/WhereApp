package com.jcs.where.features.mall.detail.sku

import android.annotation.SuppressLint
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallAttribute
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
     * // 当前行
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
        return getResultValue(getDefaultResult(source))
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

    // 1.当前选中的值，和之前选中的值去交集
    // 2.设置列表可选状态
    // 3.

    // #################################         ###############################


    fun getAllSelectedItem2(mAdapter: SkuFirstAdapter): ArrayList<MallAttributeValue> {

        val result = ArrayList<MallAttributeValue>()

        mAdapter.data.forEachIndexed { index, group ->
            // 每一组
            group.value.forEach { item ->
                if (item.nativeSelected) {
                    result.add(item)
                }
            }
        }
        return result
    }


    /**
     * 筛选出所有符合条件的SKU结果，单独筛选最后结果去重
     * @param skuSource   sku 数据源 list
     * @param allSelected 已选中的属性 list
     */
    fun getSkuResultList(skuSource: ArrayList<MallSpecs>, allSelected: ArrayList<MallAttributeValue>): ArrayList<MallSpecs> {

        // 最终结果去重
        val result = ArrayList<MallSpecs>()

        // 用户未选择任何属性
        // 匹配其他以选中的组
        if (allSelected.isEmpty()) {
            skuSource.forEach {
                if (it.stock > 0) {
                    result.add(it)
                }
            }
            return result
        }

        skuSource.forEach { sourceItem ->
            if (sourceItem.specs.values.containsAll(getUserSelectedValues(allSelected)) && sourceItem.stock > 0){
                result.add(sourceItem)
            }
        }


        return distinctList(result)
    }

    /***
     * 元素去重
     */
    private fun distinctList(oldList: ArrayList<MallSpecs>): ArrayList<MallSpecs> {
        val distinctBy = oldList.distinctBy {
            it.id
        }
        return ArrayList(distinctBy)
    }

    /**
     * 刷新列表UI可用
     */
    @SuppressLint("NotifyDataSetChanged")
    fun changeViewStatus(mAdapter: SkuFirstAdapter, resultSkuList: ArrayList<MallSpecs>, secondDataItem: MallAttributeValue?) {

        mAdapter.data.forEach { uiGroup ->

            // 颜色 使用结果集筛选全部
            uiGroup.value.forEachIndexed { index, value ->
                var enabled = false
                resultSkuList.forEach { sku ->
                    sku.specs.forEach {
                        if (it.key == value.key && it.value == value.name) {
                            enabled = true
                        }
                    }
                }
                value.nativeEnable = enabled
            }


            // 筛选其他选中的组
            if (secondDataItem!=null && uiGroup.key != secondDataItem.key  && hasSelectedGroup(uiGroup)){
                uiGroup.value.forEachIndexed { index, value ->
                    var enabled = false
                    resultSkuList.forEach { sku ->
                        sku.specs.forEach {
                            if (it.key == value.key && it.value == value.name) {
                                enabled = true
                            }
                        }
                    }
                    value.nativeEnable = enabled
                }

            }

        }
        mAdapter.notifyDataSetChanged()
    }




    /**
     * 当前组是否有选中元素
     */
    private fun hasSelectedGroup(uiGroup: MallAttribute) :Boolean{
        var hasSelected = false
        uiGroup.value.forEach {
            if (it.nativeSelected) {
                hasSelected = true
            }
        }

        return hasSelected

    }



}
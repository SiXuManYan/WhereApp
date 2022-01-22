package com.jcs.where.features.mall.detail.sku

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallAttributeValue
import com.jcs.where.api.response.mall.MallSpecs
import com.jcs.where.api.response.mall.SkuDataSource
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.MyLayoutManager
import com.jcs.where.widget.NumberView2
import kotlinx.android.synthetic.main.fragment_mall_sku.*

/**
 * Created by Wangsw  2021/12/13 14:45.
 *
 */
class MallSkuFragment : BaseBottomSheetDialogFragment<MallSkuPresenter>(), MallSkuView, TargetGoodItemClickCallBack {

    var selectResult: MallSkuSelectResult? = null
    var result: MallSpecs? = null

    lateinit var data: SkuDataSource


    private lateinit var mAdapter: SkuFirstAdapter

    private var value = ArrayList<MallAttributeValue>()

    var goodNumber = 1

    override fun getLayoutId() = R.layout.fragment_mall_sku


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 8 / 10)

    override fun initView(parent: View) {

        // content
        initSkuLayout()
        initOther()
    }

    private fun initOther() {

        price_tv.text = getString(R.string.price_unit_format, data.min_price)
        GlideUtil.load(requireContext(), data.main_image, good_iv, 4)

        number_view.apply {
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1

            cut_iv.setImageResource(R.mipmap.ic_cut_blue_transparent)
            add_iv.setImageResource(R.mipmap.ic_add_blue_transparent)

            cutResIdCommon = R.mipmap.ic_cut_blue
            cutResIdMin = R.mipmap.ic_cut_blue_transparent
            addResIdCommon = R.mipmap.ic_add_blue
            addResIdMax = R.mipmap.ic_add_blue_transparent

            if (result != null) {
                MAX_GOOD_NUM = result!!.stock
                add_iv.setImageResource(R.mipmap.ic_add_blue)
            } else {
                MAX_GOOD_NUM = data.stock
            }
            updateNumber(goodNumber)

            cut_iv.visibility = View.VISIBLE

            // 没有SKU 且 用户之前没有选择过
            if (data.attribute_list.isNotEmpty() && result == null) {
                cut_iv.isClickable = false
                add_iv.isClickable = false
                cut_iv.setImageResource(R.mipmap.ic_cut_blue_transparent)
                add_iv.setImageResource(R.mipmap.ic_add_blue_transparent)
            }

            valueChangeListener = object : NumberView2.OnValueChangeListener {
                override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
                    goodNumber = goodNum
                }

            }
        }
        stock_tv.text = StringUtils.getString(R.string.stock_format, data.stock)
    }

    private fun initSkuLayout() {
        mAdapter = SkuFirstAdapter().apply {
            targetGoodItemClickCallBack = this@MallSkuFragment
        }
        content_rv.apply {
            adapter = mAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }


    }

    override fun initData() {
        presenter = MallSkuPresenter(this)
        if (data.attribute_list.isEmpty() && data.specs.isNotEmpty()) {
            result = data.specs[0]
        }
        mAdapter.setNewInstance(data.attribute_list)
        initDefault(null)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            dismiss()
        }

        confirm_tv.setOnClickListener {
            if (result == null) {
                ToastUtils.showShort(R.string.please_selected)
                return@setOnClickListener
            }
            // 判断是否所有必选都选中
            mAdapter.data.forEach {
                it.value.forEach { value ->
                    if (value.nativeIsSelected == 0) {
                        ToastUtils.showShort(getString(R.string.please_selected) + it.key)
                        return@setOnClickListener
                    }
                }
            }
            if (number_view.goodNum == 0) {
                ToastUtils.showShort(getString(R.string.inventory_shortage_select))
                return@setOnClickListener
            }
            selectResult?.selectResult(result!!, number_view.goodNum)
            dismissAllowingStateLoss()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onAttrItemClick() {

        // 用户选中属性
        val allSelected: ArrayList<MallAttributeValue> = presenter.getAllSelectedValue(mAdapter)

        // 复合条件的SKU
        val matchSpecsList: ArrayList<MallSpecs> = presenter.getTargetResult(data, allSelected)

        // 所有符合条件的SKU属性值   // h 45  k65
        val selectedResultValues: ArrayList<String> = presenter.getResultValue(matchSpecsList)

        // 默认所有SKU合集 值
        val defaultResultValue: ArrayList<String> = presenter.getDefaultResultValue(data)

        // 根据存在的属性，匹配列表中的可选状态
        mAdapter.data.forEach { group ->

            // 每一组
            group.value.forEachIndexed { childIndex, item ->

                // 存在即为可操作
                if (selectedResultValues.contains(item.name)) {
                    when (item.nativeIsSelected) {
                        0 -> item.nativeIsSelected = 0
                        1 -> item.nativeIsSelected = 1
                        2 -> item.nativeIsSelected = 0
                    }
                } else {
//                    if (defaultResultValue.contains(item.name)) {
//                        item.nativeIsSelected = 0
//                    } else {
//                        item.nativeIsSelected = 2
//                    }

                    item.nativeIsSelected = 2

                }

            }
        }


        // 只选中1项目, 同组设置为可选
        if (allSelected.size == 1) {
            val index = mAdapter.getFirstUserSelectedIndex()
            if (index != -1) {
                mAdapter.data[index].value.forEach {
//                    it.name

                    when (it.nativeIsSelected) {
                        0 -> it.nativeIsSelected = 0
                        1 -> it.nativeIsSelected = 1
                        2 -> {

                            if (defaultResultValue.contains(it.name)) {
                                // 结果集中包含选项，可选
                                it.nativeIsSelected = 0
                            } else {
                                // 初始化筛选时，无库存，保持不可选中状态
                                it.nativeIsSelected = 2
                            }
                        }
                    }
                }
            }
        }

        mAdapter.notifyDataSetChanged()

        if (matchSpecsList.size == 1) {
            val mallSpecs = matchSpecsList[0]
            result = mallSpecs
            price_tv.text = getString(R.string.price_unit_format, mallSpecs.price.toPlainString())

            val stock = mallSpecs.stock

            stock_tv.text = StringUtils.getString(R.string.stock_format, stock)

            number_view.apply {
                if (stock <= 0) {
                    MIN_GOOD_NUM = 0
                }
                MAX_GOOD_NUM = stock
                updateNumber(MIN_GOOD_NUM)
                cut_iv.isClickable = true
                add_iv.isClickable = true
                add_iv.setImageResource(R.mipmap.ic_add_blue)
            }
        }

    }

    override fun onAttrItemClick2(secondDataItem: MallAttributeValue?) {
        val resultSkuList: ArrayList<MallSpecs> = initDefault(secondDataItem)




        if (resultSkuList.size == 1) {
            val mallSpecs = resultSkuList[0]
            result = mallSpecs
            price_tv.text = getString(R.string.price_unit_format, mallSpecs.price.toPlainString())

            val stock = mallSpecs.stock

            stock_tv.text = StringUtils.getString(R.string.stock_format, stock)

            number_view.apply {
                if (stock <= 0) {
                    MIN_GOOD_NUM = 0
                }
                MAX_GOOD_NUM = stock
                updateNumber(MIN_GOOD_NUM)
                cut_iv.isClickable = true
                add_iv.isClickable = true
                add_iv.setImageResource(R.mipmap.ic_add_blue)
            }
        }

    }

    private fun initDefault(secondDataItem: MallAttributeValue?): ArrayList<MallSpecs> {
        // 用户选中属性
        val allSelectedItem: ArrayList<MallAttributeValue> = presenter.getAllSelectedItem2(mAdapter)

        // 所有条件的SKU
        val resultSkuList: ArrayList<MallSpecs> = presenter.getSkuResultList(data.specs, allSelectedItem)

        // 刷新列表选中状态
        presenter.changeViewStatus(mAdapter, resultSkuList, secondDataItem)
        return resultSkuList
    }

}
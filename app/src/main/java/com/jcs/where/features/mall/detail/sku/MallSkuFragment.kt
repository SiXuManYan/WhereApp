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
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 9 / 10)

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

        mAdapter.setNewInstance(data.attribute_list)

    }

    override fun initData() {
        presenter = MallSkuPresenter(this)
        if (data.attribute_list.isEmpty() && data.specs.isNotEmpty()) {
            result = data.specs[0]
        }
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            dismiss()
        }

        confirm_tv.setOnClickListener {
            if (result == null) {
                ToastUtils.showShort("请选择")
                return@setOnClickListener
            }
            // 判断是否所有必选都选中
            mAdapter.data.forEach {
                it.value.forEach { value ->
                    if (value.nativeIsSelected == 0) {
                        ToastUtils.showShort("请选择" + it.key)
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
    override fun onItemClick2(userSelect: MallAttributeValue) {
        val allUserSelected = presenter.getAllUserSelectedAttributeValue(mAdapter)
        val matchSpecsList = presenter.filterTargetSpecsList2(data, allUserSelected)
        // 2.筛选结果集中包含的所有商品属性，刷新列表中的选中状态

        // 所有属性值
        val attributes = ArrayList<String>()
        matchSpecsList.forEach {
            val resultValues = it.specs.values
            resultValues.forEach {
                attributes.add(it)
            }

        }

        // 根据存在的属性，匹配列表中的可选状态
        mAdapter.data.forEach { group ->

            group.value.forEach { item ->
                // 存在即为可操作
                if (attributes.contains(item.name)) when (item.nativeIsSelected) {
                    0 -> item.nativeIsSelected = 0
                    1 -> item.nativeIsSelected = 1
                    2 -> item.nativeIsSelected = 0
                } else {
                    // 不存在记为不可操作
                    item.nativeIsSelected = 2
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

}
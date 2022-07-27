package com.jcs.where.features.mall.sku.other

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.features.mall.sku.bean.Sku
import com.jcs.where.features.mall.sku.bean.SkuAttribute
import com.jcs.where.features.mall.sku.view.OnSkuListener
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.dialog_product_sku.*

/**
 * Created by Wangsw  2022/7/26 15:46.
 *
 */
class SkuFragment : BaseBottomSheetDialogFragment<SkuPresenter>(), SkuView {


    private var product: Product? = null
    private var skuListData: List<Sku>? = null
    var callback: Callback? = null
    var selectedSku: Sku? = null

    var mustSelectedAttrSize = 0

    override fun getLayoutId() = R.layout.dialog_product_sku


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 8 / 10)

    override fun initView(parent: View) {
        // 不可编辑
        number_value_et.isFocusable = false
        number_value_et.isFocusableInTouchMode = false

    }

    override fun initData() {
        presenter = SkuPresenter(this)
        updateSkuData2()
//        updateQuantityOperator(1)

        showLastSku(selectedSku)

    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            dismissAllowingStateLoss()
        }
        number_cut_iv.setOnClickListener {
            val quantity = number_value_et.getText().toString()
            if (TextUtils.isEmpty(quantity)) {
                return@setOnClickListener
            }
            val quantityInt = quantity.toInt()
            if (quantityInt > 1) {
                val newQuantity = (quantityInt - 1).toString()
                number_value_et.setText(newQuantity)
                number_value_et.setSelection(newQuantity.length)
                updateQuantityOperator(quantityInt - 1)
            }
        }

        number_add_iv.setOnClickListener {
            val quantity = number_value_et.text.toString()

            if (TextUtils.isEmpty(quantity) || selectedSku == null) {
                return@setOnClickListener
            }
            val quantityInt = quantity.toInt()
            if (quantityInt < selectedSku!!.stockQuantity) {
                val newQuantity = (quantityInt + 1).toString()
                number_value_et.setText(newQuantity)
                number_value_et.setSelection(newQuantity.length)
                updateQuantityOperator(quantityInt + 1)
            }
        }

        number_value_et.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE || selectedSku == null) {
                return@setOnEditorActionListener false
            }
            val quantity = number_value_et.getText().toString()
            if (TextUtils.isEmpty(quantity)) {
                return@setOnEditorActionListener false
            }
            val quantityInt = quantity.toInt()
            when {
                quantityInt <= 1 -> {
                    number_value_et.setText("1")
                    number_value_et.setSelection(1)
                    updateQuantityOperator(1)
                }
                quantityInt >= selectedSku!!.stockQuantity -> {
                    val newQuantity = selectedSku!!.stockQuantity.toString()
                    number_value_et.setText(newQuantity)
                    number_value_et.setSelection(newQuantity.length)
                    updateQuantityOperator(selectedSku!!.stockQuantity)
                }
                else -> {
                    number_value_et.setSelection(quantity.length)
                    updateQuantityOperator(quantityInt)
                }
            }
            false

        }

        scroll_sku_list.setListener(object : OnSkuListener {
            override fun onUnselected(unselectedAttribute: SkuAttribute?) {
                selectedSku = null

                product?.let {
                    GlideUtil.load(context, it.main_image, iv_sku_logo)
                    tv_sku_quantity.text = StringUtils.getString(R.string.stock_format, it.stock)
                }


                val firstUnselectedAttributeName = scroll_sku_list.firstUnelectedAttributeName
                tv_sku_info.text = "请选择：$firstUnselectedAttributeName"
//                confirm_tv.isEnabled = false


                val quantity = number_value_et.text.toString()
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity))
                } else {
                    updateQuantityOperator(0)
                }
            }

            override fun onSelect(selectAttribute: SkuAttribute?) {
                val firstUnselectedAttributeName = scroll_sku_list.getFirstUnelectedAttributeName()
                tv_sku_info.setText("请选择：$firstUnselectedAttributeName")
            }

            override fun onSkuSelected(sku: Sku?) {
                selectedSku = sku
                if (selectedSku == null) {
                    return
                }

                GlideUtil.load(context, selectedSku!!.mainImage, iv_sku_logo)
                val attributeList = selectedSku!!.attributes
                val builder = StringBuilder()
                for (i in attributeList.indices) {
                    if (i != 0) {
                        builder.append("　")
                    }
                    val attribute = attributeList[i]
                    builder.append("\"" + attribute.value + "\"")
                }
                tv_sku_info.text = "已选：$builder"

                tv_sku_quantity.text = StringUtils.getString(R.string.stock_format, selectedSku!!.stockQuantity)

                val sellingPrice = selectedSku!!.sellingPrice
                val originPrice = selectedSku!!.originPrice

                BusinessUtils.setNowPriceAndOldPrice(sellingPrice, originPrice, tv_sku_selling_price, original_price_tv)

//                confirm_tv.isEnabled = true
                val quantity = number_value_et.text.toString()
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity))
                } else {
                    updateQuantityOperator(0)
                }
            }

        })

        confirm_tv.setOnClickListener {

            if (selectedSku == null) {
                ToastUtils.showShort(R.string.please_selected)
                return@setOnClickListener
            }

            val size = selectedSku!!.attributes.size
            if (size != mustSelectedAttrSize) {
                ToastUtils.showShort(R.string.please_selected)
                return@setOnClickListener
            }

            if (selectedSku!!.stockQuantity <= 0) {
                ToastUtils.showShort(R.string.inventory_shortage_select)
                return@setOnClickListener
            }


            val quantity = number_value_et.text.toString()

            if (TextUtils.isEmpty(quantity)) {
                return@setOnClickListener
            }
            val quantityInt = quantity.toInt()
            if (quantityInt > 0 && quantityInt <= selectedSku!!.stockQuantity) {
                callback?.onAdded(selectedSku, quantityInt)
                dismissAllowingStateLoss()
            } else {
                ToastUtils.showShort("The quantity is out of stock, please revise")
            }
        }
    }


    fun setData(product: Product) {
        this.product = product
        skuListData = product.skus
        if (product.skus.isNotEmpty()) {
            mustSelectedAttrSize = product.skus[0].attributes.size
        }

    }


    public fun updateSkuData() {
        scroll_sku_list.setSkuList(product!!.skus)
        if (product!!.skus.isEmpty()) {
            return
        }

        val firstSku = product!!.skus[0]
        if (firstSku.stockQuantity > 0) {
            selectedSku = firstSku
            // 选中第一个sku
            scroll_sku_list.selectedSku = selectedSku
            GlideUtil.load(context, selectedSku?.mainImage, iv_sku_logo)

            val sellingPrice = selectedSku!!.sellingPrice
            val originPrice = selectedSku!!.originPrice

            BusinessUtils.setNowPriceAndOldPrice(sellingPrice, originPrice, tv_sku_selling_price, original_price_tv)


            tv_sku_quantity.text = StringUtils.getString(R.string.stock_format, selectedSku!!.stockQuantity)

//            confirm_tv.isEnabled = selectedSku!!.stockQuantity > 0
            val attributeList = selectedSku!!.attributes
            val builder = StringBuilder()
            for (i in attributeList.indices) {
                if (i != 0) {
                    builder.append("　")
                }
                val attribute = attributeList[i]
                builder.append("\"" + attribute.value + "\"")
            }
            tv_sku_info.text = "已选：$builder"
        } else {
            GlideUtil.load(context, product!!.main_image, iv_sku_logo)

            val sellingPrice = product!!.price
            val originalCost = product!!.original_cost

            BusinessUtils.setNowPriceAndOldPrice(sellingPrice, originalCost, tv_sku_selling_price, original_price_tv)

            tv_sku_quantity.text = StringUtils.getString(R.string.stock_format, product!!.stock)

//            confirm_tv.isEnabled = false
            tv_sku_info.text = "请选择：" + skuListData!![0].attributes[0].key
        }


    }


    public fun showLastSku(lastSku: Sku?) {
        if (product == null || lastSku == null) {
            return
        }
        if (lastSku.stockQuantity > 0) {
            selectedSku = lastSku
            // 选中第一个sku
            scroll_sku_list.selectedSku = selectedSku
            GlideUtil.load(context, selectedSku?.mainImage, iv_sku_logo)

            val sellingPrice = selectedSku!!.sellingPrice
            val originPrice = selectedSku!!.originPrice

            BusinessUtils.setNowPriceAndOldPrice(sellingPrice, originPrice, tv_sku_selling_price, original_price_tv)


            tv_sku_quantity.text = StringUtils.getString(R.string.stock_format, selectedSku!!.stockQuantity)

//            confirm_tv.isEnabled = selectedSku!!.stockQuantity > 0
            val attributeList = selectedSku!!.attributes
            val builder = StringBuilder()
            for (i in attributeList.indices) {
                if (i != 0) {
                    builder.append("　")
                }
                val attribute = attributeList[i]
                builder.append("\"" + attribute.value + "\"")
            }
            tv_sku_info.text = "已选：$builder"

        }


    }


    private fun updateSkuData2() {
        if (product == null) {
            return
        }

        scroll_sku_list.setSkuList(product!!.skus)
        if (product!!.skus.isEmpty()) {
            return
        }
        GlideUtil.load(context, product!!.main_image, iv_sku_logo)

        val sellingPrice = product!!.price
        val originalCost = product!!.original_cost

        BusinessUtils.setNowPriceAndOldPrice(sellingPrice, originalCost, tv_sku_selling_price, original_price_tv)

        tv_sku_quantity.text = StringUtils.getString(R.string.stock_format, product!!.stock)

//        confirm_tv.isEnabled = false
        tv_sku_info.text = "请选择：" + skuListData!![0].attributes[0].key


    }


    public fun updateQuantityOperator(newQuantity: Int) {
        if (selectedSku == null) {
            number_cut_iv.isEnabled = false
            number_add_iv.isEnabled = false
            number_value_et.isEnabled = false

            number_cut_iv.setImageResource(R.mipmap.ic_cut_blue_transparent)
            number_add_iv.setImageResource(R.mipmap.ic_add_blue_transparent)


        } else {
            when {
                newQuantity <= 1 -> {
                    number_cut_iv.isEnabled = false
                    number_add_iv.isEnabled = true

                    number_cut_iv.setImageResource(R.mipmap.ic_cut_blue_transparent)
                    number_add_iv.setImageResource(R.mipmap.ic_add_blue)
                }
                newQuantity >= selectedSku!!.stockQuantity -> {
                    number_cut_iv.isEnabled = true
                    number_add_iv.isEnabled = false

                    number_cut_iv.setImageResource(R.mipmap.ic_cut_blue)
                    number_add_iv.setImageResource(R.mipmap.ic_add_blue_transparent)
                }
                else -> {
                    number_cut_iv.isEnabled = true
                    number_add_iv.isEnabled = true

                    number_cut_iv.setImageResource(R.mipmap.ic_cut_blue)
                    number_add_iv.setImageResource(R.mipmap.ic_add_blue)
                }
            }
            number_value_et.isEnabled = true
        }
    }


    interface Callback {
        fun onAdded(sku: Sku?, quantity: Int)
    }

}
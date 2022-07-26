package com.jcs.where.features.mall.sku.other

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.features.mall.sku.bean.Sku
import com.jcs.where.features.mall.sku.bean.SkuAttribute
import com.jcs.where.features.mall.sku.view.OnSkuListener
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.dialog_product_sku.*

/**
 * Created by Wangsw  2022/7/26 15:46.
 *
 */
class SkuFragment : BaseBottomSheetDialogFragment<SkuPresenter>(), SkuView {


    private var product: Product? = null
    private var skuList: List<Sku>? = null
    var callback: Callback? = null
    private var selectedSku: Sku? = null
    private var priceFormat: String? = null
    private var stockQuantityFormat: String? = null

    override fun getLayoutId() = R.layout.dialog_product_sku


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 8 / 10)

    override fun initView(parent: View) {
        priceFormat = StringUtils.getString(R.string.comm_price_format)
        stockQuantityFormat = StringUtils.getString(R.string.product_detail_sku_stock)
    }

    override fun initData() {
        presenter = SkuPresenter(this)
        updateSkuData()
//        updateQuantityOperator(1)
    }

    override fun bindListener() {
        ib_sku_close.setOnClickListener {
            dismiss()
        }
        btn_sku_quantity_minus.setOnClickListener {
            val quantity = et_sku_quantity_input.getText().toString()
            if (TextUtils.isEmpty(quantity)) {
                return@setOnClickListener
            }
            val quantityInt = quantity.toInt()
            if (quantityInt > 1) {
                val newQuantity = (quantityInt - 1).toString()
                et_sku_quantity_input.setText(newQuantity)
                et_sku_quantity_input.setSelection(newQuantity.length)
                updateQuantityOperator(quantityInt - 1)
            }
        }

        btn_sku_quantity_plus.setOnClickListener {
            val quantity = et_sku_quantity_input.getText().toString()
            if (TextUtils.isEmpty(quantity) || selectedSku == null) {
                return@setOnClickListener
            }
            val quantityInt = quantity.toInt()
            if (quantityInt < selectedSku!!.stockQuantity) {
                val newQuantity = (quantityInt + 1).toString()
                et_sku_quantity_input.setText(newQuantity)
                et_sku_quantity_input.setSelection(newQuantity.length)
                updateQuantityOperator(quantityInt + 1)
            }
        }

        et_sku_quantity_input.setOnEditorActionListener { v, actionId, event ->
            if (actionId != EditorInfo.IME_ACTION_DONE || selectedSku == null) {
                return@setOnEditorActionListener false
            }
            val quantity = et_sku_quantity_input.getText().toString()
            if (TextUtils.isEmpty(quantity)) {
                return@setOnEditorActionListener false
            }
            val quantityInt = quantity.toInt()
            if (quantityInt <= 1) {
                et_sku_quantity_input.setText("1")
                et_sku_quantity_input.setSelection(1)
                updateQuantityOperator(1)
            } else if (quantityInt >= selectedSku!!.stockQuantity) {
                val newQuantity = selectedSku!!.stockQuantity.toString()
                et_sku_quantity_input.setText(newQuantity)
                et_sku_quantity_input.setSelection(newQuantity.length)
                updateQuantityOperator(selectedSku!!.stockQuantity)
            } else {
                et_sku_quantity_input.setSelection(quantity.length)
                updateQuantityOperator(quantityInt)
            }
            false

        }

        scroll_sku_list.setListener(object : OnSkuListener {
            override fun onUnselected(unselectedAttribute: SkuAttribute?) {
                selectedSku = null
                GlideUtil.load(context, product!!.main_image, iv_sku_logo)
                tv_sku_quantity.setText(String.format(stockQuantityFormat!!, product!!.stock))
                val firstUnselectedAttributeName = scroll_sku_list.getFirstUnelectedAttributeName()
                tv_sku_info.setText("请选择：$firstUnselectedAttributeName")
                btn_submit.setEnabled(false)
                val quantity = et_sku_quantity_input.getText().toString()
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
                tv_sku_info.setText("已选：$builder")
                tv_sku_quantity.setText(String.format(stockQuantityFormat!!, selectedSku!!.stockQuantity))
                btn_submit.setEnabled(true)
                val quantity = et_sku_quantity_input.getText().toString()
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity))
                } else {
                    updateQuantityOperator(0)
                }
            }

        })

        btn_submit.setOnClickListener {
            val quantity = et_sku_quantity_input.getText().toString()
            if (TextUtils.isEmpty(quantity)) {
                return@setOnClickListener
            }
            val quantityInt = quantity.toInt()
            if (quantityInt > 0 && quantityInt <= selectedSku!!.stockQuantity) {
                callback!!.onAdded(selectedSku, quantityInt)
                dismiss()
            } else {
                Toast.makeText(getContext(), "商品数量超出库存，请修改数量", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun setData(product: Product) {
        this.product = product
        skuList = product.skus
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
//            tv_sku_selling_price.text = String.format(priceFormat!!, NumberUtils.formatNumber((selectedSku!!.sellingPrice / 100).toDouble()))
            tv_sku_selling_price.text = String.format(priceFormat!!, NumberUtils.formatNumber((selectedSku!!.sellingPrice ).toDouble()))



            tv_sku_selling_price_unit.text = "/" + product!!.measurementUnit
            tv_sku_quantity.text = String.format(stockQuantityFormat!!, selectedSku!!.stockQuantity)
            btn_submit.isEnabled = selectedSku!!.stockQuantity > 0
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
            GlideUtil.load(context, selectedSku!!.mainImage, iv_sku_logo)
//            tv_sku_selling_price.text = String.format(priceFormat!!, NumberUtils.formatNumber((product!!.price / 100).toDouble()))
            tv_sku_selling_price.text = String.format(priceFormat!!, NumberUtils.formatNumber((product!!.price ).toDouble()))


            tv_sku_selling_price_unit.text = "/" + product!!.measurementUnit
            tv_sku_quantity.text = String.format(stockQuantityFormat!!, product!!.stock)
            btn_submit.isEnabled = false
            tv_sku_info.text = "请选择：" + skuList!![0].attributes[0].key
        }



    }


    public fun updateQuantityOperator(newQuantity: Int) {
        if (selectedSku == null) {
            btn_sku_quantity_minus.isEnabled = false
            btn_sku_quantity_plus.isEnabled = false
            et_sku_quantity_input.isEnabled = false
        } else {
            if (newQuantity <= 1) {
                btn_sku_quantity_minus.isEnabled = false
                btn_sku_quantity_plus.isEnabled = true
            } else if (newQuantity >= selectedSku!!.stockQuantity) {
                btn_sku_quantity_minus.isEnabled = true
                btn_sku_quantity_plus.isEnabled = false
            } else {
                btn_sku_quantity_minus.isEnabled = true
                btn_sku_quantity_plus.isEnabled = true
            }
            et_sku_quantity_input.isEnabled = true
        }
    }


    interface Callback {
        fun onAdded(sku: Sku?, quantity: Int)
    }

}
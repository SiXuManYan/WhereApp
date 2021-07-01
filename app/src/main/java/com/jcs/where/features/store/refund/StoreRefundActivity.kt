package com.jcs.where.features.store.refund

import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.order.store.StoreOrderShopGoods
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.order.detail.StoreOrderDetailAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_store_refund.*

/**
 * Created by Wangsw  2021/7/1 10:27.
 * 商城退款退货
 */
class StoreRefundActivity : BaseMvpActivity<StoreRefundPresenter>(), StoreRefundView {


    private var orderId = 0
    private var totalPrice = 0.0

    private lateinit var mAdapter: StoreOrderDetailAdapter
    private lateinit var mImageAdapter: StoreRefundAdapter


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_refund

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        mAdapter = StoreOrderDetailAdapter()
        intent.extras?.let {
            val goodData = it.getParcelableArrayList<StoreOrderShopGoods>(Constant.PARAM_DATA)
            mAdapter.setNewInstance(goodData)
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
            totalPrice = it.getDouble(Constant.PARAM_TOTAL_PRICE)
        }


        good_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            adapter = mAdapter
        }
        amount_tv.text = getString(R.string.price_unit_format, totalPrice.toString())

        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            addChildClickViewIds(R.id.delete_iv)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.delete_iv -> {
                        mImageAdapter.removeAt(position)
                    }
                    else -> {
                    }
                }


            }

        }
        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }


    }

    override fun initData() {
        presenter = StoreRefundPresenter(this)
    }

    override fun bindListener() {

        upload_iv.setOnClickListener {

            val size = mImageAdapter.data.size
            if (size == 5) {
                ToastUtils.showShort(R.string.refund_image_hint)
                return@setOnClickListener
            }
            val max = 5 - size
            FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)

        }
        refund_tv.setOnClickListener {
            val desc = desc_tv.text.toString().trim()
            if (desc.isEmpty()) {
                ToastUtils.showShort(R.string.refund_reason_input_hint)
                return@setOnClickListener
            }

            if (mImageAdapter.data.isNotEmpty()) {
                presenter.upLoadImage(ArrayList(mImageAdapter.data),orderId , desc)
            }else{
                presenter.storeRefund(orderId , desc)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val elements = Matisse.obtainPathResult(data)
        elements.forEach {
            mImageAdapter.addData(it)
        }
    }

    override fun applicationSuccess() {
        ToastUtils.showShort(R.string.application_success)
    }

}
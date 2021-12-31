package com.jcs.where.features.mall.refund.apply

import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallOrderGood
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.order.MallOrderDetailAdapter
import com.jcs.where.features.store.refund.MallRefundPresenter
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.features.store.refund.StoreRefundView
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.widget.list.DividerDecoration
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_store_refund.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/12/17 19:19.
 * 商城申请售后
 */
class MallRefundActivity : BaseMvpActivity<MallRefundPresenter>(), StoreRefundView {


    private var orderId = 0
    private var totalPrice = 0.0

    /**
     * 是否是修改申请内容
     */
    private var isChange = false

    /**
     * 申请售后原因
     */
    private var cancelReason = ""

    private lateinit var mAdapter: MallOrderDetailAdapter
    private lateinit var mImageAdapter: StoreRefundAdapter


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_refund

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        mAdapter = MallOrderDetailAdapter()
        intent.extras?.let {
            val goodData = it.getSerializable(Constant.PARAM_DATA) as ArrayList<MallOrderGood>
            mAdapter.setNewInstance(goodData)
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
            totalPrice = it.getDouble(Constant.PARAM_TOTAL_PRICE)
            isChange = it.getBoolean(Constant.PARAM_BOOLEAN, false)
            cancelReason = it.getString(Constant.PARAM_DESCRIPTION, "")
        }


        // 商品
        good_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            adapter = mAdapter
            addItemDecoration(DividerDecoration(getColor(R.color.white), SizeUtils.dp2px(10f),
                0, 0))
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

        handleFromChange()
    }

    /**
     * 处理修改申请
     */
    private fun handleFromChange() {

        if (cancelReason.isNotEmpty()) {
            desc_tv.setText(cancelReason)
        }
        if (isChange) {
            intent.extras?.let {
                val selectImage = it.getStringArrayList(Constant.PARAM_SELECT_IMAGE)
                if (!selectImage.isNullOrEmpty()) {
                    mImageAdapter.setNewInstance(selectImage)
                }

            }

        }


    }

    override fun initData() {
        presenter = MallRefundPresenter(this)
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

            if (isChange) {
                presenter.upLoadImage(ArrayList(mImageAdapter.data), orderId, desc)

            } else {
                if (mImageAdapter.data.isNotEmpty()) {
                    presenter.upLoadImage(ArrayList(mImageAdapter.data), orderId, desc)
                } else {
                    presenter.storeRefund(orderId, desc)
                }
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
        // 回到商城订单详情，刷新
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        finish()
    }

    override fun modifyApplicationSuccess() {
        ToastUtils.showShort(R.string.modify_success)
        // 回到商城订单详情，刷新
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        finish()
    }

    override fun upLoadImageSuccess(link: ArrayList<String>, orderId: Int, desc: String) {
        if (isChange) {
            val allAlreadyUploadImage = presenter.getAllAlreadyUploadImage(mImageAdapter)
            allAlreadyUploadImage.addAll(link)

            val descImages = Gson().toJson(allAlreadyUploadImage)
            presenter.modifyRefundAgain(orderId, desc, descImages)
        } else {
            val descImages = Gson().toJson(link)
            presenter.storeRefund(orderId, desc, descImages)
        }
    }

}
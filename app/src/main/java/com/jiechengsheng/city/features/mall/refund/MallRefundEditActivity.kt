package com.jiechengsheng.city.features.mall.refund

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.gson.Gson
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.response.mall.MallOrderGood
import com.jiechengsheng.city.api.response.mall.MallRefundInfo
import com.jiechengsheng.city.api.response.mall.RefundMethod
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.mall.order.MallOrderDetailAdapter
import com.jiechengsheng.city.features.refund.method.RefundMethodActivity
import com.jiechengsheng.city.features.store.refund.image.RefundImage
import com.jiechengsheng.city.features.store.refund.image.StoreRefundAdapter2
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.FeaturesUtil
import com.jiechengsheng.city.widget.list.DividerDecoration
import com.zhihu.matisse.Matisse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mall_refund.*
import org.greenrobot.eventbus.EventBus
import top.zibin.luban.Luban


/**
 * Created by Wangsw  2022/3/24 15:45.
 * 售后表单
 */
class MallRefundEditActivity : BaseMvpActivity<MallRefundEditPresenter>(), MallRefundEditView, OnItemChildClickListener {

    /** 商品订单id */
    private var orderId = 0

    /** 售后ID */
    private var refundId = 0

    /** 是否是修改申请内容 */
    private var isChange = false

    /** 用于请求退款的订单id */
    private var handleRefundOrderId = 0

    /** 打款id */
    private var remitId = 0


    private lateinit var mAdapter: MallOrderDetailAdapter
    private lateinit var mImageAdapter: StoreRefundAdapter2

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_refund

    companion object {

        fun navigation(context: Context, orderId: Int, refundId: Int, isChange: Boolean) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, orderId)
                putInt(Constant.PARAM_REFUND_ID, refundId)
                putBoolean(Constant.PARAM_BOOLEAN, isChange)
            }
            val intent = Intent(context, MallRefundEditActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bundle = it.data?.extras ?: return@registerForActivityResult
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                val refundMethod = bundle.getParcelable<RefundMethod>(Constant.PARAM_REFUND_METHOD)!!
                loadRefundMethod(refundMethod)
            }
        }

    }



    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
        initContent()
    }


    private fun initExtra() {
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
            refundId = it.getInt(Constant.PARAM_REFUND_ID, 0)
            isChange = it.getBoolean(Constant.PARAM_BOOLEAN, false)
        }
    }

    private fun initContent() {

        // 商品
        mAdapter = MallOrderDetailAdapter()
        good_rv.apply {
            adapter = mAdapter
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            addItemDecoration(DividerDecoration(getColor(R.color.white), SizeUtils.dp2px(10f), 0, 0))
        }

        // 相册
        mImageAdapter = StoreRefundAdapter2().apply {
            addChildClickViewIds(R.id.delete_iv, R.id.image_iv, R.id.image_add_iv)
            setOnItemChildClickListener(this@MallRefundEditActivity)
        }

        image_rv.apply {
            isNestedScrollingEnabled = true
            adapter = mImageAdapter
            layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        }

        val addItem = RefundImage().apply {
            type = RefundImage.TYPE_ADD
        }
        mImageAdapter.addData(addItem)

        // 判断表单类型
        if (isChange) {
            refund_tv.setText(R.string.modify_application)
        } else {
            refund_tv.setText(R.string.submit_application)
        }


    }


    override fun initData() {
        presenter = MallRefundEditPresenter(this)
        presenter.getData(orderId, refundId)
    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.image_add_iv -> {
                // 添加图片
                val size = mImageAdapter.data.size
                if (size == 7) {
                    ToastUtils.showShort(R.string.refund_image_hint_6)
                    return
                }
                val max = 7 - size
                FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)
            }
            R.id.image_iv -> {
                // 展示图片
            }
            R.id.delete_iv -> {
                mImageAdapter.removeAt(position)
            }
            else -> {
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 添加图片
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        if (requestCode == Constant.REQUEST_MEDIA) {
            val elements = Matisse.obtainPathResult(data)

            Flowable.just(elements)
                .observeOn(Schedulers.io())
                .map {
                    Luban.with(this@MallRefundEditActivity).load(elements).ignoreBy(100).get()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {

                    elements.forEach {
                        val apply = RefundImage().apply {
                            type = RefundImage.TYPE_EDIT
                            imageSource = it
                        }
                        mImageAdapter.addData(apply)
                    }

                }
                .subscribe {
                    it.forEach { file ->
                        val apply = RefundImage().apply {
                            type = RefundImage.TYPE_EDIT
                            imageSource = file.absolutePath
                        }
                        mImageAdapter.addData(apply)
                    }
                }
        }

    }

    override fun bindDetail(response: MallRefundInfo) {
        val goods = ArrayList<MallOrderGood>()
        goods.add(response.goods)
        mAdapter.setNewInstance(goods)

        response.money_info.let {
            amount_tv.text = getString(R.string.price_unit_format, it.refund_money)
            good_price_tv.text = getString(R.string.price_unit_format, it.goods_total)
            shop_offers_tv.text = getString(R.string.price_unit_format, it.shop_coupon_money)
            platform_offers_tv.text = getString(R.string.price_unit_format, it.order_coupon_money)
        }

        desc_et.setText(response.cancel_reason)

        response.cancel_images.forEach {
            val apply = RefundImage().apply {
                type = RefundImage.TYPE_EDIT
                imageSource = it
            }
            mImageAdapter.addData(apply)
        }

        handleRefundOrderId = response.order_id

        response.remit_info?.let {
            loadRefundMethod(it)
        }

    }

    private fun loadRefundMethod(refundMethod: RefundMethod) {
        remitId = refundMethod.id

        refund_name_tv.text = refundMethod.name
        refund_user_name_tv.text = refundMethod.user_name
        refund_account_tv.text = refundMethod.account
        refund_method_ll.visibility = View.VISIBLE
    }

    override fun bindListener() {

        refund_tv.setOnClickListener {
            if (remitId == 0) {
                ToastUtils.showShort(R.string.please_add_refund_methods)
                return@setOnClickListener
            }
            val desc = desc_et.text.toString().trim()
            if (desc.isEmpty()) {
                ToastUtils.showShort(R.string.refund_reason_input_hint)
                return@setOnClickListener
            }
            showLoadingDialog(false)
            if (mImageAdapter.data.size > 1) {
                presenter.upLoadImage(mImageAdapter, handleRefundOrderId, remitId, desc)
            } else {
                presenter.doRefund(handleRefundOrderId, remitId, desc)
            }
        }

        refund_method_tv.setOnClickListener {
            launcher.launch(Intent(this, RefundMethodActivity::class.java)
                .putExtra(Constant.PARAM_HANDLE_SELECT, true))
        }

    }

    override fun applicationSuccess() {
        dismissLoadingDialog()
        ToastUtils.showShort(R.string.application_success)
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }

    override fun upLoadImageSuccess(link: ArrayList<String>, orderId: Int, remitId: Int, desc: String) {
        if (isChange) {
            val allAlreadyUploadImage = presenter.getAllAlreadyUploadImage(mImageAdapter)
            if (link.isNotEmpty()) {
                allAlreadyUploadImage.addAll(link)
            }
            val descImages = Gson().toJson(allAlreadyUploadImage)
            presenter.doRefund(orderId, remitId, desc, descImages)
        } else {
            val descImages = Gson().toJson(link)
            presenter.doRefund(orderId, remitId, desc, descImages)
        }
    }

    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        dismissLoadingDialog()
    }


}


package com.jiechengsheng.city.features.mall.refund.complaint

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.gson.Gson
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.request.hotel.ComplaintRequest
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.store.refund.image.RefundImage
import com.jiechengsheng.city.features.store.refund.image.StoreRefundAdapter2
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.FeaturesUtil
import com.zhihu.matisse.Matisse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_complaint.*
import top.zibin.luban.Luban

/**
 * Created by Wangsw  2022/3/29 14:45.
 * 申诉
 */
class ComplaintActivity : BaseMvpActivity<ComplaintPresenter>(), ComplaintView, OnItemChildClickListener {


    /** 商品订单id */
    private var orderId = 0

    private var type = 0

    private lateinit var mImageAdapter: StoreRefundAdapter2


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_complaint

    override fun initView() {
        initExtra()
        initContent()
    }


    private fun initExtra() {

        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
            type = it.getInt(Constant.PARAM_TYPE, ComplaintRequest. TYPE_MALL)
        }

    }


    private fun initContent() {
        // 相册
        mImageAdapter = StoreRefundAdapter2().apply {
            addChildClickViewIds(R.id.delete_iv, R.id.image_iv, R.id.image_add_iv)
            setOnItemChildClickListener(this@ComplaintActivity)
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

    }


    override fun initData() {
        presenter = ComplaintPresenter(this)
        presenter.complaintType = type
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
        val elements = Matisse.obtainPathResult(data)

        Flowable.just(elements)
            .observeOn(Schedulers.io())
            .map {
                Luban.with(this@ComplaintActivity).load(elements).ignoreBy(100).get()
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


    override fun bindListener() {
        commit_tv.setOnClickListener {
            val desc = desc_et.text.toString().trim()
            if (desc.isEmpty()) {
                ToastUtils.showShort(R.string.refund_reason_input_hint)
                return@setOnClickListener
            }
            showLoadingDialog(false)
            if (mImageAdapter.data.size > 1) {
                presenter.upLoadImage(mImageAdapter, orderId, desc)
            } else {
                presenter.doComplaint(orderId, desc)
            }
        }

    }

    override fun upLoadImageSuccess(link: ArrayList<String>, orderId: Int, desc: String) {
        val descImages = Gson().toJson(link)
        presenter.doComplaint(orderId, desc, descImages)
    }

    override fun applicationSuccess() {
        dismissLoadingDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        dismissLoadingDialog()
    }


}
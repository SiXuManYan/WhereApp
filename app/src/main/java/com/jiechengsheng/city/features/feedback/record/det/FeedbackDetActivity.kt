package com.jiechengsheng.city.features.feedback.record.det

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.feedback.form.FeedBackPostPresenter
import com.jiechengsheng.city.features.feedback.form.FeedBackPostView
import com.jiechengsheng.city.features.store.refund.StoreRefundAdapter
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_feedback_detail.*

/**
 * Created by Wangsw  2022/10/14 13:47.
 * 意见详情
 */
class FeedbackDetActivity : BaseMvpActivity<FeedBackPostPresenter>(), FeedBackPostView {

    var image = ArrayList<String>()
    var content = ""
    var phone = ""
    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_feedback_detail

    companion object {
        fun navigation(context: Context, image: ArrayList<String>? = ArrayList(), content: String? = "", phone: String? = "") {

            val bundle = Bundle().apply {
                putStringArrayList(Constant.PARAM_IMAGE, image)
                putString(Constant.PARAM_CONTENT, content)
                putString(Constant.PARAM_PHONE, phone)
            }
            val intent = Intent(context, FeedbackDetActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun initView() {
        initExtra()
        initImage()
    }


    private fun initExtra() {
        intent.extras?.let {
            val images: ArrayList<String>? = it.getStringArrayList(Constant.PARAM_IMAGE)
            if (!images.isNullOrEmpty()) {
                image.addAll(images)
            }
            content = it.getString(Constant.PARAM_CONTENT, "")
            phone = it.getString(Constant.PARAM_PHONE, "")
        }

    }

    private fun initImage() {
        mImageAdapter = StoreRefundAdapter().apply {
            hideDelete = true
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun initData() {
        presenter = FeedBackPostPresenter(this)

        content_tv.text = content
        phone_tv.text = phone
        mImageAdapter.setNewInstance(image)

    }

    override fun bindListener() {

    }


}
package com.jcs.where.features.job.form.certificate

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.job.CreateCertificate
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_job_cv_certificate.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2023/2/6 11:00.
 * 资格证书
 */
class CertificateFromActivity : BaseMvpActivity<CertificatePresenter>(), CertificateView {


    /**
     * 工作经历id， 不为0时为修改
     */
    private var draftId = 0
    private var draftData: JobExperience? = null


    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_certificate

    companion object {
        fun navigation(context: Context, item: JobExperience) {

            val bundle = Bundle().apply {
                putParcelable(Constant.PARAM_DATA, item)
            }
            val intent = Intent(context, CertificateFromActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun initView() {
        draftData = intent.getParcelableExtra(Constant.PARAM_DATA)
        initImage()
        initDraft()
    }

    private fun initDraft() {
        draftData?.let {
            draftId = it.id
            name_et.setText(it.title)
            mImageAdapter.setNewInstance(it.images)
            handleImageSize()
            if (draftId > 0) {
                delete_tv.visibility = View.VISIBLE
            }
        }
    }


    private fun initImage() {

        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            addChildClickViewIds(R.id.delete_iv)
            setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.delete_iv) {
                    mImageAdapter.removeAt(position)
                    handleImageSize()
                }
            }
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

    }


    override fun initData() {
        presenter = CertificatePresenter(this)

    }

    override fun bindListener() {

        select_iv.setOnClickListener {
            val size = mImageAdapter.data.size
            if (size == 1) {
                ToastUtils.showShort(R.string.refund_image_max_6)
                return@setOnClickListener
            }
            val max = 1 - size
            FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)
        }

        delete_tv.setOnClickListener {
            AlertDialog.Builder(this, R.style.JobAlertDialogTheme)
                .setCancelable(false)
                .setTitle(R.string.hint)
                .setMessage(R.string.delete_hint)
                .setPositiveButton(R.string.confirm) { dialog: DialogInterface, which: Int ->
                    presenter.deleteCertificate(draftId)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show()
        }

        save_tv.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {

            override fun onDebouncingClick(v: View?) {

                save_tv.isClickable = false

                val name = name_et.text.toString().trim()
                if (name.isBlank()) {
                    ToastUtils.showShort(R.string.please_enter)
                    return
                }

                if (mImageAdapter.data.isNullOrEmpty()) {
                    ToastUtils.showShort("Please upload image")
                    return
                }

                val imageUrl = mImageAdapter.data[0]

                if (!RegexUtils.isURL(imageUrl)) {
                    presenter.upLoadImage(draftId, name, ArrayList(mImageAdapter.data))
                } else {

                    val descImages = Gson().toJson(mImageAdapter.data)
                    val apply = CreateCertificate().apply {
                        title = name
                        images = descImages
                    }

                    presenter.handleCertificate(draftId, apply)
                }





                Handler(Looper.getMainLooper()).postDelayed({
                    save_tv.isClickable = true
                }, 500)
            }


        })
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

        handleImageSize()
    }

    private fun handleImageSize() {
        if (mImageAdapter.data.isNullOrEmpty()) {
            select_iv.visibility = View.VISIBLE
        } else {
            select_iv.visibility = View.GONE
        }
    }


    override fun handleSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_CV_CERTIFICATE))
        finish()
    }

    override fun deleteSuccess() {
        EventBus.getDefault().post(BaseEvent(EventCode.EVENT_DELETE_CV_CERTIFICATE, draftId))
        finish()
    }


}
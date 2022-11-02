package com.jcs.where.features.job.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.job.CompanyInfo
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.detail.media.DetailMediaAdapter
import com.jcs.where.features.hotel.detail.media.MediaData
import com.jcs.where.features.job.company.info.CompanyInfoActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_company.*

/**
 * Created by Wangsw  2022/11/1 16:16.
 * 公司详情
 */
class CompanyActivity : BaseMvpActivity<CompanyPresenter>(), CompanyView {


    private var jobId = 0
    var introduce: String = ""
    private lateinit var mAdapter: CompanyPhotoAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_company

    companion object {
        fun navigation(context: Context, jobId: Int) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, jobId)
            }
            val intent = Intent(context, CompanyActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        BarUtils.setStatusBarColor(this,Color.WHITE)
        jobId = intent.getIntExtra(Constant.PARAM_ID, 0)
        initPhoto()
    }

    private fun initPhoto() {


        mAdapter = CompanyPhotoAdapter()
        photo_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@CompanyActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(16f)))
        }

    }

    override fun initData() {
        presenter = CompanyPresenter(this)
        presenter.getCompanyDetail(jobId)
    }

    override fun bindListener() {
        more_introduce_tv.setOnClickListener {
            startActivity(CompanyInfoActivity::class.java ,Bundle().apply {
                putString(Constant.PARAM_DATA , introduce)
            })
        }
    }

    override fun bindCompanyDetail(response: CompanyInfo, media: ArrayList<CompanyPhoto>) {

        GlideUtil.load(this, response.logo, logo_iv, 24 , GlideRoundedCornersTransform.CornerType.ALL ,R.mipmap.ic_company_default_logo )
        company_name_tv.text = response.company_title
        company_type_tv.text = response.company_type
        company_size_tv.text = response.company_size
        introduce = response.profile
        company_introduce_tv.text = introduce
        address_tv.text = response.address

        mAdapter.setNewInstance(media)


    }


}
package com.jiechengsheng.city.features.job.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.CompanyInfo
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.job.company.album.CompanyAlbumActivity
import com.jiechengsheng.city.features.job.company.info.CompanyInfoActivity
import com.jiechengsheng.city.features.job.open.JobOpeningsActivity
import com.jiechengsheng.city.features.media.MediaDetailActivity
import com.jiechengsheng.city.features.web.WebViewActivity
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.utils.image.GlideRoundedCornersTransform
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_company.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/11/1 16:16.
 * 公司详情
 */
class CompanyActivity : BaseMvpActivity<CompanyPresenter>(), CompanyView {

    private var isCollect = false
    private var companyId = 0
    private var introduce: String? = ""
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
        BarUtils.setStatusBarColor(this, Color.WHITE)
        companyId = intent.getIntExtra(Constant.PARAM_ID, 0)
        initPhoto()
    }

    private fun initPhoto() {
        mAdapter = CompanyPhotoAdapter().apply {
            setOnItemClickListener { _, _, position ->
                when (mAdapter.getItemViewType(position)) {
                    CompanyPhoto.HORIZONTAL_IMAGE -> {
                        val photos = ArrayList<String>()
                        mAdapter.data.forEach {
                            if (it.itemType == CompanyPhoto.HORIZONTAL_IMAGE) {
                                photos.add(it.src)
                            }
                        }
                        MediaDetailActivity.navigationOnlyStringImage(this@CompanyActivity, position, photos, true)
                    }
                    CompanyPhoto.HORIZONTAL_IMAGE_LOOK_MORE -> {
                        viewMore()
                    }
                }

            }
        }
        photo_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@CompanyActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(16f)))
        }

    }

    override fun initData() {
        presenter = CompanyPresenter(this)
        presenter.getCompanyDetail(companyId)
    }

    override fun bindListener() {
        more_introduce_tv.setOnClickListener {
            startActivity(CompanyInfoActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_DATA, introduce)
            })
        }

        more_photo_tv.setOnClickListener {
            viewMore()
        }

        collect_iv.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            presenter.handleCollection(isCollect, companyId)
        }

        job_openings_tv.setOnClickListener {
            startActivity(JobOpeningsActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, companyId)
            })
        }

    }

    private fun viewMore() {
        startActivity(CompanyAlbumActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, companyId)
        })
    }

    override fun bindCompanyDetail(response: CompanyInfo, media: ArrayList<CompanyPhoto>) {
        companyId = response.id
        GlideUtil.load(this, response.logo, logo_iv, 24, GlideRoundedCornersTransform.CornerType.ALL, R.mipmap.ic_company_default_logo)
        company_name_tv.text = response.company_title
        company_type_tv.text = response.company_type
        company_size_tv.text = getString(R.string.company_size_format, response.company_size)
        address_tv.text = response.address


        // 公司简介
        introduce = response.profile

        if (introduce.isNullOrBlank()) {
            introduce_rl.visibility = View.GONE
            company_introduce_tv.visibility = View.GONE
            introduce_line.visibility = View.GONE
        } else {
            company_introduce_tv.text = introduce
            introduce_rl.visibility = View.VISIBLE
            company_introduce_tv.visibility = View.VISIBLE
            introduce_line.visibility = View.VISIBLE
        }

        // 公司图片
        if (response.images.isNullOrEmpty()) {
            photo_rl.visibility = View.GONE
            photo_rv.visibility = View.GONE
            photos_line.visibility = View.GONE
        } else {
            photo_rl.visibility = View.VISIBLE
            photo_rv.visibility = View.VISIBLE
            photos_line.visibility = View.VISIBLE
            mAdapter.setNewInstance(media)
        }

        // 媒体
        val website = response.website
        if (!website.isNullOrBlank()) {
            media_ll.visibility = View.VISIBLE

            SpanUtils.with(media_tv).append(website).setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) = WebViewActivity.navigation(this@CompanyActivity, website)
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            }).create()
        } else {
            media_ll.visibility = View.GONE
        }

        isCollect = response.is_collect
        setLikeImage()

        job_openings_tv.text = getString(R.string.job_openings_format, response.job_count)

    }

    private fun setLikeImage() {

        collect_iv.setImageResource(
            if (isCollect) {

                R.mipmap.ic_like_red_night
            } else {
                R.mipmap.ic_like_normal_night
            }
        )
    }

    override fun collectionResult(result: Boolean) {
        isCollect = result
        setLikeImage()
        if (result) {
            ToastUtils.showShort(R.string.collection_success)
        } else {
            ToastUtils.showShort(R.string.cancel_collection_success)
        }
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_COLLECTION))
    }


}
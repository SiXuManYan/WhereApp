package com.jcs.where.features.mechanism

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.response.MechanismDetailResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.frams.common.Html5Url
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.MobUtil
import com.jcs.where.widget.JcsBanner
import kotlinx.android.synthetic.main.activity_mechanism.*



/**
 * Created by Wangsw  2021/9/4 15:08.
 *  机构详情
 */
class MechanismActivity : BaseMvpActivity<MechanismPresenter>(), MechanismView {

    private var infoId = 0

    /** 是否收藏 */
    private var collect_status = 1


    private lateinit var mAdapter: ImageBannerAdapter

    private var isToolbarDark = false
    private var businessPhone = ""
    private var webUrl = ""
    private var facebook = ""
    private var mLat = 0.0
    private var mLng = 0.0

    override fun getLayoutId() = R.layout.activity_mechanism

    override fun isStatusDark() = isToolbarDark

    override fun initView() {
        infoId = intent.getIntExtra(Constant.PARAM_ID, 0)
        initScroll()
        initMedia()
    }

    private fun initMedia() {
        mAdapter = ImageBannerAdapter()
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(media_rv)

        media_rv.apply {
            layoutManager = LinearLayoutManager(this@MechanismActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        point_view.onPageSelected(firstItemPosition)
                    }
                }
            })


        }

    }


    override fun initData() {
        presenter = MechanismPresenter(this)
        presenter.getDetail(infoId)
    }

    override fun bindListener() {
        back_iv.setOnClickListener { finish() }
        phone_ll.setOnClickListener {
            val data = Uri.parse("tel:$businessPhone")
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = data
            startActivity(intent)
        }
        nav_ll.setOnClickListener {
            FeaturesUtil.startNaviGoogle(this, mLat.toFloat(), mLng.toFloat())
        }
        web_tv.setOnClickListener {
            WebViewActivity.goTo(this, webUrl)
        }
        facebook_tv.setOnClickListener {
            WebViewActivity.goTo(this, facebook)
        }
        shareIv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_GENERAL, infoId)
            MobUtil.shareFacebookWebPage(url, this@MechanismActivity)
        }
        like_iv.setOnClickListener {
            if (collect_status == 1) {
                presenter.collection(infoId)
            } else {
                presenter.unCollection(infoId)
            }
        }


    }

    private fun initScroll() {
        // alpha
        useView.setBackgroundColor(getColor(R.color.white))
        toolbar.setBackgroundColor(getColor(R.color.white))
        useView.background.alpha = 0
        toolbar.background.alpha = 0
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            val headHeight = media_fl.measuredHeight - toolbar.measuredHeight
            var alpha = (y.toFloat() / headHeight * 255).toInt()
            if (alpha >= 255) {
                alpha = 255
            }
            if (alpha <= 5) {
                alpha = 0
            }
            isToolbarDark = alpha > 130
            setLikeImage()
            setBackImage()
            setShareImage()

            useView.background.alpha = alpha
            toolbar.background.alpha = alpha

            changeStatusTextColor()
        }

    }


    private fun setLikeImage() {

        like_iv.setImageResource(
            if (collect_status == 2) {
                if (isToolbarDark) {
                    R.mipmap.ic_like_red_night
                } else {
                    R.mipmap.ic_like_red_light
                }
            } else {
                if (isToolbarDark) {
                    R.mipmap.ic_like_norma_nightl
                } else {
                    R.mipmap.ic_like_normal_light
                }
            }
        )
    }

    private fun setBackImage() {
        back_iv.setImageResource(
            if (isToolbarDark) {
                R.mipmap.ic_back_black
            } else {
                R.mipmap.ic_back_light
            }
        )
    }

    private fun setShareImage() {
        shareIv.setImageResource(
            if (isToolbarDark) {
                R.mipmap.ic_share_night
            } else {
                R.mipmap.ic_share_light
            }
        )
    }

    override fun bindDetail(data: MechanismDetailResponse) {

        name_tv.text = data.title
        address_tv.text = data.address
        collect_status = data.collect_status
        businessPhone = data.tel
        webUrl = data.web_site
        facebook = data.facebook
        setLikeImage()

        // 营业时间
        val weeks = StringUtils.getStringArray(R.array.weeks)
        val startWeek: String = weeks[data.week_start - 1]
        val endWeek: String = weeks[data.week_end - 1]
        time_tv.text = getString(R.string.valid_period_format2, startWeek, endWeek)

        phone_tv.text = businessPhone
        mail_tv.text = data.email
        web_tv.text = data.web_site
        facebook_tv.text = data.facebook
        val content = data.abstractX
        content_tv.text = if (content.isBlank()) {
            getString(R.string.no_introduce)
        } else {
            content
        }
        val images = data.images
        if (images.isEmpty()) {
            images.add(JcsBanner.DEFAULT)
        }
        mLat = data.lat
        mLng = data.lng
        mAdapter.setNewInstance(data.images)

    }

    override fun collectionHandleSuccess(collectionStatus: Int) {
        collect_status = collectionStatus
        setLikeImage()
    }

}
package com.jcs.where.features.home.child.header

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.MyLayoutManager
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import com.jcs.where.widget.calendar.JcsCalendarDialog
import pl.droidsonroids.gif.GifImageView


/**
 * Created by Wangsw  2022/6/21 14:54.
 * 首页 子推荐Header
 */
class HomeChildHeader(val mActContext: FragmentActivity) : LinearLayout(mActContext) {

    init {
        initView()
    }


    var onChildCategoryClick: OnChildCategoryClick? = null

    lateinit var topBanner: XBanner

    lateinit var bannerContainer: LinearLayout
    lateinit var categoryRv: RecyclerView
    private lateinit var mAdapter: HomeChildHeaderAdapter

    private fun initView() {

        val view = LayoutInflater.from(context).inflate(R.layout.view_home_child_header, this, true)
        val bannerContainer = view.findViewById<LinearLayout>(R.id.ll_banner)
        val topBanner = view.findViewById<XBanner>(R.id.top_banner)
        val categoryRv = view.findViewById<RecyclerView>(R.id.home_category_rv)

        this.topBanner = topBanner
        this.bannerContainer = bannerContainer
        this.categoryRv = categoryRv


        val bannerParams = bannerContainer.layoutParams.apply {
            height = ScreenUtils.getScreenWidth() * 108 / 375
        }
        bannerContainer.layoutParams = bannerParams



        initBanner()
        initCategory()

    }


    /** 轮播图 */
    private fun initBanner() {
        topBanner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
            .setTitleHeight(50)
            .isAutoPlay(true)
            .setDelay(5000)
            .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
            .setUpIndicatorSize(4, 4)
            .setIndicatorGravity(XBanner.INDICATOR_CENTER)
            .setImageLoader(object : AbstractUrlLoader() {
                override fun loadImages(context: Context, url: String, image: ImageView) {
                    GlideUtil.load(context, url, image, 4)
                }

                override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) {
                    GlideUtil.load(context, url, gifImageView, 4)
                }
            })
    }


    /** 分类 */
    @SuppressLint("NotifyDataSetChanged")
    private fun initCategory() {
        mAdapter = HomeChildHeaderAdapter().apply {
            setOnItemClickListener { _, _, position ->
                // 筛选
                val datas = mAdapter.data
                datas.forEach {
                    it.nativeIsSelected = false
                }
                notifyDataSetChanged()

                val category = data[position]
                category.nativeIsSelected = true
                notifyItemChanged(position)
                onChildCategoryClick?.onChildCategoryClick(category)
            }
        }

        categoryRv.apply {
            adapter = mAdapter
            layoutManager = MyLayoutManager()
        }

    }

    fun bindTopBannerData(response: ArrayList<BannerResponse>) {

        val bannerUrls: java.util.ArrayList<String> = java.util.ArrayList()
        response.forEach {
            bannerUrls.add(it.src)
        }
        if (bannerUrls.isNotEmpty()) {
            bannerContainer.visibility = View.VISIBLE
        } else {
            bannerContainer.visibility = View.GONE
        }


        topBanner.setImageUrls(bannerUrls)
        topBanner.setBannerPageListener(object : XBanner.BannerPageListener {

            override fun onBannerDragging(item: Int) = Unit

            override fun onBannerIdle(item: Int) = Unit

            override fun onBannerClick(item: Int) {
                val data = response[item]


                if (data.redirect_type == 0) {
                    return
                }
                if (data.redirect_type == 1 && data.h5_link.isNotBlank()) {
                    WebViewActivity.goTo(context, data.h5_link)
                    return
                }

                if (data.redirect_type == 2) {

                    when (data.target_type) {
                        1 -> {
                            val dialog = JcsCalendarDialog()
                            dialog.initCalendar(mActContext)
                            HotelDetailActivity2.navigation(mActContext, data.target_id, dialog.startBean, dialog.endBean)
                        }
                        2 -> TravelDetailActivity.navigation(mActContext, data.target_id)
                        3 -> {
                            mActContext.startActivity(Intent(mActContext,
                                NewsDetailActivity::class.java).putExtras(Bundle().apply {
                                putString(Constant.PARAM_NEWS_ID, data.target_id.toString())
                            }))
                        }
                        4 -> {
                            mActContext.startActivity(Intent(mActContext,
                                MechanismActivity::class.java).putExtras(Bundle().apply {
                                putInt(Constant.PARAM_ID, data.target_id)
                            }))
                        }
                        5 -> {
                            mActContext.startActivity(Intent(mActContext, RestaurantDetailActivity::class.java).putExtras(
                                Bundle().apply {
                                    putInt(Constant.PARAM_ID, data.target_id)
                                }))
                        }
                        6 -> MallDetailActivity.navigation(mActContext, data.target_id)
                        //  7 -> startActivityAfterLogin(CouponCenterActivity::class.java)
                    }
                    return
                }
            }

        }).start()
    }


    fun bindCategory(category: ArrayList<MallCategory>) {

        mAdapter.setNewInstance(category)
        if (category.isNotEmpty()) {
            mAdapter.data[0].nativeIsSelected = true
            mAdapter.notifyItemChanged(0)
        }

        val layoutParams = categoryRv.layoutParams as LinearLayout.LayoutParams

        categoryRv.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                categoryRv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val height = categoryRv.height


                if (height < SizeUtils.dp2px(64f)) {
                    layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                } else {
                    // 多行只展示两行
                    layoutParams.height = SizeUtils.dp2px(64f)
                }
                categoryRv.layoutParams = layoutParams

            }
        })


    }


}


interface OnChildCategoryClick {
    fun onChildCategoryClick(category: MallCategory)
}
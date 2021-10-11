package com.jcs.where.features.hotel.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jcs.where.R
import com.jcs.where.api.response.HotelRoomListResponse
import com.jcs.where.api.response.hotel.HotelDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter
import com.jcs.where.features.hotel.map.HotelMapActivity
import com.jcs.where.frams.common.Html5Url
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter
import com.jcs.where.hotel.activity.detail.MediaData
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.MobUtil
import com.jcs.where.widget.calendar.JcsCalendarAdapter
import com.jcs.where.widget.calendar.JcsCalendarAdapter.CalendarBean
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.*
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.address_tv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.back_iv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.facebook_tv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.like_iv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.media_fl
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.media_rv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.name_tv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.nav_ll
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.phone_ll
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.phone_tv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.point_view
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.scrollView
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.share_iv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.time_tv
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.toolbar
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.useView
import kotlinx.android.synthetic.main.activity_mechanism.*
import java.util.*


/**
 * Created by Wangsw  2021/10/8 11:33.
 *
 */
class HotelDetailActivity2 : BaseMvpActivity<HotelDetailPresenter>(), HotelDetailView {


    /** 酒店分类 id ,用户获取酒店下的子分类 */
    private var hotelId = 0

    /** 星级 */
    var starLevel: String? = null

    /** 价格区间 */
    var priceRange: String? = null

    /** 酒店分数 */
    var grade: String? = null


    /** 房间 */
    private lateinit var mRoomAdapter: HotelRoomAdapter

    /** 设施 */
    private lateinit var mFacilitiesAdapter: HotelFacilitiesAdapter

    /** 评价 */
    private lateinit var mCommentAdapter: HotelCommentAdapter

    /** 轮播 */
    private lateinit var mMediaAdapter: DetailMediaAdapter

    private var isToolbarDark = false

    /** 是否收藏 */
    private var collect_status = 1


    private var businessPhone = ""
    private var webUrl = ""
    private var facebook = ""
    private var mLat = 0.0
    private var mLng = 0.0
    private lateinit var mStartDateBean: CalendarBean
    private lateinit var mEndDateBean: CalendarBean

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_hotel_detail_new_2


    companion object {

        fun navigation(
            context: Context,
            hotelId: Int,
            startDate: JcsCalendarAdapter.CalendarBean,
            endDate: JcsCalendarAdapter.CalendarBean,
            starLevel: String? = null,
            priceRange: String? = null,
            grade: String? = null
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_HOTEL_ID, hotelId)
                putString(Constant.PARAM_STAR_LEVEL, starLevel)
                putString(Constant.PARAM_PRICE_RANGE, priceRange)
                putString(Constant.PARAM_GRADE, grade)
                putSerializable(Constant.PARAM_START_DATE, startDate)
                putSerializable(Constant.PARAM_END_DATE, endDate)
            }
            val intent = Intent(context, HotelMapActivity::class.java)
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
        initScroll()
        initMedia()
        initRoom()
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            hotelId = getInt(Constant.PARAM_CATEGORY_ID)
            starLevel = getString(Constant.PARAM_STAR_LEVEL)
            priceRange = getString(Constant.PARAM_PRICE_RANGE)
            grade = getString(Constant.PARAM_GRADE)
            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as CalendarBean
        }
        (mStartDateBean.showMonthDayDate + mEndDateBean.showMonthDayDate).also { select_date_tv.text = it }


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

        // 禁用滑动
        room_rv.layoutManager = object : LinearLayoutManager(this, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
        comment_rv.layoutManager = object : LinearLayoutManager(this, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }

        facility_rv.layoutManager = object : GridLayoutManager(this, 2, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    /**
     * 处理轮播
     */
    private fun initMedia() {
        mMediaAdapter = DetailMediaAdapter()
        PagerSnapHelper().attachToRecyclerView(media_rv)

        media_rv.apply {
            layoutManager = LinearLayoutManager(this@HotelDetailActivity2, LinearLayoutManager.HORIZONTAL, false)
            adapter = mMediaAdapter
        }
        media_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                val linearManager = recyclerView.layoutManager as LinearLayoutManager
                val firstItemPosition: Int = linearManager.findFirstVisibleItemPosition()
                val lastItemPosition: Int = linearManager.findLastVisibleItemPosition()

                //大于0说明有播放
                if (GSYVideoManager.instance().playPosition >= 0) {

                    //当前播放的位置
                    val position = GSYVideoManager.instance().playPosition

                    // 对应的播放列表TAG
                    if (GSYVideoManager.instance().playTag == DetailMediaAdapter.TAG && (position < firstItemPosition || position > lastItemPosition)) {
                        if (GSYVideoManager.isFullState(this@HotelDetailActivity2)) {
                            return
                        }
                        //如果滑出去了上面和下面就是否
                        GSYVideoManager.releaseAllVideos()
                        mMediaAdapter.notifyDataSetChanged()
                    }

                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    point_view.onPageSelected(firstItemPosition)
                }


            }


        })


    }

    private fun initRoom() {
        mRoomAdapter = HotelRoomAdapter()
        room_rv.adapter = mRoomAdapter

    }


    override fun initData() {
        presenter = HotelDetailPresenter(this)
        presenter.getData(hotelId)
    }

    override fun bindListener() {
        phone_ll.setOnClickListener {
            val data = Uri.parse("tel:$businessPhone")
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = data
            startActivity(intent)
        }
        nav_ll.setOnClickListener {
            FeaturesUtil.startNaviGoogle(this, mLat.toFloat(), mLng.toFloat())
        }
        facebook_tv.setOnClickListener {
            WebViewActivity.goTo(this, facebook)
        }
        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_HOTEL, hotelId)
            MobUtil.shareFacebookWebPage(url, this@HotelDetailActivity2)
        }
        like_iv.setOnClickListener {
            if (collect_status == 1) {
                presenter.collection(hotelId)
            } else {
                presenter.unCollection(hotelId)
            }
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
        share_iv.setImageResource(
            if (isToolbarDark) {
                R.mipmap.ic_share_night
            } else {
                R.mipmap.ic_share_light
            }
        )
    }


    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun bindDetail(response: HotelDetail, mediaList: ArrayList<MediaData>) {

        collect_status = response.collect_status
        businessPhone = response.tel
        facebook = response.facebook_link
        setLikeImage()

        // 轮播
        mMediaAdapter.setNewInstance(mediaList)
        point_view.setPointCount(mediaList.size)

        star_view.rating = response.grade
        name_tv.text = response.name
        score_tv.text = response.grade.toString()
        score_retouch_tv.text = FeaturesUtil.getGradeRetouchString(response.grade)

        time_tv.text = response.start_business_time
        phone_tv.text = response.tel
        address_tv.text = response.address
        facebook_tv.text = response.facebook_link

        // 评价
        mCommentAdapter.setNewInstance(response.comments)
        mFacilitiesAdapter.setNewInstance(response.facilities)

        // 政策

        response.policy?.let {
            hotel_clock_tv.text =
                getString(R.string.check_in_out_time_format, it.check_in_time, it.check_out_time)

            hotel_child_tv.text = it.children
        }




    }

    override fun collectionHandleSuccess(collectionStatus: Int) {
        collect_status = collectionStatus
        setLikeImage()
    }

    override fun bindRoom(toMutableList: MutableList<HotelRoomListResponse>) = mRoomAdapter.setNewInstance(toMutableList)


}
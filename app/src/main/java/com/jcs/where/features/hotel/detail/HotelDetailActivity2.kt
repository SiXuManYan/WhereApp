package com.jcs.where.features.hotel.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.HotelRoomListResponse
import com.jcs.where.api.response.hotel.HotelDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.hotel.comment.HotelCommentActivity2
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter
import com.jcs.where.features.hotel.detail.room.RoomDetailFragment
import com.jcs.where.frams.common.Html5Url
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter
import com.jcs.where.hotel.activity.detail.MediaData
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.MobUtil
import com.jcs.where.widget.calendar.JcsCalendarAdapter.CalendarBean
import com.jcs.where.widget.list.DividerDecoration
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.*
import java.util.*


/**
 * Created by Wangsw  2021/10/8 11:33.
 * 酒店详情
 */
class HotelDetailActivity2 : BaseMvpActivity<HotelDetailPresenter>(), HotelDetailView {


    private var hotelId = 0

    /** 星级 */
    var starLevel: String? = null

    /** 价格区间 */
    var priceRange: String? = null

    /** 酒店分数 */
    var grade: String? = null


    /** 房间数量 */
    private var roomNumber = 1

    private lateinit var mStartDateBean: CalendarBean
    private lateinit var mEndDateBean: CalendarBean


    /** 房间 */
    private lateinit var mRoomAdapter: HotelRoomAdapter

    /** 设施 */
    private lateinit var mFacilitiesAdapter: HotelFacilitiesAdapter

    /** 评价 */
    private lateinit var mCommentAdapter: HotelCommentAdapter

    /** 轮播 */
    private lateinit var mMediaAdapter: DetailMediaAdapter

    private var isToolbarDark = false

    /** 是否收藏  收藏状态（1：已收藏，2：未收藏） */
    private var collect_status = 1


    private var businessPhone = ""
    private var webUrl = ""
    private var facebook = ""
    private var mLat = 0.0
    private var mLng = 0.0


    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_hotel_detail_new_2


    companion object {

        fun navigation(
            context: Context,
            hotelId: Int,
            startDate: CalendarBean,
            endDate: CalendarBean,
            starLevel: String? = null,
            priceRange: String? = null,
            grade: String? = null,
            roomNumber :Int? = 1
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_HOTEL_ID, hotelId)
                putString(Constant.PARAM_STAR_LEVEL, starLevel)
                putString(Constant.PARAM_PRICE_RANGE, priceRange)
                putString(Constant.PARAM_GRADE, grade)
                putSerializable(Constant.PARAM_START_DATE, startDate)
                putSerializable(Constant.PARAM_END_DATE, endDate)
                putInt(Constant.PARAM_ROOM_NUMBER, roomNumber!!)
            }
            val intent = Intent(context, HotelDetailActivity2::class.java)
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
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            hotelId = getInt(Constant.PARAM_HOTEL_ID)
            starLevel = getString(Constant.PARAM_STAR_LEVEL)
            priceRange = getString(Constant.PARAM_PRICE_RANGE)
            grade = getString(Constant.PARAM_GRADE)
            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as CalendarBean
            roomNumber =  getInt(Constant.PARAM_ROOM_NUMBER,1)
        }
        (mStartDateBean.showMonthDayDate + " / " + mEndDateBean.showMonthDayDate).also { select_date_tv.text = it }


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
        mRoomAdapter = HotelRoomAdapter()
        mRoomAdapter.setOnItemClickListener { _, _, position ->
            val data = mRoomAdapter.data[position]
            RoomDetailFragment.newInstance(
                name_tv.text.toString(),
                hotelId,
                data.id,
                mStartDateBean,
                mEndDateBean,
                starLevel,
                priceRange,
                grade,
                roomNumber
            ).apply {
                show(supportFragmentManager, this.tag)
            }
        }

        room_rv.layoutManager = object : LinearLayoutManager(this, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
        room_rv.addItemDecoration(
            DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), 0, 0)
        )
        room_rv.adapter = mRoomAdapter

        mCommentAdapter = HotelCommentAdapter()
        comment_rv.layoutManager = object : LinearLayoutManager(this, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
        comment_rv.adapter = mCommentAdapter


        mFacilitiesAdapter = HotelFacilitiesAdapter()
        facility_rv.layoutManager = object : GridLayoutManager(this, 2, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
        facility_rv.adapter = mFacilitiesAdapter
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


    override fun initData() {
        presenter = HotelDetailPresenter(this)
        presenter.getData(hotelId)
        presenter.getRoomList(hotelId, mStartDateBean.showMonthDayDate, mEndDateBean.showMonthDayDate, 1)
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
                presenter.unCollection(hotelId)
            } else {
                presenter.collection(hotelId)
            }
        }
        back_iv.setOnClickListener {
            finish()
        }
        more_comment_tv.setOnClickListener {

            startActivityAfterLogin(HotelCommentActivity2::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, hotelId);
            })
        }


    }


    private fun setLikeImage() {

        like_iv.setImageResource(
            if (collect_status == 1) {
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

        response.facebook_link.apply {
            facebook_tv.visibility = if (isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            facebook_tv.text = this
        }

        // 评价
        mCommentAdapter.setNewInstance(response.comments)

        // 政策
        response.policy?.let {
            hotel_clock_tv.text =
                getString(R.string.check_in_out_time_format, it.check_in_time, it.check_out_time)

            hotel_child_tv.text = it.children
        }

        // 设施
        mFacilitiesAdapter.setNewInstance(response.facilities)

        // 介绍
        desc_tv.text = response.desc


    }

    override fun collectionHandleSuccess(collectionStatus: Boolean) {
        collect_status = if (collectionStatus) {
            1
        } else {
            2
        }
        setLikeImage()
    }

    override fun bindRoom(toMutableList: MutableList<HotelRoomListResponse>) = mRoomAdapter.setNewInstance(toMutableList)


}
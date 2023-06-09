package com.jiechengsheng.city.features.hotel.detail.room

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.hotel.RoomDetail
import com.jiechengsheng.city.base.mvp.BaseBottomSheetDialogFragment
import com.jiechengsheng.city.base.mvp.FixedHeightBottomSheetDialog
import com.jiechengsheng.city.features.hotel.book.HotelBookActivity
import com.jiechengsheng.city.features.hotel.detail.HotelFacilitiesAdapter
import com.jiechengsheng.city.features.hotel.detail.media.DetailMediaAdapter
import com.jiechengsheng.city.features.hotel.detail.media.MediaData
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.calendar.JcsCalendarAdapter
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.fragment_hotel_room.*
import java.math.BigDecimal
import java.util.*


/**
 * Created by Wangsw  2021/10/12 10:41.
 *  房间详情
 */
class RoomDetailFragment : BaseBottomSheetDialogFragment<RoomDetailPresenter>(), RoomDetailView {


    private var hotelId = 0

    private var roomId = 0

    /** 星级 */
    var starLevel: String? = null

    /** 价格区间 */
    var priceRange: String? = null

    /** 酒店分数 */
    var grade: String? = null

    var singlePrice: BigDecimal = BigDecimal.ZERO
    var hotelRoomType = ""
    private var hotelName = ""
    private var roomImage = ""

    /** 房间数量 */
    private var roomNumber = 1

    /** 房间类型 1展示 其他隐藏 */
    private var roomStatus = 0

    private lateinit var mStartDateBean: JcsCalendarAdapter.CalendarBean
    private lateinit var mEndDateBean: JcsCalendarAdapter.CalendarBean


    /** 轮播 */
    private lateinit var mMediaAdapter: DetailMediaAdapter

    /** 设施 */
    private lateinit var mFacilitiesAdapter: HotelFacilitiesAdapter


    companion object {


        fun newInstance(
            hotelName: String,
            hotelId: Int,
            roomId: Int,
            startDate: JcsCalendarAdapter.CalendarBean,
            endDate: JcsCalendarAdapter.CalendarBean,
            starLevel: String? = null,
            priceRange: String? = null,
            grade: String? = null,
            roomNumber: Int? = 1,
            roomShowStatus:Int?=0
        ): RoomDetailFragment {

            val fragment = RoomDetailFragment()

            val apply = Bundle().apply {
                putString(Constant.PARAM_NAME, hotelName)
                putInt(Constant.PARAM_HOTEL_ID, hotelId)
                putInt(Constant.PARAM_ROOM_ID, roomId)
                putString(Constant.PARAM_STAR_LEVEL, starLevel)
                putString(Constant.PARAM_PRICE_RANGE, priceRange)
                putString(Constant.PARAM_GRADE, grade)
                putSerializable(Constant.PARAM_START_DATE, startDate)
                putSerializable(Constant.PARAM_END_DATE, endDate)

                roomNumber?.let {
                    putInt(Constant.PARAM_ROOM_NUMBER, it)
                }
                roomShowStatus?.let {
                    putInt(Constant.PARAM_STATUS, it)
                }
            }
            fragment.arguments = apply

            return fragment
        }


    }


    override fun getLayoutId() = R.layout.fragment_hotel_room


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 设置最大高度和展开高度
        val height = ScreenUtils.getScreenHeight() * 9 / 10

        return FixedHeightBottomSheetDialog(requireContext(), theme, height)
    }


    override fun initView(parent: View) {
        initExtra()
        initMedia()
        initList()

    }

    private fun initExtra() {

        arguments?.apply {
            hotelName = getString(Constant.PARAM_NAME, "")
            hotelId = getInt(Constant.PARAM_HOTEL_ID)
            roomId = getInt(Constant.PARAM_ROOM_ID)
            starLevel = getString(Constant.PARAM_STAR_LEVEL)
            priceRange = getString(Constant.PARAM_PRICE_RANGE)
            grade = getString(Constant.PARAM_GRADE)
            roomNumber = getInt(Constant.PARAM_ROOM_NUMBER)
            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as JcsCalendarAdapter.CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as JcsCalendarAdapter.CalendarBean
            roomStatus =  getInt(Constant.PARAM_STATUS,0)
        }
        name_tv.text = hotelName
        title_name_tv.text = hotelName

//        booking_tv.visibility = if (roomStatus == 1) {
//            View.VISIBLE
//        }else {
//            View.GONE
//        }
        // 2022-08-05 物理隐藏预订按钮

    }


    override fun initData() {
        presenter = RoomDetailPresenter(this)
        presenter.getDetail(
            roomId,
            roomNumber,
            mStartDateBean.showYearMonthDayDateWithSplit,
            mEndDateBean.showYearMonthDayDateWithSplit
        )
    }


    private fun initMedia() {
        mMediaAdapter = DetailMediaAdapter()
        PagerSnapHelper().attachToRecyclerView(media_rv)
        media_rv.isNestedScrollingEnabled = true

        media_rv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                        if (GSYVideoManager.isFullState(this@RoomDetailFragment.activity)) {
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

    private fun initList() {
        mFacilitiesAdapter = HotelFacilitiesAdapter()

        facility_rv.layoutManager = object : GridLayoutManager(requireContext(), 2, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = true
        }
        facility_rv.adapter = mFacilitiesAdapter
        facility_rv.isNestedScrollingEnabled = true

    }

    override fun bindDetail(response: RoomDetail, mediaList: ArrayList<MediaData>) {

        // 轮播
        mMediaAdapter.setNewInstance(mediaList)
        point_view.setPointCount(mediaList.size)
        if (mediaList.isNotEmpty()) {
          roomImage =   mediaList[0].src
        }

        hotelRoomType = response.hotel_room_type
        bed_tv.text = hotelRoomType
        when (response.breakfast_type) {
            1 -> breakfast_tv.text = getString(R.string.breakfast_support)
            2 -> breakfast_tv.text = getString(R.string.breakfast_un_support)
        }
        area_tv.text = response.room_area

        people_tv.text = getString(R.string.people_number_format, response.people)

        mFacilitiesAdapter.setNewInstance(response.facilities)
        singlePrice = response.price
        price_tv.text = getString(R.string.price_unit_format, response.price.toPlainString())
        note_tv.text = if (response.is_cancel == 1) {
            getString(R.string.cancelable)
        } else {
            getString(R.string.not_cancelable)
        }

    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            dismissAllowingStateLoss()
        }
        booking_tv.setOnClickListener {

            HotelBookActivity.navigation(
                requireContext(),
                hotelRoomId = roomId,
                singlePrice = singlePrice.toDouble(),
                roomType = hotelRoomType,
                breakFastType = breakfast_tv.text.toString(),
                roomArea = area_tv.text.toString(),
                roomPeople = people_tv.text.toString(),
                hotelName = hotelName,
                cancelable = note_tv.text.toString(),
                roomNumber = roomNumber,
                roomImage= roomImage,
                startDate = mStartDateBean,
                endDate = mEndDateBean
            )
            dismissAllowingStateLoss()
        }


    }


}
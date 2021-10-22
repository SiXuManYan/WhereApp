package com.jcs.where.features.hotel.detail.room

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.hotel.RoomDetail
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.features.hotel.book.HotelBookActivity
import com.jcs.where.features.hotel.detail.HotelFacilitiesAdapter
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter
import com.jcs.where.hotel.activity.detail.MediaData
import com.jcs.where.utils.Constant
import com.jcs.where.widget.calendar.JcsCalendarAdapter
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

    var price: BigDecimal = BigDecimal.ZERO
    var hotelRoomType = ""
    private var hotelName = ""

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
            grade: String? = null
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
            }
            fragment.arguments = apply

            return fragment
        }


    }


    override fun getLayoutId() = R.layout.fragment_hotel_room



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 设置最大高度和展开高度
        return FixedHeightBottomSheetDialog(requireContext(), theme, SizeUtils.dp2px(710f))
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

            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as JcsCalendarAdapter.CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as JcsCalendarAdapter.CalendarBean

        }


    }


    override fun initData() {
        presenter = RoomDetailPresenter(this)
        presenter.getDetail(roomId, 1, mStartDateBean.showMonthDayDate, mEndDateBean.showMonthDayDate)
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

        hotelRoomType = response.hotel_room_type
        bed_tv.text = hotelRoomType
        when (response.breakfast_type) {
            1 -> breakfast_tv.text = getString(R.string.breakfast_support)
            2 -> breakfast_tv.text = getString(R.string.breakfast_un_support)
        }
        area_tv.text = response.room_area

        people_tv.text = getString(R.string.people_number_format, response.people)

        mFacilitiesAdapter.setNewInstance(response.facilities)
        price = response.price
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
                price = price.toDouble(),
                roomType = hotelRoomType,
                breakFastType = breakfast_tv.text.toString(),
                roomArea = area_tv.text.toString(),
                roomPeople = people_tv.text.toString(),
                hotelName = hotelName,
                cancelable = note_tv.text.toString(),
                startDate = mStartDateBean,
                endDate = mEndDateBean
            )
            dismissAllowingStateLoss()
        }


    }


}
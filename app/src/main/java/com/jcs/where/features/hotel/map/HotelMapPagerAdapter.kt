package com.jcs.where.features.hotel.map

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jcs.where.api.response.category.Category
import com.jcs.where.features.hotel.map.child.HotelChildFragment
import com.jcs.where.widget.calendar.JcsCalendarAdapter

/**
 * Created by Wangsw  2021/9/28 14:34.
 *
 */
class HotelMapPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    var starLevel: String? = null
    var priceRange: String? = null
    var grade: String? = null
    var category: ArrayList<Category> = ArrayList()

    /** 房间数量 */
     var roomNumberCount = 1


     lateinit var startDateBean: JcsCalendarAdapter.CalendarBean
     lateinit var endDateBean: JcsCalendarAdapter.CalendarBean

    override fun getPageTitle(position: Int): CharSequence = category[position].name

    override fun getCount() = category.size

    override fun getItem(position: Int): HotelChildFragment = HotelChildFragment().apply {
        star_level = starLevel
        hotel_type_ids = category[position].id.toString()
        price_range = priceRange
        grade = this@HotelMapPagerAdapter.grade
        mStartDateBean = startDateBean
        mEndDateBean = endDateBean
        roomNumber = roomNumberCount

    }
}
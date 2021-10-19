package com.jcs.where.features.hotel.book

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.request.HotelOrderRequest
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.widget.NumberView2
import com.jcs.where.widget.calendar.JcsCalendarAdapter
import kotlinx.android.synthetic.main.activity_hotel_boolk.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by Wangsw  2021/10/13 15:17.
 *  酒店预订房间
 */
class HotelBookActivity : BaseMvpActivity<HotelBookPresenter>(), HotelBookView, NumberView2.OnValueChangeListener {

    private var hotelRoomId = 0
    private var totalPrice = 0.0
    private var number = 1

    private var roomType = ""
    private var breakFastType = ""
    private var roomArea = ""
    private var roomPeople = ""
    private var hotelName = ""
    private var cancelable = ""


    private lateinit var mStartDateBean: JcsCalendarAdapter.CalendarBean
    private lateinit var mEndDateBean: JcsCalendarAdapter.CalendarBean

    override fun isStatusDark() = true

    companion object {

        fun navigation(
            context: Context,
            hotelRoomId: Int,
            price: Double,
            roomType: String,
            breakFastType: String,
            roomArea: String,
            roomPeople: String,
            hotelName: String,
            cancelable: String,
            startDate: JcsCalendarAdapter.CalendarBean,
            endDate: JcsCalendarAdapter.CalendarBean,
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ROOM_ID, hotelRoomId)
                putDouble(Constant.PARAM_PRICE, price)
                putString(Constant.PARAM_ROOM_TYPE, roomType)
                putString(Constant.PARAM_BREAKFAST, breakFastType)
                putString(Constant.PARAM_ROOM_AREA, roomArea)
                putString(Constant.PARAM_ROOM_PEOPLE, roomPeople)
                putString(Constant.PARAM_NAME, hotelName)
                putString(Constant.PARAM_CANCELABLE, cancelable)

                putSerializable(Constant.PARAM_START_DATE, startDate)
                putSerializable(Constant.PARAM_END_DATE, endDate)


            }
            val intent = Intent(context, HotelBookActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_hotel_boolk

    override fun initView() {
        initExtra()
        number_view.apply {
            alwaysEnableCut = true
            cut_iv.setImageResource(R.mipmap.ic_cut_black)
            add_iv.setImageResource(R.mipmap.ic_add_black)
            updateNumber(1)
            valueChangeListener = this@HotelBookActivity
        }
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            hotelRoomId = getInt(Constant.PARAM_ROOM_ID)
            totalPrice = getDouble(Constant.PARAM_PRICE)
            price_tv.text = getString(R.string.price_unit_format,totalPrice.toString())

            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as JcsCalendarAdapter.CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as JcsCalendarAdapter.CalendarBean

            roomType = getString(Constant.PARAM_ROOM_TYPE, "")
            breakFastType = getString(Constant.PARAM_BREAKFAST, "")
            roomArea = getString(Constant.PARAM_ROOM_AREA, "")
            roomPeople = getString(Constant.PARAM_ROOM_PEOPLE, "")
            hotelName = getString(Constant.PARAM_NAME, "")
            cancelable = getString(Constant.PARAM_CANCELABLE, "")
            name_tv.text = hotelName
            cancel_tv.text = cancelable

        }


    }


    override fun initData() {
        presenter = HotelBookPresenter(this)
        initDefult()
    }

    private fun initDefult() {
        type_tv.text = "·$roomType"
        content_tv.text = "·$breakFastType | $roomArea | $roomPeople"
        cancel_tv.text = "·$cancelable"

        start_date_tv.text = mStartDateBean.showMonthDayDate
        end_date_tv.text = mEndDateBean.showMonthDayDate


    }

    override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
        number = goodNum
    }

    override fun bindListener() {
        booking_tv.setOnClickListener {
            val resident = resident_et.text.toString().trim()

            if (resident.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter)
                return@setOnClickListener
            }
            val userPhone = phone_aet.text.toString().trim()
            if (userPhone.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter)
                return@setOnClickListener
            }
            val apply = HotelOrderRequest().apply {
                hotel_room_id = hotelRoomId
                username = resident
                phone = userPhone
                start_date = mStartDateBean.showYearMonthDayDateWithSplit
                end_date = mEndDateBean.showYearMonthDayDateWithSplit
                room_num = number.toString()
                country_code = "63"
                price = BigDecimalUtil.mul(totalPrice, number.toDouble()).toPlainString()
            }

            presenter.commitOrder(apply)


        }
    }

    override fun commitSuccess(response: HotelOrderCommitResponse) {

        val orderIds = ArrayList<Int>()
        val order = response.order
        orderIds.add(order!!.id)
        val bundle = Bundle()
        bundle.putDouble(Constant.PARAM_TOTAL_PRICE, response.total_price.toDouble())
        bundle.putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
        bundle.putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_HOTEL)
        startActivityAfterLogin(StorePayActivity::class.java, bundle)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        if (baseEvent.code == EventCode.EVENT_CANCEL_PAY) {
            finish()
        }
    }


}
package com.jcs.where.features.hotel.book

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.request.HotelOrderRequest
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.NumberView2
import com.jcs.where.widget.calendar.JcsCalendarAdapter
import kotlinx.android.synthetic.main.activity_hotel_boolk.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal
import java.util.*

/**
 * Created by Wangsw  2021/10/13 15:17.
 *  酒店预订
 */
class HotelBookActivity : BaseMvpActivity<HotelBookPresenter>(), HotelBookView, NumberView2.OnValueChangeListener {

    private var hotelRoomId = 0
    private var singlePrice = 0.0
    private var totalPrice: BigDecimal = BigDecimal.ZERO

    /** 房间数量 */
    private var roomNumber = 1
    private var roomImage = ""

    private var roomType = ""
    private var breakFastType = ""
    private var roomArea = ""
    private var roomPeople = ""
    private var hotelName = ""
    private var cancelable = ""

    /**
     * 国家码
     * 默认 菲律宾+63前缀
     */
    private var mCountryPrefix = StringUtils.getStringArray(R.array.country_prefix)[0]


    private lateinit var mStartDateBean: JcsCalendarAdapter.CalendarBean
    private lateinit var mEndDateBean: JcsCalendarAdapter.CalendarBean

    override fun isStatusDark() = true

    companion object {

        fun navigation(
            context: Context,
            hotelRoomId: Int,
            singlePrice: Double,
            roomType: String,
            breakFastType: String,
            roomArea: String,
            roomPeople: String,
            hotelName: String,
            cancelable: String,
            roomNumber: Int? = 1,
            roomImage: String,
            startDate: JcsCalendarAdapter.CalendarBean,
            endDate: JcsCalendarAdapter.CalendarBean
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ROOM_ID, hotelRoomId)
                putDouble(Constant.PARAM_PRICE, singlePrice)
                putString(Constant.PARAM_ROOM_TYPE, roomType)
                putString(Constant.PARAM_BREAKFAST, breakFastType)
                putString(Constant.PARAM_ROOM_AREA, roomArea)
                putString(Constant.PARAM_ROOM_PEOPLE, roomPeople)
                putString(Constant.PARAM_NAME, hotelName)
                putString(Constant.PARAM_CANCELABLE, cancelable)
                putInt(Constant.PARAM_ROOM_NUMBER, roomNumber!!)
                putString(Constant.PARAM_ROOM_IMAGE, roomImage)

                putSerializable(Constant.PARAM_START_DATE, startDate)
                putSerializable(Constant.PARAM_END_DATE, endDate)
            }

            val intent = if (User.isLogon()) {
                Intent(context, HotelBookActivity::class.java).putExtras(bundle)
            } else {
                Intent(context, LoginActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

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
            MIN_GOOD_NUM = 1
            MAX_GOOD_NUM = 20
            cut_iv.setImageResource(R.mipmap.ic_cut_black_transparent)
            add_iv.setImageResource(R.mipmap.ic_add_black)
            cutResIdCommon = R.mipmap.ic_cut_black
            cutResIdMin = R.mipmap.ic_cut_black_transparent
            addResIdCommon = R.mipmap.ic_add_black
            addResIdMax = R.mipmap.ic_add_black_transparent
            updateNumberJudgeMin(roomNumber)
            cut_iv.visibility = View.VISIBLE
            valueChangeListener = this@HotelBookActivity
        }
        country_tv.text = mCountryPrefix
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            hotelRoomId = getInt(Constant.PARAM_ROOM_ID)
            singlePrice = getDouble(Constant.PARAM_PRICE)

            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as JcsCalendarAdapter.CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as JcsCalendarAdapter.CalendarBean

            roomType = getString(Constant.PARAM_ROOM_TYPE, "")
            breakFastType = getString(Constant.PARAM_BREAKFAST, "")
            roomArea = getString(Constant.PARAM_ROOM_AREA, "")
            roomPeople = getString(Constant.PARAM_ROOM_PEOPLE, "")
            hotelName = getString(Constant.PARAM_NAME, "")
            cancelable = getString(Constant.PARAM_CANCELABLE, "")
            roomNumber = getInt(Constant.PARAM_ROOM_NUMBER, 1)
            roomImage = getString(Constant.PARAM_ROOM_IMAGE, "")
            name_tv.text = hotelName
            cancel_tv.text = cancelable
        }

        handlePrice()


    }

    private fun handlePrice() {
        totalPrice = BigDecimalUtil.mul(singlePrice, roomNumber.toDouble())
        price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }


    override fun initData() {
        presenter = HotelBookPresenter(this)
        initDefault()
    }

    private fun initDefault() {

        "·$roomType".also { type_tv.text = it }
        "·$breakFastType | $roomArea | $roomPeople".also { content_tv.text = it }
        "·$cancelable".also { cancel_tv.text = it }

        start_date_tv.text = mStartDateBean.showMonthDayDate
        end_date_tv.text = mEndDateBean.showMonthDayDate

        GlideUtil.load(this, roomImage, hotel_image_iv, 5)

        val span = (mEndDateBean.time - mStartDateBean.time) / (1000 * 60 * 60 * 24)
        total_date_tv.text = getString(R.string.total_date_format, span.toString())
    }

    override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
        roomNumber = goodNum
        handlePrice()

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
                room_num = roomNumber.toString()
                country_code = mCountryPrefix
                price = totalPrice.toPlainString()
            }

            presenter.commitOrder(apply)
        }

        country_tv.setOnClickListener {
            FeaturesUtil.getCountryPrefix(this) { countryCode: String ->
                mCountryPrefix = countryCode
                country_tv.text = getString(R.string.country_code_format, countryCode)
            }
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